// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.Date;
import org.inqle.domain.Choice;
import org.inqle.domain.ChoiceTranslation;
import org.inqle.domain.security.Principal;

privileged aspect ChoiceTranslation_Roo_JavaBean {
    
    public Choice ChoiceTranslation.getChoice() {
        return this.choice;
    }
    
    public void ChoiceTranslation.setChoice(Choice choice) {
        this.choice = choice;
    }
    
    public Date ChoiceTranslation.getCreated() {
        return this.created;
    }
    
    public void ChoiceTranslation.setCreated(Date created) {
        this.created = created;
    }
    
    public Date ChoiceTranslation.getUpdated() {
        return this.updated;
    }
    
    public void ChoiceTranslation.setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public String ChoiceTranslation.getLang() {
        return this.lang;
    }
    
    public void ChoiceTranslation.setLang(String lang) {
        this.lang = lang;
    }
    
    public Principal ChoiceTranslation.getCreatedBy() {
        return this.createdBy;
    }
    
    public void ChoiceTranslation.setCreatedBy(Principal createdBy) {
        this.createdBy = createdBy;
    }
    
    public Principal ChoiceTranslation.getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void ChoiceTranslation.setUpdatedBy(Principal updatedBy) {
        this.updatedBy = updatedBy;
    }
    
}
