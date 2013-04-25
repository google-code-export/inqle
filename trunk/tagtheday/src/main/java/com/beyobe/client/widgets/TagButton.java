package com.beyobe.client.widgets;

import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Question;
import com.googlecode.mgwt.ui.client.widget.Button;

public class TagButton extends Button {



//	public TagButton() {
//		// TODO Auto-generated constructor stub
//	}

	public TagButton(Question question, Datum datum) {
		super();
//		this.addStyleName("mgwt-Button-small");
//		this.addStyleName("mgwt-Button-round");
		this.setSmall(true);
        this.setRound(true);
		this.addStyleName("ttd-TagButton");
		this.question = question;
		this.datum = datum;
		if(datum == null) {
			setText(question.getShortForm() + "?");
			setTitle(question.getLongForm());
//			this.addStyleName("ttd-TagButton-unanswered");
			this.setImportant(true);
		} else {
			setText(question.getAbbreviation() + " " + datum.getTextValue());
			setTitle(question.getLongForm() + " " + datum.getTextValue());
//			this.addStyleName("ttd-TagButton-answered");
			if (datum.getStatus()!=Datum.STATUS_INFERRED) this.setConfirm(true);
		}
		
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

	private Question question;
	private Datum datum;
}
