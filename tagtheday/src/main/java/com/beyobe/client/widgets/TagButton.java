package com.beyobe.client.widgets;

import java.util.Date;

import com.beyobe.client.beans.AnswerStatus;
import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Question;
import com.googlecode.mgwt.ui.client.widget.Button;

public class TagButton extends Button {
	private Question question;
	private Datum datum;
	private Date effectiveDate;


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
			setText(question.getAbbreviation() + "?");
			setTitle(question.getLongForm());
//			this.addStyleName("ttd-TagButton-unanswered");
			this.setImportant(true);
		} else {
			setText(question.getAbbreviation() + " " + datum.getTextValue());
			setTitle(question.getLongForm() + " " + datum.getTextValue());
//			this.addStyleName("ttd-TagButton-answered");
			if (datum.getAnswerStatus()!=AnswerStatus.INFERRED) this.setConfirm(true);
		}
		
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
}
