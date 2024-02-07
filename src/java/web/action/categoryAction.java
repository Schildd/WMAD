package web.action;
import cart.ShoppingCart;
import entity.Category;
import entity.Product;
import java.util.List;
import javax.servlet.http.*;
import model.CategoryModel;
import model.ProductModel;
import web.ViewManager;

/**
 *
 * @author entel
 */
public class categoryAction implements Action{
    
    CategoryModel categoryModel;
    ProductModel productModel;
    ShoppingCart shoppingCart;

    public categoryAction(CategoryModel categoryModel, ProductModel productModel) {
        this.categoryModel = categoryModel;
        this.productModel = productModel;
        this.shoppingCart = new ShoppingCart();
    }
    
    

    @Override
    public void perform(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String cat_id = (String)req.getParameter("categoryid");
        
        
        
        System.out.println("categoryid: "+ cat_id);
        

        int id = 0;
        if (cat_id != null){
            id = Integer.valueOf(cat_id);
            Category category = categoryModel.retrive(id);
            List<Product> products = productModel.ProductList(category);
            for(Product product : products){
                System.out.println(product.getName());
            }
            
            req.getSession().setAttribute("cart", shoppingCart);
            req.setAttribute("cart", shoppingCart);
            req.setAttribute("products", productModel.ProductList(category));
            req.setAttribute("categories", categoryModel.retrieveAll());
            req.setAttribute("category", category);
            
            System.out.println("category: "+category.getName());
            ViewManager.nextView(req, resp, "/view/Product.jsp");
            
        }   
        
    }
    
    
}
