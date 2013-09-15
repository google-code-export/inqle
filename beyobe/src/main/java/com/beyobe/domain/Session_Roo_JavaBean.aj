// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.beyobe.domain;

import com.beyobe.domain.Session;
import java.util.Date;
import java.util.List;

privileged aspect Session_Roo_JavaBean {
    
    public String Session.getId() {
        return this.id;
    }
    
    public void Session.setId(String id) {
        this.id = id;
    }
    
    public String Session.getServerVersion() {
        return this.serverVersion;
    }
    
    public void Session.setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }
    
    public String Session.getClient() {
        return this.client;
    }
    
    public void Session.setClient(String client) {
        this.client = client;
    }
    
    public String Session.getClientVersion() {
        return this.clientVersion;
    }
    
    public void Session.setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }
    
    public String Session.getUsername() {
        return this.username;
    }
    
    public void Session.setUsername(String username) {
        this.username = username;
    }
    
    public Boolean Session.getExpired() {
        return this.expired;
    }
    
    public void Session.setExpired(Boolean expired) {
        this.expired = expired;
    }
    
    public Date Session.getCreated() {
        return this.created;
    }
    
    public void Session.setCreated(Date created) {
        this.created = created;
    }
    
    public Date Session.getUpdated() {
        return this.updated;
    }
    
    public void Session.setUpdated(Date updated) {
        this.updated = updated;
    }
    
    public Date Session.getSessionDate() {
        return this.sessionDate;
    }
    
    public void Session.setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }
    
    public Date Session.getLoggedOut() {
        return this.loggedOut;
    }
    
    public void Session.setLoggedOut(Date loggedOut) {
        this.loggedOut = loggedOut;
    }
    
    public Integer Session.getRequestCount() {
        return this.requestCount;
    }
    
    public void Session.setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }
    
    public Integer Session.getQuestionsCreated() {
        return this.questionsCreated;
    }
    
    public void Session.setQuestionsCreated(Integer questionsCreated) {
        this.questionsCreated = questionsCreated;
    }
    
    public Integer Session.getQuestionsUpdated() {
        return this.questionsUpdated;
    }
    
    public void Session.setQuestionsUpdated(Integer questionsUpdated) {
        this.questionsUpdated = questionsUpdated;
    }
    
    public Integer Session.getQuestionsSubscribed() {
        return this.questionsSubscribed;
    }
    
    public void Session.setQuestionsSubscribed(Integer questionsSubscribed) {
        this.questionsSubscribed = questionsSubscribed;
    }
    
    public Integer Session.getQuestionsUnsubscribed() {
        return this.questionsUnsubscribed;
    }
    
    public void Session.setQuestionsUnsubscribed(Integer questionsUnsubscribed) {
        this.questionsUnsubscribed = questionsUnsubscribed;
    }
    
    public Integer Session.getDataCreated() {
        return this.dataCreated;
    }
    
    public void Session.setDataCreated(Integer dataCreated) {
        this.dataCreated = dataCreated;
    }
    
    public Integer Session.getDataUpdated() {
        return this.dataUpdated;
    }
    
    public void Session.setDataUpdated(Integer dataUpdated) {
        this.dataUpdated = dataUpdated;
    }
    
    public Integer Session.getDrupalUserId() {
        return this.drupalUserId;
    }
    
    public void Session.setDrupalUserId(Integer drupalUserId) {
        this.drupalUserId = drupalUserId;
    }
    
    public String Session.getUserUid() {
        return this.userUid;
    }
    
    public void Session.setUserUid(String userUid) {
        this.userUid = userUid;
    }
    
    public String Session.getSessionToken() {
        return this.sessionToken;
    }
    
    public void Session.setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
    
    public String Session.getClientIpAddress() {
        return this.clientIpAddress;
    }
    
    public void Session.setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }
    
    public String Session.getTimezone() {
        return this.timezone;
    }
    
    public void Session.setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    public List<String> Session.getRoles() {
        return this.roles;
    }
    
    public void Session.setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    public String Session.getLang() {
        return this.lang;
    }
    
    public void Session.setLang(String lang) {
        this.lang = lang;
    }
    
    public Integer Session.getStatus() {
        return this.status;
    }
    
    public void Session.setStatus(Integer status) {
        this.status = status;
    }
    
}