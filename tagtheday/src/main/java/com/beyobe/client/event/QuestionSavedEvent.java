package com.beyobe.client.event;

import com.beyobe.client.beans.Question;
import com.beyobe.client.widgets.TagButton;
import com.google.gwt.event.shared.GwtEvent;

public class QuestionSavedEvent extends GwtEvent<QuestionSavedEventHandler> {

public static Type<QuestionSavedEventHandler> TYPE = new Type<QuestionSavedEventHandler>();
private Question question;

  public QuestionSavedEvent(Question question) {
	this.question = question;
}

@Override
public Type<QuestionSavedEventHandler> getAssociatedType() {
    return TYPE;
}

@Override
protected void dispatch(QuestionSavedEventHandler handler) {
    handler.onQuestionSaved(this);
}

public Question getQuestion() {
	return question;
}

}