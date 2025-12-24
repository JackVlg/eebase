package tech.eebase.security.entities;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "SEC_USER_CUSTOM_ROLE_BINDINGS")
@NamedQueries({
    @NamedQuery(name = "GET_CUSTOM_ROLE_BINDINGS_BY_USER",  query = "SELECT ucrb.customRole.name FROM UserCustomRoleBinding ucrb WHERE ucrb.userId = :userId AND ucrb.customRole.active = true")
})
public class UserCustomRoleBinding {

    private String userId;
    private CustomRoleEntity customRole;
    
    @Id
    @Column(name = "USER_ID")
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    @Id
    @ManyToOne(cascade = {}, optional = false, fetch = FetchType.EAGER)
    public CustomRoleEntity getCustomRole() {
        return customRole;
    }
    public void setCustomRole(CustomRoleEntity customRole) {
        this.customRole = customRole;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(customRole, userId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserCustomRoleBinding other = (UserCustomRoleBinding) obj;
        return Objects.equals(customRole, other.customRole) && Objects.equals(userId, other.userId);
    }
    
}
