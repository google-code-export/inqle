// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain.security;

import org.inqle.domain.security.Authority;
import org.inqle.domain.security.AuthorityPrincipalAssignment;
import org.inqle.domain.security.Principal;

privileged aspect AuthorityPrincipalAssignment_Roo_JavaBean {
    
    public Principal AuthorityPrincipalAssignment.getUsername() {
        return this.username;
    }
    
    public void AuthorityPrincipalAssignment.setUsername(Principal username) {
        this.username = username;
    }
    
    public Authority AuthorityPrincipalAssignment.getRoleId() {
        return this.roleId;
    }
    
    public void AuthorityPrincipalAssignment.setRoleId(Authority roleId) {
        this.roleId = roleId;
    }
    
}