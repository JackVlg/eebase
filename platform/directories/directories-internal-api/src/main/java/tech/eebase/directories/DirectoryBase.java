package tech.eebase.directories;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@MappedSuperclass
public abstract class DirectoryBase {
    
    private String key;
    private boolean system;
    private boolean removed;
    private Instant createTimestamp;
    private Instant removeTimestamp;
    private Instant updateTimestamp;
    private String createUser;
    private String removeUser;
    private String updateUser;
    
    //@DirectoryField(name="ID", position=-1, unique=true, showInCard=false, tableStyle="width:100px")
    @Id
    @Column(name="DIC_KEY", length = 64, updatable=false)
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    @Column(name="DIC_SYSTEM", columnDefinition="boolean default 'false'")
    public boolean isSystem() {
        return system;
    }
    public void setSystem(boolean system) {
        this.system = system;
    }

    //@DirectoryField(name="Удален", position=1000, required=true, showInCard=false, showInList=false)
    @Column(name="DIC_ITEM_REMOVED")
    public boolean isRemoved() {
        return removed;
    }
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    @Column(name="DIC_ITEM_CREATE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    public Instant getCreateTimestamp() {
        return createTimestamp;
    }
    public void setCreateTimestamp(Instant createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    @Column(name="DIC_ITEM_UPDATE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }
    public void setUpdateTimestamp(Instant updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @Column(name="DIC_ITEM_REMOVE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    public Instant getRemoveTimestamp() {
        return removeTimestamp;
    }
    public void setRemoveTimestamp(Instant removeTimestamp) {
        this.removeTimestamp = removeTimestamp;
    }

    @Column(name="DIC_ITEM_CREATE_USER", length=256)
    public String getCreateUser() {
        return createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Column(name="DIC_ITEM_REMOVE_USER", length=256)
    public String getRemoveUser() {
        return removeUser;
    }
    public void setRemoveUser(String removeUser) {
        this.removeUser = removeUser;
    }

    @Column(name="DIC_ITEM_UPDATE_USER", length=256)
    public String getUpdateUser() {
        return updateUser;
    }
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

}
