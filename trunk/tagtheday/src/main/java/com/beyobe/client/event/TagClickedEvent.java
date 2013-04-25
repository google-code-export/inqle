package com.beyobe.client.event;

import com.beyobe.client.widgets.TagButton;
import com.google.gwt.event.shared.GwtEvent;

public class TagClickedEvent extends GwtEvent<TagClickedEventHandler> {

public static Type<TagClickedEventHandler> TYPE = new Type<TagClickedEventHandler>();
private TagButton tagButton;

  public TagClickedEvent(TagButton tagButton) {
	this.tagButton = tagButton;
}

@Override
public Type<TagClickedEventHandler> getAssociatedType() {
    return TYPE;
}

@Override
protected void dispatch(TagClickedEventHandler handler) {
    handler.onTagClicked(this);
}

public TagButton getTagButton() {
	return tagButton;
}

}