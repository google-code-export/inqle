// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.beyobe.domain;

import com.beyobe.domain.ChoiceTranslation;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Version;

privileged aspect ChoiceTranslation_Roo_Jpa_Entity {
    
    declare @type: ChoiceTranslation: @Entity;
    
    @Version
    @Column(name = "version")
    private Integer ChoiceTranslation.version;
    
    public Integer ChoiceTranslation.getVersion() {
        return this.version;
    }
    
    public void ChoiceTranslation.setVersion(Integer version) {
        this.version = version;
    }
    
}
