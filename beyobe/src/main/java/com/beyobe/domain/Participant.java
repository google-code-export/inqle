package com.beyobe.domain;

import com.beyobe.client.beans.UserRole;
import flexjson.JSONSerializer;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
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
@RooJpaActiveRecord(finders = { "findParticipantsByUsernameEqualsAndPasswordEquals", "findParticipantsBySessionTokenEqualsAndClientIpAddressEquals" })
public class Participant {

    @Autowired
    @Transient
    private transient MessageDigestPasswordEncoder passwordEncoder;

    @Id
    @GenericGenerator(name = "HibernateUuidGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "HibernateUuidGenerator")
    private String id;

    @Column(unique = true)
    @Size(min = 1, max = 255)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Boolean enabled;

    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date created = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date updated = null;

    private String updatedBy;

    private String createdBy;

    private String sessionToken;
    
    private String clientIpAddress;

    public void setPassword(String password) {
        if (password == null || password.equals("")) return;
        String encodedPassword = passwordEncoder.encodePassword(password, username);
        this.password = encodedPassword;
    }

    public String toJsonForClient() {
        return new JSONSerializer().exclude("*.class").exclude("created").exclude("updated").exclude("createdBy").exclude("updatedBy").exclude("password").exclude("sessionKey").serialize(this);
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
        System.out.println("Password hashes to: " + e.encodePassword("admin", "admin"));
    }
}
