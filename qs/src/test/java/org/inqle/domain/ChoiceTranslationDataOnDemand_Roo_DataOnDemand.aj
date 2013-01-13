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
import org.inqle.domain.Choice;
import org.inqle.domain.ChoiceDataOnDemand;
import org.inqle.domain.ChoiceTranslation;
import org.inqle.domain.ChoiceTranslationDataOnDemand;
import org.inqle.domain.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect ChoiceTranslationDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ChoiceTranslationDataOnDemand: @Component;
    
    private Random ChoiceTranslationDataOnDemand.rnd = new SecureRandom();
    
    private List<ChoiceTranslation> ChoiceTranslationDataOnDemand.data;
    
    @Autowired
    ChoiceDataOnDemand ChoiceTranslationDataOnDemand.choiceDataOnDemand;
    
    public ChoiceTranslation ChoiceTranslationDataOnDemand.getNewTransientChoiceTranslation(int index) {
        ChoiceTranslation obj = new ChoiceTranslation();
        setChoice(obj, index);
        setCreated(obj, index);
        setCreatedBy(obj, index);
        setLang(obj, index);
        setUpdated(obj, index);
        setUpdatedBy(obj, index);
        return obj;
    }
    
    public void ChoiceTranslationDataOnDemand.setChoice(ChoiceTranslation obj, int index) {
        Choice choice = choiceDataOnDemand.getRandomChoice();
        obj.setChoice(choice);
    }
    
    public void ChoiceTranslationDataOnDemand.setCreated(ChoiceTranslation obj, int index) {
        Date created = new Date(new Date().getTime() + 10000000L);
        obj.setCreated(created);
    }
    
    public void ChoiceTranslationDataOnDemand.setCreatedBy(ChoiceTranslation obj, int index) {
        Principal createdBy = null;
        obj.setCreatedBy(createdBy);
    }
    
    public void ChoiceTranslationDataOnDemand.setLang(ChoiceTranslation obj, int index) {
        String lang = "lang_" + index;
        obj.setLang(lang);
    }
    
    public void ChoiceTranslationDataOnDemand.setUpdated(ChoiceTranslation obj, int index) {
        Date updated = new Date(new Date().getTime() + 10000000L);
        obj.setUpdated(updated);
    }
    
    public void ChoiceTranslationDataOnDemand.setUpdatedBy(ChoiceTranslation obj, int index) {
        Principal updatedBy = null;
        obj.setUpdatedBy(updatedBy);
    }
    
    public ChoiceTranslation ChoiceTranslationDataOnDemand.getSpecificChoiceTranslation(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ChoiceTranslation obj = data.get(index);
        Long id = obj.getId();
        return ChoiceTranslation.findChoiceTranslation(id);
    }
    
    public ChoiceTranslation ChoiceTranslationDataOnDemand.getRandomChoiceTranslation() {
        init();
        ChoiceTranslation obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ChoiceTranslation.findChoiceTranslation(id);
    }
    
    public boolean ChoiceTranslationDataOnDemand.modifyChoiceTranslation(ChoiceTranslation obj) {
        return false;
    }
    
    public void ChoiceTranslationDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ChoiceTranslation.findChoiceTranslationEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ChoiceTranslation' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ChoiceTranslation>();
        for (int i = 0; i < 10; i++) {
            ChoiceTranslation obj = getNewTransientChoiceTranslation(i);
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
