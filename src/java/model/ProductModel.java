
package model;

import entity.Product;
import entity.Category;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author juanluis
 */
public class ProductModel {

    UserTransaction utx;
    EntityManager em;

    public ProductModel(EntityManager em, UserTransaction utx) {
        this.utx = utx;
        this.em = em;
    }
   
    public Product retrieveById(int id){
        Query q = em.createQuery("SELECT p FROM Product p WHERE p.id = :id");
        q.setParameter("id", id);
        System.out.print("product query" + (Product) q.getSingleResult());
        return (Product) q.getSingleResult();
    }

   
    public List<Product> retrieveAll(){
        Query q = em.createQuery("select o from Product as o");
        return q.getResultList();
    }
   
    public List<Product> ProductList(Category category){
        Query q = em.createQuery("SELECT p FROM Product p WHERE p.category = :category");
        q.setParameter("category", category);
        return q.getResultList();
    }
   
}