package tech.eebase.web.managed.beans.security;

import java.io.Serializable;
import java.security.Principal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import tech.eebase.security.RoleService;

@Named
@SessionScoped
public class CurrentUserMB implements Serializable {

    private static final long serialVersionUID = -1644328257781974048L;

    private static final Logger LOG = LoggerFactory.getLogger(CurrentUserMB.class);
    
    @Inject
    private SecurityContext securityContext;
    
    @EJB
    private RoleService roleService;
    
    @PostConstruct
    public void init() {
        LOG.info("securityContext={}", securityContext);
        LOG.info("roles={}", roleService.getAllCurrentUserRoles());
    }
    
    public String getUserLogin() {
        Principal p = securityContext.getCallerPrincipal();
        return p.getName();
    }
    
}
