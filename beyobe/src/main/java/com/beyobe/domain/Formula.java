package com.beyobe.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Formula {

	@javax.persistence.Id
    @GenericGenerator(name = "HibernateUuidGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "HibernateUuidGenerator")
    private String id;
	
    @NotNull
    @Column(updatable = false)
    @Future
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date created = new Date();

    @NotNull
    @Future
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date updated = null;

    @NotNull
    @ManyToOne
    private Concept concept;

    @NotNull
    private String expression;

    private Long updatedBy;

    private Long createdBy;

}
