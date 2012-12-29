package org.inqle.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Reference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Subscription {

    @NotNull
    @Column(updatable = false)
    @Future
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date created = new Date();

    @ManyToOne
    private Account createdBy;
    
    @NotNull
    @OneToOne
//    @JoinColumn(name="question_id")
    private Question question;

    @NotNull
    @OneToOne
//    @JoinColumn(name="participant_id")
    private Participant participant;

    @NotNull
    @Value("0")
    private int rank;
}
