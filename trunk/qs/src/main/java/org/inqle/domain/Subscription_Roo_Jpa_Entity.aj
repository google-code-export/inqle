// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import org.inqle.domain.Subscription;

privileged aspect Subscription_Roo_Jpa_Entity {
    
    declare @type: Subscription: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Subscription.id;
    
    @Version
    @Column(name = "version")
    private Integer Subscription.version;
    
    public Long Subscription.getId() {
        return this.id;
    }
    
    public void Subscription.setId(Long id) {
        this.id = id;
    }
    
    public Integer Subscription.getVersion() {
        return this.version;
    }
    
    public void Subscription.setVersion(Integer version) {
        this.version = version;
    }
    
}
