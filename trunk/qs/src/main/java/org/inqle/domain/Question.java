package org.inqle.domain;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.inqle.domain.security.Principal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.solr.RooSolrSearchable;
import org.springframework.roo.addon.tostring.RooToString;

@RooJson
@RooJavaBean
@RooToString
@RooJpaEntity
@RooSolrSearchable
public class Question {

    private String text;

    @NotNull
    private String tag;

    private String abbreviation;

    @Value("0")
    private int latency;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date created = new Date();

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "FF")
    private Date updated = new Date();

    @ManyToOne
    private Concept concept;

    @ManyToOne
    private Principal createdBy;

    @ManyToOne
    private Principal updatedBy;

    @NotNull
    @Value("100")
    private int priority;
    
    private String lang;
}
