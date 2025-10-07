package tech.eebase.web;

import java.io.PrintWriter;
import java.io.StringWriter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class ErrorHandler {

    public String getRequestURI() {
        return (String)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(RequestDispatcher.ERROR_REQUEST_URI);
    }
    
    public String getReferrer() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getHeader("Referer");
    }
    
    public String getRootCause() {
        Throwable errorException = (Throwable)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(RequestDispatcher.ERROR_EXCEPTION);
        if (errorException instanceof ServletException servletException && servletException.getRootCause() != null) {
            Throwable rootCause = servletException.getRootCause();
            return printStackTrace(rootCause);
        }
        return printStackTrace(findRootCause(errorException));
    }

    public String getFullStackTrace() {
        Throwable errorException = (Throwable)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(RequestDispatcher.ERROR_EXCEPTION);
        return printStackTrace(errorException);
    }

    public String printStackTrace(Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter, true));
        return stringWriter.toString();
    }
    
    private Throwable findRootCause(Throwable e) {
        while (e.getCause() != null) {
            e = e.getCause();
        }
        return e;
    }
    
}