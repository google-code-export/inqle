package com.beyobe.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.beyobe.client.beans.AnswerStatus;
import com.beyobe.client.beans.DataType;
import com.beyobe.client.beans.Unit;

import flexjson.JSONSerializer;

@RooJson
@RooJavaBean
@RooToString
@RooJpaEntity
public class Datum implements HasUuid {

//	public static final Integer STATUS_ANSWERED_PERSONALLY = 2;
//	public static final Integer STATUS_INFERRED = 1;
//	public static final Integer STATUS_ASKED_BUT_NO_ANSWER = -1;
//	public static final Integer STATUS_DECLINED_ANSWER = -2;
//	public static final Integer STATUS_NEVER_ASK_AGAIN = -3;
	
//	@javax.persistence.Id
//    @GenericGenerator(name = "HibernateUuidGenerator", strategy = "uuid2")
//    @GeneratedValue(generator = "HibernateUuidGenerator")
	
//	@Id
//	@GeneratedValue(generator = "uuid")
////	@GenericGenerator(name = "hibernate-uuid", strategy = "hibernate-uuid")
//	@GenericGenerator(name = "uuid", strategy = "uuid2")
//	@Column(name = "uuid", unique = true)
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="IdOrGenerated")
	@GenericGenerator(name="IdOrGenerated",
	                  strategy="com.beyobe.db.util.UseIdOrGenerate"
	)
	private String id;
	
    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date created = new Date();

//    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date updated = null;

//    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date effectiveDate = null;
    
    @NotNull
    private String questionId;
    
    @ManyToOne
    private Question question;

//    @ManyToOne
//    private QuestionConcept questionConcept;
    
    @ManyToOne
    private Formula formula;

    @ManyToOne
    @NotNull
    private Session session;
    
    @NotNull
    private String userId;

    private String updatedBy;

    private String createdBy;

    private Double numericValue;
    
    private Integer integerValue;

    @NotNull
    private String textValue;

//    @ManyToOne
//    private ChoiceConcept choice;

    private Double normalizedValue;

    private Unit unit;

    @Enumerated(EnumType.STRING)
    private DataType dataType;
    
    @Enumerated(EnumType.STRING)
    private AnswerStatus answerStatus;
    
    public String toJson() {
 	   return new JSONSerializer()
 	   	.exclude("*.class")
 	   	.exclude("created")
 	   	.exclude("updated")
 	   	.exclude("createdBy")
 	   	.exclude("updatedBy")
 	   	.exclude("participant")
 	   	.exclude("question")
 	   	.exclude("session")
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
