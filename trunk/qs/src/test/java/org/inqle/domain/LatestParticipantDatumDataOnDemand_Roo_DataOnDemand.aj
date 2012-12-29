// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.inqle.domain.Concept;
import org.inqle.domain.ConceptDataOnDemand;
import org.inqle.domain.DatumDataOnDemand;
import org.inqle.domain.LatestParticipantDatum;
import org.inqle.domain.LatestParticipantDatumDataOnDemand;
import org.inqle.domain.Participant;
import org.inqle.domain.ParticipantDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect LatestParticipantDatumDataOnDemand_Roo_DataOnDemand {
    
    declare @type: LatestParticipantDatumDataOnDemand: @Component;
    
    private Random LatestParticipantDatumDataOnDemand.rnd = new SecureRandom();
    
    private List<LatestParticipantDatum> LatestParticipantDatumDataOnDemand.data;
    
    @Autowired
    ConceptDataOnDemand LatestParticipantDatumDataOnDemand.conceptDataOnDemand;
    
    @Autowired
    DatumDataOnDemand LatestParticipantDatumDataOnDemand.datumDataOnDemand;
    
    @Autowired
    ParticipantDataOnDemand LatestParticipantDatumDataOnDemand.participantDataOnDemand;
    
    public LatestParticipantDatum LatestParticipantDatumDataOnDemand.getNewTransientLatestParticipantDatum(int index) {
        LatestParticipantDatum obj = new LatestParticipantDatum();
        setAskableAfter(obj, index);
        setConcept(obj, index);
        setCreated(obj, index);
        setParticipant(obj, index);
        return obj;
    }
    
    public void LatestParticipantDatumDataOnDemand.setAskableAfter(LatestParticipantDatum obj, int index) {
        Date askableAfter = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setAskableAfter(askableAfter);
    }
    
    public void LatestParticipantDatumDataOnDemand.setConcept(LatestParticipantDatum obj, int index) {
        Concept concept = conceptDataOnDemand.getRandomConcept();
        obj.setConcept(concept);
    }
    
    public void LatestParticipantDatumDataOnDemand.setCreated(LatestParticipantDatum obj, int index) {
        Date created = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreated(created);
    }
    
    public void LatestParticipantDatumDataOnDemand.setParticipant(LatestParticipantDatum obj, int index) {
        Participant participant = participantDataOnDemand.getRandomParticipant();
        obj.setParticipant(participant);
    }
    
    public LatestParticipantDatum LatestParticipantDatumDataOnDemand.getSpecificLatestParticipantDatum(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        LatestParticipantDatum obj = data.get(index);
        Long id = obj.getId();
        return LatestParticipantDatum.findLatestParticipantDatum(id);
    }
    
    public LatestParticipantDatum LatestParticipantDatumDataOnDemand.getRandomLatestParticipantDatum() {
        init();
        LatestParticipantDatum obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return LatestParticipantDatum.findLatestParticipantDatum(id);
    }
    
    public boolean LatestParticipantDatumDataOnDemand.modifyLatestParticipantDatum(LatestParticipantDatum obj) {
        return false;
    }
    
    public void LatestParticipantDatumDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = LatestParticipantDatum.findLatestParticipantDatumEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'LatestParticipantDatum' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<LatestParticipantDatum>();
        for (int i = 0; i < 10; i++) {
            LatestParticipantDatum obj = getNewTransientLatestParticipantDatum(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
