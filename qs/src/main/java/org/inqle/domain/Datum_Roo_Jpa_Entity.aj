// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import org.inqle.domain.Datum;

privileged aspect Datum_Roo_Jpa_Entity {
    
    declare @type: Datum: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Datum.id;
    
    @Version
    @Column(name = "version")
    private Integer Datum.version;
    
    public Long Datum.getId() {
        return this.id;
    }
    
    public void Datum.setId(Long id) {
        this.id = id;
    }
    
    public Integer Datum.getVersion() {
        return this.version;
    }
    
    public void Datum.setVersion(Integer version) {
        this.version = version;
    }
    
}