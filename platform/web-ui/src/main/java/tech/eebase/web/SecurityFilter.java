package tech.eebase.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class SecurityFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);
    
    private String roleRequired;
    
    private static final List<String> UNSECURED_URI;
    static {
        UNSECURED_URI = new ArrayList<>();
        UNSECURED_URI.add("/public/");
        UNSECURED_URI.add("/static/");
        UNSECURED_URI.add("/jakarta.faces.resource/");
        UNSECURED_URI.add("/omnifaces.push/");
        UNSECURED_URI.add("/favicon.ico");
        UNSECURED_URI.add("/login.xhtml");
        UNSECURED_URI.add("/expired.xhtml");
        UNSECURED_URI.add("/500.xhtml");
        UNSECURED_URI.add("/access-denied.xhtml");
        UNSECURED_URI.add("/error-page.xhtml");
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        roleRequired = filterConfig.getInitParameter("tech.eebase.REQUIRED_ROLE");
        if (roleRequired == null) {
            roleRequired = "USER";
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();
        
        LOG.info("********** {}", requestURI);
        
        for (int i = 0; i < UNSECURED_URI.size(); i++) {
            if (requestURI.startsWith(UNSECURED_URI.get(i))) {
                chain.doFilter(request, response);
                return;
            }
        }
        
        boolean userAuthenticated = req.isUserInRole(roleRequired);
        
        if (userAuthenticated) {
            if (requestURI.contains("/logout")) {
                chain.doFilter(request, response);
                return;
            }
            
            try {
                chain.doFilter(request, response);
            } catch (Exception e) {
                if (e.getMessage() == null) return;
                throw new ServletException(getRootCause(e));
            }
        } else {
            String loginPage = "/login.xhtml";
            RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher(loginPage);
            dispatcher.forward(req, response);
        }
    }

    private Throwable getRootCause(Throwable e) {
        Throwable cause = null;
        Throwable result = e;

        if (e == null) {
            return null;
        }

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }
}
