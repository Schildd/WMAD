/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.service;

import entity.Message;
import entity.Topic;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
@Path("entity.message")
public class MessageFacadeREST extends AbstractFacade<Message> {

  @PersistenceContext(unitName = "PubSubWebServerPU")
  private EntityManager em;

  public MessageFacadeREST() {
    super(Message.class);
  }

  @POST
  @Path("create")
  @Consumes({"application/xml", "application/json"})
  public void create(Message entity) {
    

    // check out if the topic of this message is defined:
    
    // ...
    // create a query and request the Topics
    Query query = em.createQuery("SELECT t FROM Topic t WHERE t.name=:name");
    query.setParameter("name", entity.getTopic().getName());
    
    
    // save the new message and use the WebSocketServer to forward that message
    // to the currently connected subscribers of the involved topic.
    // WARNING!!! do not use the same instance of Message to save and forward
    // the message, make a copy of the message and use both of them, one for each
    // action.
    
    // ...
    List<Topic> list = query.getResultList();
    if (!list.isEmpty()) {
      //I had to do this beacuse otherwise notifyNewMessage makes a stackOverflow:
      Message m = new Message();
      m.setTopic(list.get(0));
      m.setContent(entity.getContent());
      em.persist(m);
      em.flush();
      WebSocketServer.notifyNewMessage(entity);
    }
    
    // throw new RuntimeException("To be completed by the student");
    
  }

  @POST
  @Path("messagesFromTopic")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public List<Message> messagesFrom(Topic entity) {
    
    Query query = em.createQuery("SELECT t FROM Topic t WHERE t.name=:name");
    query.setParameter("name", entity.getName());
    List<Topic> list = query.getResultList();
    if (!list.isEmpty()) {
      query = em.createQuery("SELECT m FROM Message m WHERE m.topic=:topic");
      query.setParameter("topic", list.get(0));
      return query.getResultList();
    }
    else{
      return null;
    }
    
    //throw new RuntimeException("To be completed by the student");
    
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

}
