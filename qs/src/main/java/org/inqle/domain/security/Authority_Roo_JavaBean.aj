// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.domain.security;

import org.inqle.domain.security.Authority;

privileged aspect Authority_Roo_JavaBean {
    
    public String Authority.getRoleId() {
        return this.roleId;
    }
    
    public void Authority.setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
    public String Authority.getAuthority() {
        return this.authority;
    }
    
    public void Authority.setAuthority(String authority) {
        this.authority = authority;
    }
    
}