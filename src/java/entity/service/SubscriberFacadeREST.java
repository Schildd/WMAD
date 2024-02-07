/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.service;

import entity.Subscriber;
import util.Subscription_check;
import entity.Topic;
import entity.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author upcnet
 */
@Stateless
@Path("entity.subscriber")
public class SubscriberFacadeREST extends AbstractFacade<Subscriber> {

  @PersistenceContext(unitName = "PubSubWebServerPU")
  private EntityManager em;

  public SubscriberFacadeREST() {
    super(Subscriber.class);
  }

  @POST
  @Path("create")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public Subscription_check check_to_create(Subscriber entity) {
    
    // first, check out if the topic from which to subscribe is defined at
    // the Topic table, otherwise return the corresponding message:
    
    // ...
    
    // return a Subscription_check after saving the user as subscriber of the
    // topic:
    
    // ...
    
    Query q = em.createQuery("SELECT t FROM Topic t WHERE t.name=:name");
    q.setParameter("name", entity.getTopic().getName());
    List list = q.getResultList();
    if (list.isEmpty()) {
        return new Subscription_check(entity.getTopic(), Subscription_check.Result.NO_TOPIC);
    } else {
        entity.setTopic((Topic) list.get(0));
        q = em.createQuery("SELECT s FROM Subscriber s WHERE s.user=:user AND s.topic=:topic");
        q.setParameter("user", entity.getUser());
        q.setParameter("topic", entity.getTopic());
        list = q.getResultList();
        if (list.isEmpty()) {
            em.persist(entity);
            em.flush();
            return new Subscription_check(entity.getTopic(), Subscription_check.Result.OKAY);
        } else {
            return new Subscription_check(entity.getTopic(), Subscription_check.Result.OKAY);
        }
    }

    //throw new RuntimeException("To be completed by the student");
    
  }

@POST
@Path("delete")
@Consumes({"application/xml", "application/json"})
public Subscription_check checkToDelete(Subscriber entity) {
    // Check if the topic exists
    Query q = em.createQuery("SELECT t FROM Topic t WHERE t.name=:name");
    q.setParameter("name", entity.getTopic().getName());
    List list = q.getResultList();

    if (list.isEmpty()) {
        // If the topic doesn't exist, return a result indicating no topic
        return new Subscription_check(entity.getTopic(), Subscription_check.Result.NO_TOPIC);
    } else {
        // Set the topic from the database
        entity.setTopic((Topic) list.get(0));

        // Check if the user is subscribed to the topic
        q = em.createQuery("SELECT s FROM Subscriber s WHERE s.user=:user AND s.topic=:topic");
        q.setParameter("user", entity.getUser());
        q.setParameter("topic", entity.getTopic());
        list = q.getResultList();

        if (list.isEmpty()) {
            // If not subscribed, return a result indicating no subscription
            return new Subscription_check(entity.getTopic(), Subscription_check.Result.NO_SUBSCRIPTION);
        } else {
            // If subscribed, delete the subscription and return OKAY result
            super.delete((Subscriber) list.get(0));
            return new Subscription_check(entity.getTopic(), Subscription_check.Result.OKAY);
        }
    }
}

@POST
@Path("subscriptions")
@Consumes({"application/xml", "application/json"})
@Produces({"application/xml", "application/json"})
public List<Subscriber> getSubscriptions(User entity) {
    // Retrieve subscriptions for a user
    Query q = em.createQuery("SELECT s FROM Subscriber s WHERE s.user=:user");
    q.setParameter("user", entity);
    return q.getResultList();
}

@Override
protected EntityManager getEntityManager() {
    return em;
}


}
