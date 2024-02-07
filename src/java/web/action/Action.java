package web.action;

import javax.servlet.http.*;

public interface Action {
    public void perform(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}