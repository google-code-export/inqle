// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.Date;
import org.inqle.domain.Concept;
import org.inqle.domain.Datum;
import org.inqle.domain.LatestParticipantDatum;
import org.inqle.domain.Participant;

privileged aspect LatestParticipantDatum_Roo_JavaBean {
    
    public Date LatestParticipantDatum.getCreated() {
        return this.created;
    }
    
    public void LatestParticipantDatum.setCreated(Date created) {
        this.created = created;
    }
    
    public Datum LatestParticipantDatum.getDatum() {
        return this.datum;
    }
    
    public void LatestParticipantDatum.setDatum(Datum datum) {
        this.datum = datum;
    }
    
    public Concept LatestParticipantDatum.getConcept() {
        return this.concept;
    }
    
    public void LatestParticipantDatum.setConcept(Concept concept) {
        this.concept = concept;
    }
    
    public Participant LatestParticipantDatum.getParticipant() {
        return this.participant;
    }
    
    public void LatestParticipantDatum.setParticipant(Participant participant) {
        this.participant = participant;
    }
    
    public Date LatestParticipantDatum.getAskableAfter() {
        return this.askableAfter;
    }
    
    public void LatestParticipantDatum.setAskableAfter(Date askableAfter) {
        this.askableAfter = askableAfter;
    }
    
}
