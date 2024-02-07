package cart;

import entity.Product;

/**
 *
 * @author juanluis
 */
public class ShoppingCartItem {

    private Product product;
    private int quantity;
    
    public ShoppingCartItem (Product product){
        this.product = product;
        this.quantity = 1;
        
    }
    
    public Product getProduct(){
        return product;
    }

    public int getQuantity (){
        return quantity;
    }
    
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    
    public double getTotal(){
        return this.product.getPrice() * quantity;
    }
}