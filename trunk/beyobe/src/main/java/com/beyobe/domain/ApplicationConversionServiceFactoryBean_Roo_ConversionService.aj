// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.beyobe.domain;

import com.beyobe.domain.ApplicationConversionServiceFactoryBean;
import com.beyobe.domain.Choice;
import com.beyobe.domain.ChoiceConcept;
import com.beyobe.domain.Datum;
import com.beyobe.domain.Formula;
import com.beyobe.domain.Participant;
import com.beyobe.domain.Question;
import com.beyobe.domain.QuestionConcept;
import com.beyobe.domain.Subscription;
import com.beyobe.repository.DatumRepository;
import com.beyobe.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    declare @type: ApplicationConversionServiceFactoryBean: @Configurable;
    
    @Autowired
    DatumRepository ApplicationConversionServiceFactoryBean.datumRepository;
    
    @Autowired
    QuestionRepository ApplicationConversionServiceFactoryBean.questionRepository;
    
    public Converter<Choice, String> ApplicationConversionServiceFactoryBean.getChoiceToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.beyobe.domain.Choice, java.lang.String>() {
            public String convert(Choice choice) {
                return new StringBuilder().append(choice.getId()).append(' ').append(choice.getCreated()).append(' ').append(choice.getUpdated()).append(' ').append(choice.getLang()).toString();
            }
        };
    }
    
    public Converter<Long, Choice> ApplicationConversionServiceFactoryBean.getIdToChoiceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.beyobe.domain.Choice>() {
            public com.beyobe.domain.Choice convert(java.lang.Long id) {
                return Choice.findChoice(id);
            }
        };
    }
    
    public Converter<String, Choice> ApplicationConversionServiceFactoryBean.getStringToChoiceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.beyobe.domain.Choice>() {
            public com.beyobe.domain.Choice convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Choice.class);
            }
        };
    }
    
    public Converter<ChoiceConcept, String> ApplicationConversionServiceFactoryBean.getChoiceConceptToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.beyobe.domain.ChoiceConcept, java.lang.String>() {
            public String convert(ChoiceConcept choiceConcept) {
                return new StringBuilder().append(choiceConcept.getKey()).append(' ').append(choiceConcept.getCreated()).append(' ').append(choiceConcept.getUpdated()).append(' ').append(choiceConcept.getUpdatedBy()).toString();
            }
        };
    }
    
    public Converter<String, ChoiceConcept> ApplicationConversionServiceFactoryBean.getIdToChoiceConceptConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.beyobe.domain.ChoiceConcept>() {
            public com.beyobe.domain.ChoiceConcept convert(java.lang.String id) {
                return ChoiceConcept.findChoiceConcept(id);
            }
        };
    }
    
    public Converter<Datum, String> ApplicationConversionServiceFactoryBean.getDatumToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.beyobe.domain.Datum, java.lang.String>() {
            public String convert(Datum datum) {
                return new StringBuilder().append(datum.getCreated()).append(' ').append(datum.getUpdated()).append(' ').append(datum.getEffectiveDate()).append(' ').append(datum.getQuestionId()).toString();
            }
        };
    }
    
    public Converter<String, Datum> ApplicationConversionServiceFactoryBean.getIdToDatumConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.beyobe.domain.Datum>() {
            public com.beyobe.domain.Datum convert(java.lang.String id) {
                return datumRepository.findOne(id);
            }
        };
    }
    
    public Converter<Formula, String> ApplicationConversionServiceFactoryBean.getFormulaToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.beyobe.domain.Formula, java.lang.String>() {
            public String convert(Formula formula) {
                return new StringBuilder().append(formula.getId()).append(' ').append(formula.getCreated()).append(' ').append(formula.getUpdated()).append(' ').append(formula.getExpression()).toString();
            }
        };
    }
    
    public Converter<Long, Formula> ApplicationConversionServiceFactoryBean.getIdToFormulaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.beyobe.domain.Formula>() {
            public com.beyobe.domain.Formula convert(java.lang.Long id) {
                return Formula.findFormula(id);
            }
        };
    }
    
    public Converter<String, Formula> ApplicationConversionServiceFactoryBean.getStringToFormulaConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.beyobe.domain.Formula>() {
            public com.beyobe.domain.Formula convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Formula.class);
            }
        };
    }
    
    public Converter<Participant, String> ApplicationConversionServiceFactoryBean.getParticipantToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.beyobe.domain.Participant, java.lang.String>() {
            public String convert(Participant participant) {
                return new StringBuilder().append(participant.getUsername()).append(' ').append(participant.getPassword()).append(' ').append(participant.getCreated()).append(' ').append(participant.getUpdated()).toString();
            }
        };
    }
    
    public Converter<String, Participant> ApplicationConversionServiceFactoryBean.getIdToParticipantConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.beyobe.domain.Participant>() {
            public com.beyobe.domain.Participant convert(java.lang.String id) {
                return Participant.findParticipant(id);
            }
        };
    }
    
    public Converter<Question, String> ApplicationConversionServiceFactoryBean.getQuestionToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.beyobe.domain.Question, java.lang.String>() {
            public String convert(Question question) {
                return new StringBuilder().append(question.getAbbreviation()).append(' ').append(question.getLongForm()).append(' ').append(question.getLang()).append(' ').append(question.getMinValue()).toString();
            }
        };
    }
    
    public Converter<String, Question> ApplicationConversionServiceFactoryBean.getIdToQuestionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.beyobe.domain.Question>() {
            public com.beyobe.domain.Question convert(java.lang.String id) {
                return questionRepository.findOne(id);
            }
        };
    }
    
    public Converter<QuestionConcept, String> ApplicationConversionServiceFactoryBean.getQuestionConceptToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.beyobe.domain.QuestionConcept, java.lang.String>() {
            public String convert(QuestionConcept questionConcept) {
                return new StringBuilder().append(questionConcept.getKey()).append(' ').append(questionConcept.getCreated()).append(' ').append(questionConcept.getUpdated()).append(' ').append(questionConcept.getUpdatedBy()).toString();
            }
        };
    }
    
    public Converter<String, QuestionConcept> ApplicationConversionServiceFactoryBean.getIdToQuestionConceptConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.beyobe.domain.QuestionConcept>() {
            public com.beyobe.domain.QuestionConcept convert(java.lang.String id) {
                return QuestionConcept.findQuestionConcept(id);
            }
        };
    }
    
    public Converter<Subscription, String> ApplicationConversionServiceFactoryBean.getSubscriptionToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.beyobe.domain.Subscription, java.lang.String>() {
            public String convert(Subscription subscription) {
                return new StringBuilder().append(subscription.getCreated()).append(' ').append(subscription.getCreatedBy()).append(' ').append(subscription.getQuestionId()).toString();
            }
        };
    }
    
    public Converter<String, Subscription> ApplicationConversionServiceFactoryBean.getIdToSubscriptionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.beyobe.domain.Subscription>() {
            public com.beyobe.domain.Subscription convert(java.lang.String id) {
                return Subscription.findSubscription(id);
            }
        };
    }
    
    public void ApplicationConversionServiceFactoryBean.installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getChoiceToStringConverter());
        registry.addConverter(getIdToChoiceConverter());
        registry.addConverter(getStringToChoiceConverter());
        registry.addConverter(getChoiceConceptToStringConverter());
        registry.addConverter(getIdToChoiceConceptConverter());
        registry.addConverter(getDatumToStringConverter());
        registry.addConverter(getIdToDatumConverter());
        registry.addConverter(getFormulaToStringConverter());
        registry.addConverter(getIdToFormulaConverter());
        registry.addConverter(getStringToFormulaConverter());
        registry.addConverter(getParticipantToStringConverter());
        registry.addConverter(getIdToParticipantConverter());
        registry.addConverter(getQuestionToStringConverter());
        registry.addConverter(getIdToQuestionConverter());
        registry.addConverter(getQuestionConceptToStringConverter());
        registry.addConverter(getIdToQuestionConceptConverter());
        registry.addConverter(getSubscriptionToStringConverter());
        registry.addConverter(getIdToSubscriptionConverter());
    }
    
    public void ApplicationConversionServiceFactoryBean.afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
}
