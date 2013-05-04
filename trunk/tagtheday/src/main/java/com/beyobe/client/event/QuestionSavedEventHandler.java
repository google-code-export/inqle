package com.beyobe.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface QuestionSavedEventHandler extends EventHandler {
	void onQuestionSaved(QuestionSavedEvent event);
}
