// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.beyobe.domain;

import com.beyobe.domain.Question;
import java.util.Date;

privileged aspect Question_Roo_JavaBean {
    
    public String Question.getId() {
        return this.id;
    }
    
    public void Question.setId(String id) {
        this.id = id;
    }
    
    public String Question.getAbbreviation() {
        return this.abbreviation;
    }
    
    public void Question.setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
    
    public String Question.getLongForm() {
        return this.longForm;
    }
    
    public void Question.setLongForm(String longForm) {
        this.longForm = longForm;
    }
    
    public Long Question.getLatency() {
        return this.latency;
    }
    
    public void Question.setLatency(Long latency) {
        this.latency = latency;
    }
    
    public Date Question.getCreated() {
        return this.created;
    }
    
    public void Question.setCreated(Date created) {
        this.created = created;
    }
    
    public Date Question.getUpdated() {
        return this.updated;
    }
    
    public void Question.setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public String Question.getConceptUid() {
        return this.conceptUid;
    }
    
    public void Question.setConceptUid(String conceptUid) {
        this.conceptUid = conceptUid;
    }
    
    public Long Question.getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void Question.setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public Long Question.getCreatedBy() {
        return this.createdBy;
    }
    
    public void Question.setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    
    public Integer Question.getPriority() {
        return this.priority;
    }
    
    public void Question.setPriority(Integer priority) {
        this.priority = priority;
    }
    
    public String Question.getLang() {
        return this.lang;
    }
    
    public void Question.setLang(String lang) {
        this.lang = lang;
    }
    
    public Double Question.getMinValue() {
        return this.minValue;
    }
    
    public void Question.setMinValue(Double minValue) {
        this.minValue = minValue;
    }
    
    public Double Question.getMaxValue() {
        return this.maxValue;
    }
    
    public void Question.setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }
    
    public Integer Question.getMaxLength() {
        return this.maxLength;
    }
    
    public void Question.setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
    
}