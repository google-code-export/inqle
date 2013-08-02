package com.beyobe.client.widgets;

import java.util.Date;
import java.util.logging.Logger;

import org.mortbay.log.Log;

import com.beyobe.client.beans.AnswerStatus;
import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Question;
import com.googlecode.mgwt.ui.client.widget.Button;

public class TagButton extends Button {
	@Override
	public String toString() {
		String s = "TagButton [effectiveDate=" + effectiveDate;
		if (question != null) s += ", question=" + question.getAbbreviation();
		if (datum != null) s += ", datum=" + datum.getTextValue() + "]";
		return s;
	}

	private Question question;
	private Datum datum;
	private Date effectiveDate;

	private static Logger log = Logger.getLogger("TagButton");

//	public TagButton() {
//		// TODO Auto-generated constructor stub
//	}

	public TagButton(Date effectiveDate, Question question, Datum datum) {
		super();
//		this.addStyleName("mgwt-Button-small");
//		this.addStyleName("mgwt-Button-round");
		this.setSmall(true);
        this.setRound(true);
		this.addStyleName("ttd-TagButton");
		this.setEffectiveDate(effectiveDate);
		this.question = question;
		this.datum = datum;
		refreshAppearance();
		
	}

//	public TagButton(ButtonCss css) {
//		super(css);
//		// TODO Auto-generated constructor stub
//	}
//
//	public TagButton(ButtonCss css, String text) {
//		super(css, text);
//		// TODO Auto-generated constructor stub
//	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Datum getDatum() {
		return datum;
	}

	public void setDatum(Datum datum) {
		this.datum = datum;
	}

	
	public void refreshAppearance() {
		if(datum == null) {
			log.info("refreshAppearance, datum=null");
			setText(question.getAbbreviation() + "?");
			setTitle(question.getLongForm());
//			this.addStyleName("ttd-TagButton-unanswered");
			this.setImportant(true);
		} else {
			log.info("refreshAppearance, datum != null");
			if (datum.getAnswerStatus() != AnswerStatus.INFERRED) {
				setText(question.getAbbreviation() + ": " + datum.getTextValue());
				setTitle(question.getLongForm() + " " + datum.getTextValue());
				this.setConfirm(true);
			} else {
				setText(question.getAbbreviation() + ": " + datum.getTextValue() + "?");
				setTitle(question.getLongForm() + " " + datum.getTextValue());
			}
//			this.addStyleName("ttd-TagButton-answered");
			
		}
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
}
