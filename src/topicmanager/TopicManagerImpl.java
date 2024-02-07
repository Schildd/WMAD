package topicmanager;

import util.Subscription_check;
import util.Topic;
import util.Topic_check;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import publisher.Publisher;
import publisher.PublisherImpl;
import subscriber.Subscriber;

public class TopicManagerImpl implements TopicManager {

    // table of currently open topics with corresponding publishers
  private Map<Topic, Publisher> topicMap;

  public TopicManagerImpl() {
    topicMap = new HashMap<Topic, Publisher>();
  }

  @Override
  public Publisher addPublisherToTopic(Topic topic) {
      // check if topic is already open
      // if open just get publisher and increment number of publishers
      // if not create new publisher
      Topic_check topic_check = this.isTopic(topic);
      if (topic_check.isOpen==true){
          Publisher publisher = topicMap.get(topic);
          publisher.incPublishers();
          return publisher;
      }
      else{
        Publisher publisher = new PublisherImpl(topic);      
        topicMap.put(topic, publisher);
        return publisher;  
      }
  }

  @Override
  public void removePublisherFromTopic(Topic topic) {
      
      Publisher publisher = topicMap.get(topic);
      int num_publisher = publisher.decPublishers();
      System.out.println(num_publisher);
      if(num_publisher == 0){
        topicMap.remove(topic);
        publisher.detachAllSubscribers();       
          
      }
         
  }

  @Override
  public Topic_check isTopic(Topic topic) {
    boolean is_topic;
    is_topic = topicMap.containsKey(topic);
    Topic_check topic_check = new Topic_check(topic, is_topic);
    return topic_check;
    
  }

  @Override
  public List<Topic> topics() {
    List<Topic> topic_list;
      topic_list = new ArrayList<Topic>(topicMap.keySet());
      return topic_list;
  }

  @Override
  public Subscription_check subscribe(Topic topic, Subscriber subscriber) {
      // check if there is the topic, if yes attach and return subscription check
      Subscription_check subscription_check = null;
      if(topicMap.containsKey(topic) == true){
        topicMap.get(topic).attachSubscriber(subscriber);
        subscription_check = new Subscription_check(topic, Subscription_check.Result.OKAY);
        
      } else{
        subscription_check = new Subscription_check(topic, Subscription_check.Result.NO_TOPIC);
        return subscription_check;
      }
          
      return subscription_check;
  }

  @Override
  public Subscription_check unsubscribe(Topic topic, Subscriber subscriber) {
      
     Subscription_check subscription_check = null;
     
    if(topicMap.containsKey(topic) == true){
        if(topicMap.get(topic).detachSubscriber(subscriber)){        
            subscription_check = new Subscription_check(topic, Subscription_check.Result.OKAY);
        }
        else{
            subscription_check = new Subscription_check(topic, Subscription_check.Result.NO_SUBSCRIPTION);
        }
    }
    
        else{
            subscription_check = new Subscription_check(topic, Subscription_check.Result.NO_TOPIC);
        }
        
    
        return subscription_check;
    
  }
  
}
