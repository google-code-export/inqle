package org.inqle.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJson
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Datum {

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

    /**
     * typically a value of 0 to 1
     */
    private Double normalizedValue;

    @ManyToOne
    private Unit unit;

    @ManyToOne
    private Unit canonicalUnit;

    private Double canonicalValue;
}
