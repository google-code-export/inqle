package com.beyobe.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class NewTagEvent extends GwtEvent<NewTagEventHandler> {

public static Type<NewTagEventHandler> TYPE = new Type<NewTagEventHandler>();

  @Override
public Type<NewTagEventHandler> getAssociatedType() {
    return TYPE;
}

@Override
protected void dispatch(NewTagEventHandler handler) {
    handler.onNewTag(this);
}
}