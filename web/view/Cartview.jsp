<%-- 
    Document   : Cartview
    Created on : Oct 27, 2023, 3:04:03 PM
    Author     : entel
--%>

<%@page import="cart.ShoppingCartItem"%>
<%@page import="java.util.List"%>
<%@page import="entity.Product"%>
<%@page import="cart.ShoppingCart"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<html>
    <%
    ShoppingCart cart = (ShoppingCart)request.getSession().getAttribute("cart");  
    System.out.println("NUMBER OF ITEMS IN CART"+cart.getNumberOfItems());
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>Cart Page</title>
    </head>
    <body>
        <img src="img/cart.gif"> <%= cart.getNumberOfItems()%> Total Items 
       
        <a href="clearcart.do">
           <br><br> <br> Clear Cart <br><br><br>
                                        </a>
        
        
        
        <a href="init.do"> Continue Shopping <br><br><br></a>
        
        
        <%
        if(cart.getNumberOfItems()>0){    
        %>
        <p>Click the button below to go to the PayPal checkout page:</p>
        <form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
            <input type="hidden" name="cmd" value="_xclick">
            <input type="hidden" name="business" value="efthimismitk48@gmail.com">
            <input type="hidden" name="item_name" value="Product Name">
            <input type="hidden" name="amount" value=<%=cart.getTotal()%>>
            <input type="hidden" name="currency_code" value="USD">
            <input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_buynow_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online">
        </form>
        <%}%>
        <table border="1"> 
        <tr>
            <td>product</td>
            <td>name</td><!-- comment -->
            <td>price</td><!-- comment -->
            <td>quantity</td>
        </tr>
        
        <%
            List<ShoppingCartItem> products = cart.getItems();
            
            for(ShoppingCartItem product : products){
                Product prod = product.getProduct();
                int quantity = product.getQuantity();
                System.out.println("product name" + prod);
                
        %>
            <tr>
                <td valign="center" allign="middle">
                <img src="img/products/<%=prod.getName()%>.png"
                     alt=<%=prod.getName()%> >
                </td>
                <td valign="center" allign="middle"><%=prod.getName()%><br><br><br><%=prod.getDescription()%></td>
                </a>
                <td valign="center" allign="middle"><%=prod.getPrice()%> $ / unit</td>
                <td valign="center" allign="middle">
                    <form action="updatecart.do" method="post">                        
                        <input type="text" id="fname" name="fname" value=<%=quantity%>><br>
                        <input type="hidden" id="productId" name="productId" value=<%=prod.getId()%>>
                        <input type="submit" value="update">
                    </form>
                </td>
            </tr>
        
        <%}%>
        </table>
        
        <h1>
            Total amount: <%= cart.getTotal() %>
        </h1>
    </body>
</html>



