// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.beyobe.domain;

import com.beyobe.client.beans.UserRole;
import com.beyobe.domain.Participant;
import java.util.Collection;
import java.util.Date;

privileged aspect Participant_Roo_JavaBean {
    
    public String Participant.getId() {
        return this.id;
    }
    
    public void Participant.setId(String id) {
        this.id = id;
    }
    
    public String Participant.getUsername() {
        return this.username;
    }
    
    public void Participant.setUsername(String username) {
        this.username = username;
    }
    
    public String Participant.getPassword() {
        return this.password;
    }
    
    public String Participant.getEmail() {
        return this.email;
    }
    
    public void Participant.setEmail(String email) {
        this.email = email;
    }
    
    public UserRole Participant.getRole() {
        return this.role;
    }
    
    public void Participant.setRole(UserRole role) {
        this.role = role;
    }
    
    public Collection<String> Participant.getRoles() {
        return this.roles;
    }
    
    public void Participant.setRoles(Collection<String> roles) {
        this.roles = roles;
    }
    
    public Boolean Participant.getEnabled() {
        return this.enabled;
    }
    
    public void Participant.setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public Integer Participant.getStatus() {
        return this.status;
    }
    
    public void Participant.setStatus(Integer status) {
        this.status = status;
    }
    
    public Date Participant.getCreated() {
        return this.created;
    }
    
    public void Participant.setCreated(Date created) {
        this.created = created;
    }
    
    public Date Participant.getUpdated() {
        return this.updated;
    }
    
    public void Participant.setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public Date Participant.getSessionDate() {
        return this.sessionDate;
    }
    
    public void Participant.setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }
    
    public String Participant.getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void Participant.setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public String Participant.getCreatedBy() {
        return this.createdBy;
    }
    
    public void Participant.setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public String Participant.getSessionToken() {
        return this.sessionToken;
    }
    
    public void Participant.setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
    
    public String Participant.getClientIpAddress() {
        return this.clientIpAddress;
    }
    
    public void Participant.setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }
    
}
