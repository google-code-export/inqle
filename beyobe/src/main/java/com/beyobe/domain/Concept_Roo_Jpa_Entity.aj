// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.beyobe.domain;

import com.beyobe.domain.Concept;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect Concept_Roo_Jpa_Entity {
    
    declare @type: Concept: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Concept.id;
    
    @Version
    @Column(name = "version")
    private Integer Concept.version;
    
    public Long Concept.getId() {
        return this.id;
    }
    
    public void Concept.setId(Long id) {
        this.id = id;
    }
    
    public Integer Concept.getVersion() {
        return this.version;
    }
    
    public void Concept.setVersion(Integer version) {
        this.version = version;
    }
    
}
