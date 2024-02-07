<%-- 
    Document   : Product
    Created on : Oct 17, 2023, 3:25:30 PM
    Author     : entel
--%>

<%@page import="cart.ShoppingCart"%>
<%@page import="entity.Product"%>
<%@page import="java.util.List"%>
<%@page import="entity.Category"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<html>
    <style>
    table, th, td {
      border:1px solid black;
    }
    </style>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <% Category Cat = (Category)request.getAttribute("category"); %>
        <title>Products of<%=Cat.getName()%> </title>
    </head>
    <body>
        <%
        ShoppingCart cart = (ShoppingCart)request.getSession().getAttribute("cart");    
        %>
        <h1>Products of <%=Cat.getName()%> </h1>
        <%
        if(cart.getNumberOfItems() > 0){
        %>
        
        <h2> <img src="img/cart.gif"> <%= cart.getNumberOfItems()%> Total Items </h2>
        <a href="viewcart.do">
           Go to Cart
        </a>
     
        
        <%}else{%>
            
        <h2> 0 Total Items </h2>
        <%}%>
             <table border="1">
                <tr>
                    <td>
                        <!-- First Nested Table -->
                        <table border="1">
                            <%
                            List<Category> categories = (List<Category>)request.getAttribute("categories");
                            for(Category category : categories){

                            %> 
                            <tr>
                               
                                    <td width="14%" valign="center" align="middle">
                                        <a href="category.do?categoryid=<%=category.getId()%>">
                                            <%=category.getName()%>
                                        </a>
                                    </td>
                                                           
                            </tr>


                        <% } %>
                        </table>
                    </td>
                    <td>
                        <!-- Second Nested Table -->
                        <table border="1">
                                        <%
                            List<Product> products = (List<Product>)request.getAttribute("products");
                            for(Product product : products){

                            %>
                            <tr>
                                <td valign="center" allign="middle">
                                <a href="category.do?imageid=1">
                                <img src="img/products/<%=product.getName()%>.png"
                                     alt=<%=product.getName()%> >
                                </td>
                                <td valign="center" allign="middle"><%=product.getName()%><br><br><br><%=product.getDescription()%></td>
                                </a>
                                <td valign="center" allign="middle"><%=product.getPrice()%> $</td>
                                <td valign="center" allign="middle">
                                    <form action="neworder.do" method="post">
                                        <input type="hidden"
                                            name="productid"
                                        value=<%=product.getId()%>>
                                        <input type="hidden"
                                            name="categoryid"
                                        value=<%=product.getCategory().getId()%>>
                                        <input type="submit"
                                            name="submit"
                                        value="add to cart">
                                    </form>
                                </td>
                            </tr>

                        <% } %>
                        </table>
                    </td>
                </tr>
            </table>
     
    </body>
</html>
