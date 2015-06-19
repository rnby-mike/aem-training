package com.epam.aem_training.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import com.epam.aem_training.core.GoogleAuthHelperService;


@SlingServlet(
        label = "Google Login Servlet",
        paths = {"/services/googlelogout"}
)
public class GoogleLogoutServlet extends SlingSafeMethodsServlet {
	
	@Reference
	private GoogleAuthHelperService helper;
	
    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
   
    	Cookie cookie = req.getCookie("userId");
    	cookie.setValue(null);
    	cookie.setPath("/");
    	cookie.setMaxAge(0);
    	
    	resp.addCookie(cookie);
    	
    	resp.sendRedirect(req.getHeader("referer"));
    }
}
