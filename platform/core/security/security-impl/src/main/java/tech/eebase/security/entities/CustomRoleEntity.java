package tech.eebase.security.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SEC_CUSTOM_ROLE")
public class CustomRoleEntity {

    private String name;
    private boolean active;
    
    @Id
    @Column(name = "ROLE_NAME", length = 100, nullable = false)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name = "IS_ACTIVE")
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    
}
