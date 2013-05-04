package com.beyobe.client.event;

import com.beyobe.client.beans.Question;
import com.beyobe.client.widgets.Block;
import com.google.gwt.event.shared.GwtEvent;

public class EditQuestionEvent extends GwtEvent<EditQuestionEventHandler> {

public static Type<EditQuestionEventHandler> TYPE = new Type<EditQuestionEventHandler>();
private Question question;


public EditQuestionEvent(Question question) {
	this.question = question;
}

public Question getQuestion() {
	return question;
}

  @Override
public Type<EditQuestionEventHandler> getAssociatedType() {
    return TYPE;
}

@Override
protected void dispatch(EditQuestionEventHandler handler) {
    handler.onEditQuestion(this);
}
}