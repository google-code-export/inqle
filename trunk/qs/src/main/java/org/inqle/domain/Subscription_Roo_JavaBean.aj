// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.Date;
import org.inqle.domain.Participant;
import org.inqle.domain.Question;
import org.inqle.domain.Subscription;
import org.inqle.domain.security.Principal;

privileged aspect Subscription_Roo_JavaBean {
    
    public Date Subscription.getCreated() {
        return this.created;
    }
    
    public void Subscription.setCreated(Date created) {
        this.created = created;
    }
    
    public Principal Subscription.getCreatedBy() {
        return this.createdBy;
    }
    
    public void Subscription.setCreatedBy(Principal createdBy) {
        this.createdBy = createdBy;
    }
    
    public Question Subscription.getQuestion() {
        return this.question;
    }
    
    public void Subscription.setQuestion(Question question) {
        this.question = question;
    }
    
    public Participant Subscription.getParticipant() {
        return this.participant;
    }
    
    public void Subscription.setParticipant(Participant participant) {
        this.participant = participant;
    }
    
    public int Subscription.getRank() {
        return this.rank;
    }
    
    public void Subscription.setRank(int rank) {
        this.rank = rank;
    }
    
}