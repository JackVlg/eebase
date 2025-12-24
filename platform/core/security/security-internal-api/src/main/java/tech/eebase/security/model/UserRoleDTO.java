package tech.eebase.security.model;

public class UserRoleDTO {

    private String name;
    private boolean realmRole;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isCustom() {
        return realmRole;
    }
    public void setCustom(boolean realmRole) {
        this.realmRole = realmRole;
    }
    
    @Override
    public String toString() {
        return "UserRoleDTO [name=" + name + ", realmRole=" + realmRole + "]";
    }
    
}
