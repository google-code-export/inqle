package org.inqle.domain;

import javax.persistence.ManyToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class SurveyQuestion {

    @ManyToOne
    private Survey survey;

    @ManyToOne
    private Question question;

    private Integer priority;
}
