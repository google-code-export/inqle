package com.beyobe.client.event;

import com.beyobe.client.widgets.TagButton;
import com.google.gwt.event.shared.GwtEvent;

public class DataCapturedEvent extends GwtEvent<DataCapturedEventHandler> {

public static Type<DataCapturedEventHandler> TYPE = new Type<DataCapturedEventHandler>();
private TagButton tagButton;

  public DataCapturedEvent(TagButton tagButton) {
	this.tagButton = tagButton;
}

@Override
public Type<DataCapturedEventHandler> getAssociatedType() {
    return TYPE;
}

@Override
protected void dispatch(DataCapturedEventHandler handler) {
    handler.onDataCaptured(this);
}

public TagButton getTagButton() {
	return tagButton;
}

}