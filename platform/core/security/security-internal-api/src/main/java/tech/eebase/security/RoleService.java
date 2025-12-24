package tech.eebase.security;

import java.util.Collection;

import tech.eebase.security.model.UserRoleDTO;

public interface RoleService {

    Collection<UserRoleDTO> getAllCurrentUserRoles();
}
