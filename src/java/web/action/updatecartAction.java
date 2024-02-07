/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.action;

import cart.ShoppingCart;
import entity.Product;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ProductModel;
import web.ViewManager;

/**
 *
 * @author entel
 */
public class updatecartAction implements Action{
  
    private ProductModel productModel;
    
    public updatecartAction(ProductModel productModel){
        this.productModel = productModel;
       
    }

    @Override
    public void perform(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        
        ShoppingCart cart = (ShoppingCart)req.getSession().getAttribute("cart");
        String newQuantity = (String)req.getParameter("fname");
        int quantity = Integer.valueOf(newQuantity);
        String String_ID = (String)req.getParameter("productId");
        int productId = Integer.valueOf(String_ID);
        
        Product product = productModel.retrieveById(productId);
        if (quantity >= 0){
        cart.update(product, quantity);
        }
        
        System.out.println("THE CART AFTER UPDATING "+ newQuantity + "AND THIS IS THE NAME OF THE PRODUCT"+ product.getName());
        ViewManager.nextView(req, resp, "/view/Cartview.jsp");
    }
    
}
