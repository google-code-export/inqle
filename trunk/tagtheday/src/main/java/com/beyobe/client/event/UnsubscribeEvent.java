package com.beyobe.client.event;

import com.beyobe.client.beans.Question;
import com.google.gwt.event.shared.GwtEvent;

public class UnsubscribeEvent extends GwtEvent<UnsubscribeEventHandler> {

public static Type<UnsubscribeEventHandler> TYPE = new Type<UnsubscribeEventHandler>();
private Question question;


public UnsubscribeEvent(Question question) {
	this.question = question;
}

public Question getQuestion() {
	return question;
}

  @Override
public Type<UnsubscribeEventHandler> getAssociatedType() {
    return TYPE;
}

@Override
protected void dispatch(UnsubscribeEventHandler handler) {
    handler.onUnsubscribe(this);
}
}