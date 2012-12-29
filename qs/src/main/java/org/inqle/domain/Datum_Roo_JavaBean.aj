// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.Date;
import org.inqle.domain.Account;
import org.inqle.domain.Choice;
import org.inqle.domain.Concept;
import org.inqle.domain.Datum;
import org.inqle.domain.Formula;
import org.inqle.domain.Participant;
import org.inqle.domain.Question;
import org.inqle.domain.Unit;

privileged aspect Datum_Roo_JavaBean {
    
    public Date Datum.getCreated() {
        return this.created;
    }
    
    public void Datum.setCreated(Date created) {
        this.created = created;
    }
    
    public Date Datum.getUpdated() {
        return this.updated;
    }
    
    public void Datum.setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public Question Datum.getQuestion() {
        return this.question;
    }
    
    public void Datum.setQuestion(Question question) {
        this.question = question;
    }
    
    public Formula Datum.getFormula() {
        return this.formula;
    }
    
    public void Datum.setFormula(Formula formula) {
        this.formula = formula;
    }
    
    public Participant Datum.getParticipant() {
        return this.participant;
    }
    
    public void Datum.setParticipant(Participant participant) {
        this.participant = participant;
    }
    
    public Account Datum.getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void Datum.setUpdatedBy(Account updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public Account Datum.getCreatedBy() {
        return this.createdBy;
    }
    
    public void Datum.setCreatedBy(Account createdBy) {
        this.createdBy = createdBy;
    }
    
    public Double Datum.getNumericValue() {
        return this.numericValue;
    }
    
    public void Datum.setNumericValue(Double numericValue) {
        this.numericValue = numericValue;
    }
    
    public String Datum.getTextValue() {
        return this.textValue;
    }
    
    public void Datum.setTextValue(String textValue) {
        this.textValue = textValue;
    }
    
    public Choice Datum.getChoice() {
        return this.choice;
    }
    
    public void Datum.setChoice(Choice choice) {
        this.choice = choice;
    }
    
    public Double Datum.getNormalizedValue() {
        return this.normalizedValue;
    }
    
    public void Datum.setNormalizedValue(Double normalizedValue) {
        this.normalizedValue = normalizedValue;
    }
    
    public Unit Datum.getUnit() {
        return this.unit;
    }
    
    public void Datum.setUnit(Unit unit) {
        this.unit = unit;
    }
    
    public Unit Datum.getCanonicalUnit() {
        return this.canonicalUnit;
    }
    
    public void Datum.setCanonicalUnit(Unit canonicalUnit) {
        this.canonicalUnit = canonicalUnit;
    }
    
    public Double Datum.getCanonicalValue() {
        return this.canonicalValue;
    }
    
    public void Datum.setCanonicalValue(Double canonicalValue) {
        this.canonicalValue = canonicalValue;
    }
    
    public Integer Datum.getStatus() {
        return this.status;
    }
    
    public void Datum.setStatus(Integer status) {
        this.status = status;
    }
    
    public Concept Datum.getConcept() {
        return this.concept;
    }
    
    public void Datum.setConcept(Concept concept) {
        this.concept = concept;
    }
    
}