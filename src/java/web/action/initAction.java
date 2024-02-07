package web.action;

import cart.ShoppingCart;
import javax.servlet.http.*;
import model.CategoryModel;
import web.ViewManager;

public class initAction implements Action {

    CategoryModel categoryModel;
    ShoppingCart shoppingCart;

    public initAction(CategoryModel categoryModel){
        this.categoryModel = categoryModel;
        this.shoppingCart = new ShoppingCart();
    }

    public void perform(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("categories", categoryModel.retrieveAll());
        req.getSession().setAttribute("cart", shoppingCart);
        ViewManager.nextView(req, resp, "/view/init.jsp");
    }
}
