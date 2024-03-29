package com.beyobe.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class ChoiceConcept {

	@javax.persistence.Id
    @GenericGenerator(name = "HibernateUuidGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "HibernateUuidGenerator")
    private String id;
    
	@Column(unique = true)
	private String key;
	
	@NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date created = new Date();
	
	@NotNull
    @Future
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date updated = null;
	
    private Long updatedBy;

    private Long createdBy;
    
	@PrePersist
	public void onPersist() {
        this.created=new java.util.Date();
    }
	
	@PreUpdate
	public void onUpdate() {
        this.updated=new java.util.Date();
    }
	
	 public String toJsonForClient() {
	 	   return new JSONSerializer()
	 	   	.exclude("*.class")
	 	   	.exclude("created")
	 	   	.exclude("updated")
	 	   	.exclude("createdBy")
	 	   	.exclude("updatedBy")
	 	   	.serialize(this);
	 	}
}
