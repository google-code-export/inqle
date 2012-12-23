// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.inqle.domain.Account;
import org.inqle.domain.Choice;
import org.inqle.domain.ChoiceDataOnDemand;
import org.springframework.stereotype.Component;

privileged aspect ChoiceDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ChoiceDataOnDemand: @Component;
    
    private Random ChoiceDataOnDemand.rnd = new SecureRandom();
    
    private List<Choice> ChoiceDataOnDemand.data;
    
    public Choice ChoiceDataOnDemand.getNewTransientChoice(int index) {
        Choice obj = new Choice();
        setCreated(obj, index);
        setCreatedBy(obj, index);
        setUpdated(obj, index);
        setUpdatedBy(obj, index);
        return obj;
    }
    
    public void ChoiceDataOnDemand.setCreated(Choice obj, int index) {
        Date created = new Date(new Date().getTime() + 10000000L);
        obj.setCreated(created);
    }
    
    public void ChoiceDataOnDemand.setCreatedBy(Choice obj, int index) {
        Account createdBy = null;
        obj.setCreatedBy(createdBy);
    }
    
    public void ChoiceDataOnDemand.setUpdated(Choice obj, int index) {
        Date updated = new Date(new Date().getTime() + 10000000L);
        obj.setUpdated(updated);
    }
    
    public void ChoiceDataOnDemand.setUpdatedBy(Choice obj, int index) {
        Account updatedBy = null;
        obj.setUpdatedBy(updatedBy);
    }
    
    public Choice ChoiceDataOnDemand.getSpecificChoice(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Choice obj = data.get(index);
        Long id = obj.getId();
        return Choice.findChoice(id);
    }
    
    public Choice ChoiceDataOnDemand.getRandomChoice() {
        init();
        Choice obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Choice.findChoice(id);
    }
    
    public boolean ChoiceDataOnDemand.modifyChoice(Choice obj) {
        return false;
    }
    
    public void ChoiceDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Choice.findChoiceEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Choice' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Choice>();
        for (int i = 0; i < 10; i++) {
            Choice obj = getNewTransientChoice(i);
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
