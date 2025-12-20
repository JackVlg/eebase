package tech.eebase.web.managed.beans.security;

import java.io.Serializable;
import java.security.Principal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;

@Named
@SessionScoped
public class CurrentUserMB implements Serializable {

    private static final long serialVersionUID = -1644328257781974048L;

    private static final Logger LOG = LoggerFactory.getLogger(CurrentUserMB.class);
    
    private final transient SecurityContext securityContext;
    
    public CurrentUserMB(SecurityContext securityContext) {
        super();
        this.securityContext = securityContext;
    }

    @PostConstruct
    public void init() {
        LOG.info("securityContext={}", securityContext);
    }
    
    public String getUserLogin() {
        Principal p = securityContext.getCallerPrincipal();
        return p.getName();
    }
}
