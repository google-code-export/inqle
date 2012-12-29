package org.inqle.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJson
@RooJavaBean
@RooToString
@RooJpaEntity
public class Datum {

    public static final Integer STATUS_ASKED_BUT_NO_ANSWER = -1;

    public static final Integer STATUS_DECLINED_ANSWER = -2;

    public static final Integer STATUS_NEVER_ASK_AGAIN = -3;

    @NotNull
    @Column(updatable = false)
    @Future
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date created = new Date();

    @NotNull
    @Future
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date updated = null;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Formula formula;

    @NotNull
    @ManyToOne
    private Participant participant;

    @ManyToOne
    private Account updatedBy;

    @ManyToOne
    private Account createdBy;

    private Double numericValue;

    @NotNull
    private String textValue;

    @ManyToOne
    private Choice choice;

    private Double normalizedValue;

    @ManyToOne
    private Unit unit;

    @ManyToOne
    private Unit canonicalUnit;

    private Double canonicalValue;

    @NotNull
    @Value("0")
    private Integer status;

    @NotNull
    @ManyToOne
    private Concept concept;
}
