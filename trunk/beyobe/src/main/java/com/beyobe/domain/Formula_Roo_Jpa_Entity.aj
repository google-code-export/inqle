// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.beyobe.domain;

import com.beyobe.domain.Formula;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Version;

privileged aspect Formula_Roo_Jpa_Entity {
    
    declare @type: Formula: @Entity;
    
    @Version
    @Column(name = "version")
    private Integer Formula.version;
    
    public Integer Formula.getVersion() {
        return this.version;
    }
    
    public void Formula.setVersion(Integer version) {
        this.version = version;
    }
    
}
