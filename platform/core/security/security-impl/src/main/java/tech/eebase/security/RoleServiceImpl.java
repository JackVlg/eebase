package tech.eebase.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Local;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.security.enterprise.SecurityContext;
import tech.eebase.security.model.UserRoleDTO;

@Singleton
@Startup
@RolesAllowed({RoleServiceImpl.ROLES_MANAGER_ROLE_NAME, SecuritySupervisor.SUPERVISOR_ROLE_NAME})
@Local(RoleService.class)
public class RoleServiceImpl implements RoleService {

    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);
    
    // TODO Need to make setting
    private String realmRolesPrefix = "EEBR_";

    private static final String ROLES_MANAGER_ROLE_NAME = "ROLES_MANAGER";
    private static final String GROUPS_MANAGER_ROLE_NAME = "GROUPS_MANAGER";
    private static final String PRIVILEGES_MANAGER_ROLE_NAME = "PRIVILEGES_MANAGER";

    @Inject
    private SecurityContext securityContext;
    
    private Collection<String> realmRoles;
    
    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct
    public void init() {
        realmRoles = new ArrayList<>();
        
        realmRoles.add(realmRolesPrefix + ROLES_MANAGER_ROLE_NAME);
        realmRoles.add(realmRolesPrefix + GROUPS_MANAGER_ROLE_NAME);
        realmRoles.add(realmRolesPrefix + PRIVILEGES_MANAGER_ROLE_NAME);
        
        LOG.info("Roles service init finished, realm roles count is {}", realmRoles.size());
    }
    
    @PermitAll
    public Collection<UserRoleDTO> getAllCurrentUserRoles() {
        if (securityContext != null) {
            Collection<UserRoleDTO> result = new ArrayList<>();
            
            boolean isSupervisor = securityContext.isCallerInRole(SecuritySupervisor.SUPERVISOR_ROLE_NAME); 
            
            for (String realmRole : realmRoles) {
                if (isSupervisor || securityContext.isCallerInRole(realmRole)) {
                    UserRoleDTO userRole = new UserRoleDTO();
                    userRole.setName(realmRole);
                    userRole.setCustom(true);
                    
                    result.add(userRole);
                }
            }
            
            Principal principal = securityContext.getCallerPrincipal();
            if (principal != null) {
                String userId = principal.getName();
                
                Collection<String> foundRoles = fetchUserRoles(userId);
                for (String foundRole : foundRoles) {
                    UserRoleDTO userRole = new UserRoleDTO();
                    userRole.setName(foundRole);
                    userRole.setCustom(false);
                    
                    result.add(userRole);
                }
                
            }
            
            return result;
        }
        
        return new ArrayList<>();
    }

    private List<String> fetchUserRoles(String userId) {
        TypedQuery<String> query = em.createNamedQuery("GET_CUSTOM_ROLE_BINDINGS_BY_USER", String.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
