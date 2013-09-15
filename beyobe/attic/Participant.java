package com.beyobe.domain;

import com.beyobe.client.beans.UserRole;
import flexjson.JSON;
import flexjson.JSONSerializer;

import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { 
		"findParticipantsByUsernameEqualsAndPasswordEqualsAndEnabledNot", 
//		"findParticipantsBySessionTokenEqualsAndClientIpAddressEquals", 
		"findParticipantsByUsernameEquals",
		"findParticipantsBySessionTokenEqualsAndSessionDateGreaterThanAndClientIpAddressEqualsAndEnabledNot", 
		})
public class Participant implements HasUuid {

    @Autowired
    @Transient
    private transient MessageDigestPasswordEncoder passwordEncoder;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "IdOrGenerated")
    @GenericGenerator(name = "IdOrGenerated", strategy = "com.beyobe.db.util.UseIdOrGenerate")
    private String id;

    @Column(unique = true)
    @Size(min = 1, max = 40)
    private String username;

    private String password;

    private String email;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Collection<String> roles;
    private Boolean enabled;
    private Integer status;

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

    private String updatedBy;

    private String createdBy;

    private String sessionToken;

    private String clientIpAddress;

    private String lang;
    
    public void setPassword(String password) {
        if (password == null || password.equals("")) return;
//        String encodedPassword = passwordEncoder.encodePassword(password, username);
        String encodedPassword = passwordEncoder.encodePassword(password, null);
        this.password = encodedPassword;
    }

    public String toJson() {
        return new JSONSerializer()
	        .exclude("*.class")
	        .exclude("created")
	        .exclude("updated")
	        .exclude("createdBy")
	        .exclude("updatedBy")
	        .exclude("password")
	        .exclude("sessionKey")
	        .serialize(this);
    }

    @PrePersist
    public void onPersist() {
        this.created = new java.util.Date();
    }

    @PreUpdate
    public void onUpdate() {
        this.updated = new java.util.Date();
    }

    public static String hashString(String string, String salt) {
        MessageDigestPasswordEncoder e = new MessageDigestPasswordEncoder("sha-256");
        return e.encodePassword(string, salt);
    }

    public static void main(String[] args) {
        MessageDigestPasswordEncoder e = new MessageDigestPasswordEncoder("sha-256");
        System.out.println("Password hashes to: " + e.encodePassword("put password here", null));
    }

	public void mergeWithExisting() {
		Participant existing = findParticipant(this.getId());
		if (username != null) existing.setUsername(username);
		if (enabled != null) existing.setEnabled(enabled);
		if (email != null) existing.setEmail(email);
		if (role != null) existing.setRole(role);
		if (sessionDate != null) existing.setSessionDate(sessionDate);
		if (sessionToken != null) existing.setSessionToken(sessionToken);
		if (updated != null) existing.setUpdated(updated);
		if (updatedBy != null) existing.setUpdatedBy(updatedBy);
		existing.flush();
	}

}