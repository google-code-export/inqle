package org.inqle.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class LatestParticipantDatum {

    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date created = new Date();

//    @NotNull
    @OneToOne
    private Datum datum;

    @NotNull
    @ManyToOne
    private Concept concept;

    @NotNull
    @ManyToOne
    private Participant participant;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "M-")
    private Date askableAfter;
}
