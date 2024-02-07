package demo.web;

import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
// import demo.web.ViewManager;


public class ControllerServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        process(request, response);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        perform_action(request, response);
        
        //String view = perform_action(request, response);
        //forwardRequest(request, response, view);
    }

    protected void perform_action(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        
        String serv_path = request.getServletPath();
        HttpSession session = request.getSession();

        if (serv_path.equals("/login.do")) {
            String user = request.getParameter("user");
            String pswd = request.getParameter("password");
            
            RemoteLogin remote_login = getRemoteLogin();
            
            
            UserAccess access = remote_login.connect(user, pswd);
            
            if (access == null){
                // request.getSession().setAttribute("useraccess", access);
                ViewManager.nextView(request, response, "/error-not-loggedin.html");
            }else{            
                request.getSession().setAttribute("useraccess", access);
                ViewManager.nextView(request, response, "/view/wallview.jsp");
            }
        } 
        
        else if (serv_path.equals("/put.do")) {
            String message = request.getParameter("msg");
            UserAccess user = (UserAccess) request.getSession().getAttribute("useraccess");
            
            user.put(message);
            request.getSession().setAttribute("useraccess", user);
            ViewManager.nextView(request, response, "/view/wallview.jsp");
        } 
        
        else if (serv_path.equals("/refresh.do")) {
            //...
           ViewManager.nextView(request, response, "/view/wallview.jsp");
        } 
        
        else if (serv_path.equals("/logout.do")) {
            //...
            request.getSession().removeAttribute("useraccess");
            ViewManager.nextView(request, response, "/goodbye.html");
            //return "/goodbye.html";
        } 
        
        else if (serv_path.equals("/delete.do")) {
            UserAccess user = (UserAccess) request.getSession().getAttribute("useraccess");
            int index = Integer.parseInt(request.getParameter("index"));
            
            if(index != -1){
                user.delete(index);
            }
            
            ViewManager.nextView(request, response, "/view/wallview.jsp");
            //return "/wallview";
        }
        
        else {
            ViewManager.nextView(request, response, "/error-bad-action.html");
            // return "/error-bad-action.html";
        }
    }

    public RemoteLogin getRemoteLogin() {
        return (RemoteLogin) getServletContext().getAttribute("remoteLogin");
    }
    public void forwardRequest(HttpServletRequest request, HttpServletResponse response, String view) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(view);
        if (dispatcher == null) {
            throw new ServletException("No dispatcher for view path '"+view+"'");
        }
        dispatcher.forward(request,response);
    }
}


