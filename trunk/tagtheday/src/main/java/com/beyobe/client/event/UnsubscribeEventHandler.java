package com.beyobe.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface UnsubscribeEventHandler extends EventHandler {
	void onUnsubscribe(UnsubscribeEvent event);
}
