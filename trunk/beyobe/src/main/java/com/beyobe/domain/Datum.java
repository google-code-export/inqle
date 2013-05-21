package com.beyobe.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
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

import com.beyobe.client.beans.Unit;

@RooJson
@RooJavaBean
@RooToString
@RooJpaEntity
public class Datum {

	public static final Integer STATUS_ANSWERED_PERSONALLY = 2;
	public static final Integer STATUS_INFERRED = 1;
	public static final Integer STATUS_ASKED_BUT_NO_ANSWER = -1;
	public static final Integer STATUS_DECLINED_ANSWER = -2;
	public static final Integer STATUS_NEVER_ASK_AGAIN = -3;
	
	@javax.persistence.Id
    @GenericGenerator(name = "HibernateUuidGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "HibernateUuidGenerator")
    private String id;
	
    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date created = new Date();

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date updated = null;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date effectiveDate = null;
    
    private String questionUid;

    private String conceptUid;
    
    private String formulaUid;

    private Long participantId;

    private Long updatedBy;

    private Long createdBy;

    private Double numericValue;
    
    private Integer integerValue;

    @NotNull
    private String textValue;

    @ManyToOne
    private Choice choice;

    private Double normalizedValue;

    private Unit unit;

    @NotNull
    private Integer status;

}
