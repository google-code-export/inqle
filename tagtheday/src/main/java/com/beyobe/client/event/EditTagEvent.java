package com.beyobe.client.event;

import com.beyobe.client.beans.Question;
import com.beyobe.client.widgets.Block;
import com.google.gwt.event.shared.GwtEvent;

public class EditTagEvent extends GwtEvent<EditTagEventHandler> {

public static Type<EditTagEventHandler> TYPE = new Type<EditTagEventHandler>();
private Question question;


public EditTagEvent(Question question) {
	this.question = question;
}

public Question getQuestion() {
	return question;
}

  @Override
public Type<EditTagEventHandler> getAssociatedType() {
    return TYPE;
}

@Override
protected void dispatch(EditTagEventHandler handler) {
    handler.onNewTag(this);
}
}