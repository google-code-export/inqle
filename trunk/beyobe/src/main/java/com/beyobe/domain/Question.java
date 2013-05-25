package com.beyobe.domain;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.beyobe.client.beans.DataType;
import com.beyobe.client.beans.Measurement;

import flexjson.JSONSerializer;

@RooJson
@RooJavaBean
@RooToString
@RooJpaEntity
public class Question {

	public static final int DATA_TYPE_UNSPECIFIED = 0;
	public static final int DATA_TYPE_DOUBLE = 1;
	public static final int DATA_TYPE_INTEGER = 2;
	public static final int DATA_TYPE_MULTIPLE_CHOICE = 3;
	public static final int DATA_TYPE_SHORT_TEXT = 4;
	public static final int DATA_TYPE_LONG_TEXT = 5;
	public static final int DATA_TYPE_STARS = 6;

	@javax.persistence.Id
    @GenericGenerator(name = "HibernateUuidGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "HibernateUuidGenerator")
    private String id;
	
    @NotNull
    private String abbreviation;

    @NotNull
    private String longForm;
    
    private String lang;
    
    @ManyToOne
    private QuestionConcept questionConcept;
    
    @Enumerated(EnumType.STRING)
    private DataType dataType;
    
    @Enumerated(EnumType.STRING)
    public Measurement measurement;
    
    private Double minValue;
    
    private Double maxValue;
    
    @Min(1)
    @Max(4000)
    private Integer maxLength;
    
    @Value("0")
    private Long latency;
    
    @Value("100")
    private Integer priority;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date created = new Date();

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "FF")
    private Date updated = new Date();

    private Long updatedBy;

    private Long createdBy;
    
    public String toJsonForClient() {
	   return new JSONSerializer()
	   	.exclude("*.class")
	   	.exclude("created")
	   	.exclude("updated")
	   	.exclude("createdBy")
	   	.exclude("updatedBy")
	   	.serialize(this);
	}
    
	@PrePersist
	public void onPersist() {
        this.created=new java.util.Date();
    }
	
	@PreUpdate
	public void onUpdate() {
        this.updated=new java.util.Date();
    }
}
