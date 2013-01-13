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
import org.inqle.domain.Participant;
import org.inqle.domain.ParticipantDataOnDemand;
import org.inqle.domain.Question;
import org.inqle.domain.QuestionDataOnDemand;
import org.inqle.domain.Subscription;
import org.inqle.domain.SubscriptionDataOnDemand;
import org.inqle.domain.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect SubscriptionDataOnDemand_Roo_DataOnDemand {
    
    declare @type: SubscriptionDataOnDemand: @Component;
    
    private Random SubscriptionDataOnDemand.rnd = new SecureRandom();
    
    private List<Subscription> SubscriptionDataOnDemand.data;
    
    @Autowired
    ParticipantDataOnDemand SubscriptionDataOnDemand.participantDataOnDemand;
    
    @Autowired
    QuestionDataOnDemand SubscriptionDataOnDemand.questionDataOnDemand;
    
    public Subscription SubscriptionDataOnDemand.getNewTransientSubscription(int index) {
        Subscription obj = new Subscription();
        setCreated(obj, index);
        setCreatedBy(obj, index);
        setParticipant(obj, index);
        setQuestion(obj, index);
        setRank(obj, index);
        return obj;
    }
    
    public void SubscriptionDataOnDemand.setCreated(Subscription obj, int index) {
        Date created = new Date(new Date().getTime() + 10000000L);
        obj.setCreated(created);
    }
    
    public void SubscriptionDataOnDemand.setCreatedBy(Subscription obj, int index) {
        Principal createdBy = null;
        obj.setCreatedBy(createdBy);
    }
    
    public void SubscriptionDataOnDemand.setParticipant(Subscription obj, int index) {
        Participant participant = participantDataOnDemand.getSpecificParticipant(index);
        obj.setParticipant(participant);
    }
    
    public void SubscriptionDataOnDemand.setQuestion(Subscription obj, int index) {
        Question question = questionDataOnDemand.getSpecificQuestion(index);
        obj.setQuestion(question);
    }
    
    public void SubscriptionDataOnDemand.setRank(Subscription obj, int index) {
        int rank = index;
        obj.setRank(rank);
    }
    
    public Subscription SubscriptionDataOnDemand.getSpecificSubscription(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Subscription obj = data.get(index);
        Long id = obj.getId();
        return Subscription.findSubscription(id);
    }
    
    public Subscription SubscriptionDataOnDemand.getRandomSubscription() {
        init();
        Subscription obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Subscription.findSubscription(id);
    }
    
    public boolean SubscriptionDataOnDemand.modifySubscription(Subscription obj) {
        return false;
    }
    
    public void SubscriptionDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Subscription.findSubscriptionEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Subscription' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Subscription>();
        for (int i = 0; i < 10; i++) {
            Subscription obj = getNewTransientSubscription(i);
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
