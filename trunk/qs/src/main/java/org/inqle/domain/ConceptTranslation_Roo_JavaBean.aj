// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.Date;
import org.inqle.domain.Account;
import org.inqle.domain.Concept;
import org.inqle.domain.ConceptTranslation;

privileged aspect ConceptTranslation_Roo_JavaBean {
    
    public Date ConceptTranslation.getCreated() {
        return this.created;
    }
    
    public void ConceptTranslation.setCreated(Date created) {
        this.created = created;
    }
    
    public Date ConceptTranslation.getUpdated() {
        return this.updated;
    }
    
    public void ConceptTranslation.setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public String ConceptTranslation.getLang() {
        return this.lang;
    }
    
    public void ConceptTranslation.setLang(String lang) {
        this.lang = lang;
    }
    
    public Concept ConceptTranslation.getConcept() {
        return this.concept;
    }
    
    public void ConceptTranslation.setConcept(Concept concept) {
        this.concept = concept;
    }
    
    public String ConceptTranslation.getConceptName() {
        return this.conceptName;
    }
    
    public void ConceptTranslation.setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }
    
    public String ConceptTranslation.getConceptDescription() {
        return this.conceptDescription;
    }
    
    public void ConceptTranslation.setConceptDescription(String conceptDescription) {
        this.conceptDescription = conceptDescription;
    }
    
    public Account ConceptTranslation.getCreatedBy() {
        return this.createdBy;
    }
    
    public void ConceptTranslation.setCreatedBy(Account createdBy) {
        this.createdBy = createdBy;
    }
    
    public Account ConceptTranslation.getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void ConceptTranslation.setUpdatedBy(Account updatedBy) {
        this.updatedBy = updatedBy;
    }
    
}
