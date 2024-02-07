
package cart;

import entity.Product;
import java.util.*;
import java.text.DecimalFormat;

/**
 *
 * @author juanluis
 */
public class ShoppingCart {

    // add list of items
    private final List<ShoppingCartItem> cartItems;
   
    public ShoppingCart(){
        cartItems = new ArrayList<ShoppingCartItem>();
    }
    // to do continue here
    public synchronized void addItem(Product product){
        /*
        if(product.getQuantity() == 0){
            return;
        }
        */        

        for (ShoppingCartItem item : cartItems){
            if (item.getProduct().equals(product)){
                item.setQuantity(item.getQuantity()+1);
                return;
            }
        }
       
        ShoppingCartItem newItem = new ShoppingCartItem(product);
        cartItems.add(newItem);
        
        // product.setQuantity(product.getQuantity()- 1);
        
    }
   
    public synchronized void update(Product product, int quantity){
        for (ShoppingCartItem item : cartItems){
            if(item.getProduct().equals(product)){
                if(quantity!=0 && quantity > 0){
                    item.setQuantity(quantity);
                }
                else{
                    cartItems.remove(item);
                }
                return;
            }
        }
    }
   
    public synchronized List<ShoppingCartItem> getItems(){
        return cartItems;
    }
   
    public synchronized int getNumberOfItems(){
        int totalItems = 0;
        for(ShoppingCartItem item : cartItems){
            totalItems += item.getQuantity();
        }
        return totalItems;
    }
   
    public synchronized double getTotal(){
        double total = 0;
        for(ShoppingCartItem item : cartItems){
            total += item.getTotal();
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedTotal = decimalFormat.format(total);
        return Double.parseDouble(formattedTotal);
    }
   
    public synchronized void clear(){
        cartItems.clear();
    }

}