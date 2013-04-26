package com.beyobe.client.event;

import com.beyobe.client.widgets.Block;
import com.google.gwt.event.shared.GwtEvent;

public class NewTagEvent extends GwtEvent<NewTagEventHandler> {

public static Type<NewTagEventHandler> TYPE = new Type<NewTagEventHandler>();
private Block block;


public NewTagEvent(Block block) {
	this.block = block;
}

public Block getBlock() {
	return block;
}

  @Override
public Type<NewTagEventHandler> getAssociatedType() {
    return TYPE;
}

@Override
protected void dispatch(NewTagEventHandler handler) {
    handler.onNewTag(this);
}
}