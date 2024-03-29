package com.beyobe.domain;

import java.util.Date;

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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.beyobe.client.beans.DataType;
import com.beyobe.client.beans.Measurement;
import com.beyobe.client.beans.PrivacyType;

import flexjson.JSONSerializer;

@RooJson
@RooJavaBean
@RooToString
@RooJpaEntity
public class Question implements HasUuid {

	public static final int DATA_TYPE_UNSPECIFIED = 0;
	public static final int DATA_TYPE_DOUBLE = 1;
	public static final int DATA_TYPE_INTEGER = 2;
	public static final int DATA_TYPE_MULTIPLE_CHOICE = 3;
	public static final int DATA_TYPE_SHORT_TEXT = 4;
	public static final int DATA_TYPE_LONG_TEXT = 5;
	public static final int DATA_TYPE_STARS = 6;

//	@javax.persistence.Id
//    @GenericGenerator(name = "HibernateUuidGenerator", strategy = "uuid2")
//    @GeneratedValue(generator = "HibernateUuidGenerator")
//	@XmlAttribute
//	@Id
//	@Basic(optional = false)
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="IdOrGenerated")
	@GenericGenerator(name="IdOrGenerated",
	                  strategy="com.beyobe.db.util.UseIdOrGenerate"
	)
    private String id;
	
    @NotNull
    private String abbreviation;

    @NotNull
    private String longForm;
    
    private String lang;
    
//    @ManyToOne
//    private QuestionConcept questionConcept;
    
    @Enumerated(EnumType.STRING)
    private DataType dataType;
    
    @Enumerated(EnumType.STRING)
    public Measurement measurement;
    
    @Enumerated(EnumType.STRING)
    public PrivacyType privacyType;
    
    private Double minValue;
    
    private Double maxValue;
    
    @Min(1)
    @Max(4000)
    private Integer maxLength;
    
    @Value("0")
    private Long latency;
    
    @Value("100")
    private Integer priority;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date created = new Date();

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "FF")
    private Date updated = new Date();

    private String updatedBy;

    @NotNull
    private String createdBy;
    
    @NotNull
    private String ownerId;
    
    @NotNull
    @ManyToOne
    private Session session;
    
//    @NotNull
//    @ManyToOne
//    private Participant owner;
    
    public String toJson() {
	   return new JSONSerializer()
	   	.exclude("*.class")
	   	.exclude("created")
	   	.exclude("updated")
	   	.exclude("createdBy")
	   	.exclude("updatedBy")
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
	
	
	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
//	private Set<Subscription> subscriptions = new HashSet<Subscription>();
}
