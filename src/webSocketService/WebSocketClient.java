package webSocketService;

import apiREST.Cons;
import com.google.gson.Gson;
import entity.Message;
import entity.Topic;
import util.Subscription_close;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
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
      
        
        Subscriber subscriber = subscriberMap.remove(topic);
        Subscription_request sub_req = new Subscription_request(topic, Subscription_request.Type.REMOVE);
        
        Subscription_close sub_close = new Subscription_close(topic,Subscription_close.Cause.SUBSCRIBER);
        
        subscriber.onClose(sub_close);

        // Use the session to send the unsubscription request to the server
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText(new Gson().toJson(sub_req));
        }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnMessage
  public void onMessage(String json) {

    Gson gson = new Gson();
    Subscription_close subs_close = gson.fromJson(json, Subscription_close.class);
    
    System.out.println("subs_close: "+subs_close.toString());
    
    //ordinary message from topic:
    if (subs_close.cause==null) {
        System.out.println("subs_close.cause==null");

      // forward the recived message to the subsciber
      Message message = gson.fromJson(json, Message.class);
      Topic topic = message.topic;
      String content = message.content;
      
      Subscriber subscriber = subscriberMap.get(topic);
      subscriber.onMessage(message);

    }
    //ending subscription message:
    else {
        System.out.println("ELSE");
        
      Topic topic = subs_close.topic;
      
        System.out.println("topic, "+topic);
        System.out.println("subs_close, "+subs_close);
        
      Subscriber subscriber = subscriberMap.get(topic);
      if (subscriber!=null){
          subscriber.onClose(subs_close);
      }
    }
  }


}
