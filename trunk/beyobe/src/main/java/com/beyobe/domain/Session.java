package com.beyobe.domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { 
		"findSessionsByIdEqualsAndSessionDateGreaterThanAndClientIpAddressEquals", 
		})
public class Session {
	@Id
    private String id;
	
	private String app;
	private Double appVersion;
	private String client;
	private Double clientVersion;
	
	private String username;
	
	private Boolean expired = false;
    
    
    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "SS")
    private Date created = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "SS")
    private Date updated = null;
    
    @NotNull
    @Column(updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "SS")
    private Date sessionDate = new Date();
    
    @NotNull
    @Column(updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "SS")
    private Date loggedOut = null;

    private Integer requestCount = 0;
    
    private Integer questionsCreated = 0;
    
    private Integer questionsUpdated = 0;
    
    private Integer questionsSubscribed = 0;
    
    private Integer questionsUnsubscribed = 0;
    
    private Integer dataCreated = 0;
    
    private Integer dataUpdated = 0;
    
    private String userId;

    private String sessionToken;

    private String clientIpAddress;
    
    private String timezone;
    
    @Transient
    private Collection<String> roles;
    
    private String lang;
    
    private Integer status = 1;

}
