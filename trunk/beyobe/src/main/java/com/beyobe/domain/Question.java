package com.beyobe.domain;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.beyobe.client.beans.Measurement;

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
    
    @Value("0")
    private Long latency;
    
    @NotNull
    @Value("100")
    private Integer priority;
    
    private String conceptUid;
    
    @Enumerated(EnumType.STRING)
    public Measurement measurement;
    
    private Double minValue;
    
    private Double maxValue;
    
    private Integer maxLength;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date created = new Date();

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "FF")
    private Date updated = new Date();

    private Long updatedBy;

    private Long createdBy;
}
