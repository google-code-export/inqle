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
import org.inqle.domain.UserRole;
import org.inqle.domain.UserRoleDataOnDemand;
import org.springframework.stereotype.Component;

privileged aspect UserRoleDataOnDemand_Roo_DataOnDemand {
    
    declare @type: UserRoleDataOnDemand: @Component;
    
    private Random UserRoleDataOnDemand.rnd = new SecureRandom();
    
    private List<UserRole> UserRoleDataOnDemand.data;
    
    public UserRole UserRoleDataOnDemand.getNewTransientUserRole(int index) {
        UserRole obj = new UserRole();
        setCreated(obj, index);
        setUpdated(obj, index);
        return obj;
    }
    
    public void UserRoleDataOnDemand.setCreated(UserRole obj, int index) {
        Date created = new Date(new Date().getTime() + 10000000L);
        obj.setCreated(created);
    }
    
    public void UserRoleDataOnDemand.setUpdated(UserRole obj, int index) {
        Date updated = new Date(new Date().getTime() + 10000000L);
        obj.setUpdated(updated);
    }
    
    public UserRole UserRoleDataOnDemand.getSpecificUserRole(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        UserRole obj = data.get(index);
        Long id = obj.getId();
        return UserRole.findUserRole(id);
    }
    
    public UserRole UserRoleDataOnDemand.getRandomUserRole() {
        init();
        UserRole obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return UserRole.findUserRole(id);
    }
    
    public boolean UserRoleDataOnDemand.modifyUserRole(UserRole obj) {
        return false;
    }
    
    public void UserRoleDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = UserRole.findUserRoleEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'UserRole' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<UserRole>();
        for (int i = 0; i < 10; i++) {
            UserRole obj = getNewTransientUserRole(i);
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
