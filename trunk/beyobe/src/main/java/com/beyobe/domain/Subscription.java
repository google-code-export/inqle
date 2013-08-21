package com.beyobe.domain;

import com.beyobe.client.beans.SubscriptionType;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { 
		"findSubscriptionsByQuestionIdEqualsAndUserIdEquals",
		"findSubscriptionsByQuestionIdEqualsAndSessionEquals"})
public class Subscription {

    @Id
    @GenericGenerator(name = "HibernateUuidGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "HibernateUuidGenerator")
    private String id;

    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date created = new Date();

    private String createdBy;

    @NotNull
    private String questionId;

    @NotNull
    @ManyToOne
    private Session session;
    
    @NotNull
    private String userId;

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    @PrePersist
    public void onPersist() {
        this.created = new java.util.Date();
    }
}
