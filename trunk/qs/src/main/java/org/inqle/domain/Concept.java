package org.inqle.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.inqle.domain.security.Principal;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString(toStringMethod="getStringRep")
@RooJpaActiveRecord
public class Concept {

    @NotNull
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date created = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "FF")
    private Date updated = null;
    
    @Column(unique = true)
//    @Pattern(regexp = "^[_A-Za-z0-9]$")
    private String conceptkey;

    @ManyToOne
    private Principal createdBy;

    @ManyToOne
    private Principal updatedBy;
    
    public String getStringRep() {
    	return conceptkey;
    }
}
