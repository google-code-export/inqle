package com.beyobe.client.event;

import com.beyobe.client.beans.Question;
import com.beyobe.client.widgets.TagButton;
import com.google.gwt.event.shared.GwtEvent;

public class UnsubscribeEvent extends GwtEvent<UnsubscribeEventHandler> {

public static Type<UnsubscribeEventHandler> TYPE = new Type<UnsubscribeEventHandler>();
private TagButton tagButton;


public UnsubscribeEvent(TagButton tagButton) {
	this.tagButton = tagButton;
}

public TagButton getTagButton() {
	return tagButton;
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