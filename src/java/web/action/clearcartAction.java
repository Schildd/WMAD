/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.action;

import cart.ShoppingCart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import web.ViewManager;

/**
 *
 * @author entel
 */
public class clearcartAction implements Action{

    public void clearcartAction(){
        
    }
    
    @Override
    public void perform(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ShoppingCart cart = (ShoppingCart)req.getSession().getAttribute("cart");
        cart.clear();
        ViewManager.nextView(req, resp, "/view/Cartview.jsp");
    }
    
    
    
}
