// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.Date;
import org.inqle.domain.Participant;
import org.inqle.domain.security.Principal;

privileged aspect Participant_Roo_JavaBean {
    
    public Date Participant.getCreated() {
        return this.created;
    }
    
    public void Participant.setCreated(Date created) {
        this.created = created;
    }
    
    public Date Participant.getUpdated() {
        return this.updated;
    }
    
    public void Participant.setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public Principal Participant.getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void Participant.setUpdatedBy(Principal updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public Principal Participant.getCreatedBy() {
        return this.createdBy;
    }
    
    public void Participant.setCreatedBy(Principal createdBy) {
        this.createdBy = createdBy;
    }
    
}
