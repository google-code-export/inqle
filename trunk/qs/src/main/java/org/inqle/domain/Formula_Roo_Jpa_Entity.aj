// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import org.inqle.domain.Formula;

privileged aspect Formula_Roo_Jpa_Entity {
    
    declare @type: Formula: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Formula.id;
    
    @Version
    @Column(name = "version")
    private Integer Formula.version;
    
    public Long Formula.getId() {
        return this.id;
    }
    
    public void Formula.setId(Long id) {
        this.id = id;
    }
    
    public Integer Formula.getVersion() {
        return this.version;
    }
    
    public void Formula.setVersion(Integer version) {
        this.version = version;
    }
    
}