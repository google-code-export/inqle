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
import org.inqle.domain.Account;
import org.inqle.domain.Concept;
import org.inqle.domain.ConceptDataOnDemand;
import org.springframework.stereotype.Component;

privileged aspect ConceptDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ConceptDataOnDemand: @Component;
    
    private Random ConceptDataOnDemand.rnd = new SecureRandom();
    
    private List<Concept> ConceptDataOnDemand.data;
    
    public Concept ConceptDataOnDemand.getNewTransientConcept(int index) {
        Concept obj = new Concept();
        setConceptkey(obj, index);
        setCreated(obj, index);
        setCreatedBy(obj, index);
        setUpdated(obj, index);
        setUpdatedBy(obj, index);
        return obj;
    }
    
    public void ConceptDataOnDemand.setConceptkey(Concept obj, int index) {
        String conceptkey = "conceptkey_" + index;
        obj.setConceptkey(conceptkey);
    }
    
    public void ConceptDataOnDemand.setCreated(Concept obj, int index) {
        Date created = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreated(created);
    }
    
    public void ConceptDataOnDemand.setCreatedBy(Concept obj, int index) {
        Account createdBy = null;
        obj.setCreatedBy(createdBy);
    }
    
    public void ConceptDataOnDemand.setUpdated(Concept obj, int index) {
        Date updated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setUpdated(updated);
    }
    
    public void ConceptDataOnDemand.setUpdatedBy(Concept obj, int index) {
        Account updatedBy = null;
        obj.setUpdatedBy(updatedBy);
    }
    
    public Concept ConceptDataOnDemand.getSpecificConcept(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Concept obj = data.get(index);
        Long id = obj.getId();
        return Concept.findConcept(id);
    }
    
    public Concept ConceptDataOnDemand.getRandomConcept() {
        init();
        Concept obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Concept.findConcept(id);
    }
    
    public boolean ConceptDataOnDemand.modifyConcept(Concept obj) {
        return false;
    }
    
    public void ConceptDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Concept.findConceptEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Concept' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Concept>();
        for (int i = 0; i < 10; i++) {
            Concept obj = getNewTransientConcept(i);
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
