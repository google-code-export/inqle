// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.beyobe.domain;

import com.beyobe.domain.Concept;
import com.beyobe.domain.Formula;
import java.util.Date;

privileged aspect Formula_Roo_JavaBean {
    
    public String Formula.getId() {
        return this.id;
    }
    
    public void Formula.setId(String id) {
        this.id = id;
    }
    
    public Date Formula.getCreated() {
        return this.created;
    }
    
    public void Formula.setCreated(Date created) {
        this.created = created;
    }
    
    public Date Formula.getUpdated() {
        return this.updated;
    }
    
    public void Formula.setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public Concept Formula.getConcept() {
        return this.concept;
    }
    
    public void Formula.setConcept(Concept concept) {
        this.concept = concept;
    }
    
    public String Formula.getExpression() {
        return this.expression;
    }
    
    public void Formula.setExpression(String expression) {
        this.expression = expression;
    }
    
    public Long Formula.getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void Formula.setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public Long Formula.getCreatedBy() {
        return this.createdBy;
    }
    
    public void Formula.setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    
}