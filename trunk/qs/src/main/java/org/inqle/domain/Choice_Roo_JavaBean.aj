// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.Date;
import org.inqle.domain.Choice;
import org.inqle.domain.security.Principal;

privileged aspect Choice_Roo_JavaBean {
    
    public Date Choice.getCreated() {
        return this.created;
    }
    
    public void Choice.setCreated(Date created) {
        this.created = created;
    }
    
    public Date Choice.getUpdated() {
        return this.updated;
    }
    
    public void Choice.setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public Principal Choice.getCreatedBy() {
        return this.createdBy;
    }
    
    public void Choice.setCreatedBy(Principal createdBy) {
        this.createdBy = createdBy;
    }
    
    public Principal Choice.getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void Choice.setUpdatedBy(Principal updatedBy) {
        this.updatedBy = updatedBy;
    }
    
}