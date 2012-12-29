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
import org.inqle.domain.Survey;
import org.inqle.domain.SurveyDataOnDemand;
import org.springframework.stereotype.Component;

privileged aspect SurveyDataOnDemand_Roo_DataOnDemand {
    
    declare @type: SurveyDataOnDemand: @Component;
    
    private Random SurveyDataOnDemand.rnd = new SecureRandom();
    
    private List<Survey> SurveyDataOnDemand.data;
    
    public Survey SurveyDataOnDemand.getNewTransientSurvey(int index) {
        Survey obj = new Survey();
        setCreated(obj, index);
        setCreatedBy(obj, index);
        return obj;
    }
    
    public void SurveyDataOnDemand.setCreated(Survey obj, int index) {
        Date created = new Date(new Date().getTime() + 10000000L);
        obj.setCreated(created);
    }
    
    public void SurveyDataOnDemand.setCreatedBy(Survey obj, int index) {
        Account createdBy = null;
        obj.setCreatedBy(createdBy);
    }
    
    public Survey SurveyDataOnDemand.getSpecificSurvey(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Survey obj = data.get(index);
        Long id = obj.getId();
        return Survey.findSurvey(id);
    }
    
    public Survey SurveyDataOnDemand.getRandomSurvey() {
        init();
        Survey obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Survey.findSurvey(id);
    }
    
    public boolean SurveyDataOnDemand.modifySurvey(Survey obj) {
        return false;
    }
    
    public void SurveyDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Survey.findSurveyEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Survey' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Survey>();
        for (int i = 0; i < 10; i++) {
            Survey obj = getNewTransientSurvey(i);
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
