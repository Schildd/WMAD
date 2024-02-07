/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.service;

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
import util.Login_check;

/**
 *
 * @author upcnet
 */
@Stateless
@Path("entity.user")
public class UserFacadeREST extends AbstractFacade<User> {
  @PersistenceContext(unitName = "PubSubWebServerPU")
  private EntityManager em;

  public UserFacadeREST() {
    super(User.class);
  }
  
  @POST
  @Path("create")
  @Consumes({"application/xml", "application/json"})
  @Produces({"application/xml", "application/json"})
  public User create_and_return(User entity) {
    
    Query q = em.createQuery("SELECT u FROM User u WHERE u.login=:login");
    q.setParameter("login", entity.getLogin());
    List<User> list = q.getResultList();

    if (list.isEmpty()) {
        // If the user doesn't exist, persist the new user and return it
        em.persist(entity);
        em.flush();
        return entity;
    } else {
        // If the user exists, return the existing user
        return list.get(0);
    }
    //throw new RuntimeException("To be completed by the student");
    
  }
  
  @POST
  @Path("login")
  @Produces({"application/xml", "application/json"})
  @Consumes({"application/xml", "application/json"})
  public User login(Login_check login) {
    System.out.println("Login: " + login.login + ", Password: " + login.password);
    
    Query q = em.createQuery("SELECT u FROM User u WHERE u.login=:login AND u.password=:password");
    q.setParameter("login", login.login);
    q.setParameter("password", login.password);

    try {
        return (User) q.getSingleResult();
    } catch (Exception e) {
        return null;
    }
    //throw new RuntimeException("To be completed by the student");
    
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }
  
}
