/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.action;

import cart.ShoppingCart;
import entity.Category;
import entity.Product;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CategoryModel;
import model.ProductModel;
import web.ViewManager;

/**
 *
 * @author entel
 */
public class neworderAction implements Action{
    
    CategoryModel categoryModel;
    ProductModel productModel;
    // ShoppingCart shoppingCart;
    
    public neworderAction (CategoryModel cat, ProductModel prod){
        
        categoryModel = cat;
        productModel = prod;
        
        /*
        ShoppingCart shoppingCartSession = (ShoppingCart) req.getSession().getAttribute("shoppingCart");
        
        if(shoppingCartSession == null){
            shoppingCartSession = new ShoppingCart();
            req.getSession().setAttribute("shoppingCart", shoppingCartSession);
        }
        
        shoppingCart = new ShoppingCart();
        */
        
    }

    @Override
    public void perform(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        
        ShoppingCart shoppingCart = (ShoppingCart) req.getSession().getAttribute("cart");
        
        if(shoppingCart == null){
            shoppingCart = new ShoppingCart();
            req.getSession().setAttribute("cart", shoppingCart);
        }
 
        String cat_id = (String)req.getParameter("categoryid");
        String product_id = (String)req.getParameter("productid");
        System.out.println("categoryid: "+ cat_id);
        System.out.println("ProductId: "+ product_id);
        
        
        
        int id = 0;
        if (cat_id != null){
            
            id = Integer.valueOf(cat_id);
            int product_id_int = Integer.valueOf(product_id);
            Category category = categoryModel.retrive(id);
            List<Product> products = productModel.ProductList(category);

            Product newProduct = productModel.retrieveById(product_id_int);
            shoppingCart.addItem(newProduct);
            
            req.setAttribute("products", productModel.ProductList(category));
            req.setAttribute("categories", categoryModel.retrieveAll());
            req.setAttribute("category", category);
            req.setAttribute("cart", shoppingCart);
            req.getSession().setAttribute("cart", shoppingCart);
            System.out.println("category: "+category.getName());
            System.out.println(shoppingCart.getNumberOfItems());
            ViewManager.nextView(req, resp, "/view/Product.jsp");
            
        }   
        
    }
    
}
