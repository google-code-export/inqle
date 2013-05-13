package com.beyobe.domain;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

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

	@Id
    @GenericGenerator(name = "HibernateUuidGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "HibernateUuidGenerator")
    private String id;
	
    @NotNull
    private String abbreviation;

    @NotNull
    private String longForm;
    
    @Value("0")
    private Long latency;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date created = new Date();

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "FF")
    private Date updated = new Date();

    private String conceptUid;

    private Long updatedBy;

    private Long createdBy;

    @NotNull
    @Value("100")
    private Integer priority;
    
    private String lang;
    
    private Double minValue;
    
    private Double maxValue;
    
    private Integer maxLength;
}
