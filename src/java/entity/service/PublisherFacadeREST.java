/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.service;

import entity.Publisher;
import entity.Topic;
import util.Subscription_close;
import entity.User;
import java.sql.ResultSet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import webSocketService.WebSocketServer;

/**
 *
 * @author juanluis
 */

@Stateless
@Path("entity.publisher")
public class PublisherFacadeREST extends AbstractFacade<Publisher> {
  
  @PersistenceContext(unitName = "PubSubWebServerPU")
  private EntityManager em;

  public PublisherFacadeREST() {
    super(Publisher.class);
  }
  
  @POST
  @Path("create")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public void create(Publisher entity) {
    
    // first, create the topic if necessary, otherwise
    // set the topic retrieved from the query into the argument
    // of the method create, entity, to be saved as a new publisher:
    
    Query query = em.createQuery("SELECT p FROM Topic p WHERE p.name = :topic");
    
    query.setParameter("topic", entity.getTopic().getName()); 
    
    List<entity.Topic> resultList = query.getResultList();
    
    entity.Topic topic;
    
    if (resultList.isEmpty()){
        // Create the new topic
        topic = new Topic();
        topic.setName(entity.getTopic().getName());
        em.persist(topic);
        em.flush();
    }
    else {
        topic = resultList.get(0);
    }
    

    // then, create the new publisher for that topic, or
    // modify it, if he/she was publisher of a previous topic:
    
    Query publisherQuery = em.createQuery("SELECT p FROM Publisher p WHERE p.user = :user");
    
    publisherQuery.setParameter("user", entity.getUser());
    
    List<Publisher> publisherList = publisherQuery.getResultList();
    
    Publisher publisher;
    
    if (publisherList.isEmpty()) {
        // Create a new publisher
        publisher = new Publisher();
        publisher.setUser(entity.getUser());
        publisher.setTopic(topic);
        // Set other properties as needed
        
        System.out.println("publisher: "+publisher.toString());
        em.persist(publisher);
        em.flush();
    } else {
        // Update the existing publisher if necessary
        publisher = publisherList.get(0);
        // Modify the existing publisher properties as needed
    }
    
    
  }

  @POST
  @Path("delete")
  @Consumes({"application/xml", "application/json"})
  public void delete(Publisher entity) {
      
    Topic original_topic = entity.getTopic();
    
    // check out if the user is really a publisher:
    
    Query query = em.createQuery("SELECT p FROM Publisher p WHERE p.user = :user");
    query.setParameter("user", entity.getUser()); 
    List<entity.Publisher> resultList = query.getResultList();

    // if that is the case, use the fresh new instance of Publisher
    // obtained from the query to the data base to delete that publisher:
    
    if (!resultList.isEmpty()){
        em.remove(resultList.get(0));
        em.flush();
    }

    // check if it was the last publisher of that topic, if so, delete the
    // topic from the Topic table, and notify all the currently connected
    // clients, using the WebSocketServer, about the topic been closed:
    
    Query query5 = em.createQuery("SELECT p FROM Topic p WHERE p.name = :topicName");
    query5.setParameter("topicName", original_topic.getName()); 
    List<entity.Topic> topic_db_list = query5.getResultList();
    
    Query query2 = em.createQuery("SELECT p FROM Publisher p WHERE p.topic = :topic");
    query2.setParameter("topic", topic_db_list.get(0)); 
    List<entity.Publisher> resultList2 = query2.getResultList();
    

    if (resultList2.isEmpty()){
        System.out.println("EMPTY 1 ");
        Subscription_close sub_close = new Subscription_close(entity.getTopic(),Subscription_close.Cause.PUBLISHER);
        
        Query query3 = em.createQuery("SELECT p FROM Subscriber p WHERE p.topic = :topic");
        query3.setParameter("topic", topic_db_list.get(0)); 
        List<entity.Subscriber> list_sub = query3.getResultList();
        
        System.out.println("list_sub.size : "+list_sub.size());
        
        if (!list_sub.isEmpty()){
            System.out.println("NOT EMPTY 2 ");
            for (entity.Subscriber sub : list_sub){
                // Remove all the subsciptions to the closed topic
                em.remove(sub);
                em.flush();
        }
                
        }
        
        // Remove the topic
            em.remove(topic_db_list.get(0));
            em.flush();
        
        
        WebSocketServer.notifyTopicClose(sub_close);
    }
    

  }
  
  @POST
  @Path("publisherOf")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public Publisher publisherOf(User entity) {  
    
    // retrieve if the user is publisher of any topic:
    // Check if the user is a publisher of any topic
    
    Query query = em.createQuery("SELECT p FROM Publisher p WHERE p.user = :user");
    
    query.setParameter("user", entity); 
    
    List<entity.Publisher> resultList = query.getResultList();
    
    
    if (resultList.isEmpty()){
        // no publisher
        return null;
    }
    else {
        // return the publisher
        return resultList.get(0);
        
    }

    
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }
  
}