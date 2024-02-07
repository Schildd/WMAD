package publisher;

import util.Subscription_close;
import util.Message;
import util.Topic;
import java.util.ArrayList;
import java.util.List;
import subscriber.Subscriber;

public class PublisherImpl implements Publisher {

  private List<Subscriber> subscriberSet; // list of its subscribers
  private int numPublishers; // number of publishers of given topic
  private Topic topic; // from which topic is publisher

  public PublisherImpl(Topic topic) {
    subscriberSet = new ArrayList<Subscriber>();
    numPublishers = 1;
    this.topic = topic;
  }

  @Override
  public void incPublishers() {
    numPublishers++;
  }

  @Override
  public int decPublishers() {

    numPublishers--;
    return this.numPublishers;
  }

  @Override
  public void attachSubscriber(Subscriber subscriber) {
      subscriberSet.add(subscriber);
  }

  @Override
  public boolean detachSubscriber(Subscriber subscriber) {
      if(subscriberSet.contains(subscriber)){
          Subscription_close subs_close = new Subscription_close(topic, Subscription_close.Cause.SUBSCRIBER);
          subscriber.onClose(subs_close);
          subscriberSet.remove(subscriber);
          return true;
      }
      else{
          return false;
      }
  }

  @Override
  public void detachAllSubscribers() {
    Subscription_close subs_close = new Subscription_close(topic, Subscription_close.Cause.PUBLISHER);
    
      for (Subscriber sub : subscriberSet){
        sub.onClose(subs_close);
      }
      subscriberSet.removeAll(subscriberSet);
      
  }

  @Override
  public void publish(Message message) {
    for (Subscriber sub : subscriberSet){
        sub.onMessage(message);
      }
  }

  
}
