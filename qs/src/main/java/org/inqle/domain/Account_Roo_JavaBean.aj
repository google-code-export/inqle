// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain;

import java.util.Date;
import java.util.Set;
import org.inqle.domain.Account;
import org.inqle.domain.UserRole;

privileged aspect Account_Roo_JavaBean {
    
    public Date Account.getCreated() {
        return this.created;
    }
    
    public void Account.setCreated(Date created) {
        this.created = created;
    }
    
    public Date Account.getUpdated() {
        return this.updated;
    }
    
    public void Account.setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public String Account.getUsername() {
        return this.username;
    }
    
    public void Account.setUsername(String username) {
        this.username = username;
    }
    
    public String Account.getPassword() {
        return this.password;
    }
    
    public void Account.setPassword(String password) {
        this.password = password;
    }
    
    public Set<UserRole> Account.getRoles() {
        return this.roles;
    }
    
    public void Account.setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }
    
}