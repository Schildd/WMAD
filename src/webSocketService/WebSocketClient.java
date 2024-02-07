package webSocketService;

import apiREST.Cons;
import apiREST.apiREST_Publisher;
import com.google.gson.Gson;
import java.io.IOException;
import util.Message;
import util.Topic;
import util.Subscription_close;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import subscriber.Subscriber;
import util.Subscription_request;

@ClientEndpoint
public class WebSocketClient {

  static Map<Topic, Subscriber> subscriberMap;
  static Session session;

  public static void newInstance() {
    subscriberMap = new HashMap<Topic, Subscriber>();
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      session = container.connectToServer(WebSocketClient.class,
        URI.create(Cons.SERVER_WEBSOCKET));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void close() {
    try {
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static synchronized void addSubscriber(Topic topic, Subscriber subscriber) {
    try {
        System.out.println("addsubscriber called \n");
        

      
        subscriberMap.put(topic, subscriber);
        Subscription_request sub_request = new Subscription_request(topic, Subscription_request.Type.ADD);
        
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText(new Gson().toJson(sub_request));
        }
        
        // apiREST_TopicManager.
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static synchronized void removeSubscriber(Topic topic) {
    try {
      
        
        subscriberMap.remove(topic);
        // Subscription_request sub_request = new Subscription_request(topic, Subscription_request.Type.REMOVE);
        Subscription_close sub_close = new Subscription_close(topic, Subscription_close.Cause.SUBSCRIBER);
 
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText(new Gson().toJson(sub_close));
        }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnMessage
  public void onMessage(String json) {

    Gson gson = new Gson();
    Subscription_close subs_close = gson.fromJson(json, Subscription_close.class);
    Message message = gson.fromJson(json, Message.class);
    System.out.println("we have received the message");
    System.out.println(json);
    System.out.println(message.content);
    //ordinary message from topic:
    if (subs_close.cause==null) {            
        subscriberMap.get(message.topic).onMessage(message);
    }
    //ending subscription message:
    else {
      
        Topic topic = subs_close.topic;
        Subscriber subscriber = subscriberMap.get(topic);
        if(subscriber!=null){
            subscriber.onClose(subs_close);
        }
      
    } 
  }

}
