// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.beyobe.domain;

import com.beyobe.domain.Choice;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect Choice_Roo_Jpa_Entity {
    
    declare @type: Choice: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Choice.id;
    
    @Version
    @Column(name = "version")
    private Integer Choice.version;
    
    public Long Choice.getId() {
        return this.id;
    }
    
    public void Choice.setId(Long id) {
        this.id = id;
    }
    
    public Integer Choice.getVersion() {
        return this.version;
    }
    
    public void Choice.setVersion(Integer version) {
        this.version = version;
    }
    
}
