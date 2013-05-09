/*
 * Copyright 2010 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.beyobe.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Participant;
import com.beyobe.client.beans.Question;
import com.beyobe.client.data.DataBus;
import com.beyobe.client.data.Teller;
import com.beyobe.client.event.DataCapturedEvent;
import com.beyobe.client.event.DataCapturedEventHandler;
import com.beyobe.client.event.EditQuestionEvent;
import com.beyobe.client.event.EditQuestionEventHandler;
import com.beyobe.client.event.QuestionSavedEvent;
import com.beyobe.client.event.QuestionSavedEventHandler;
import com.beyobe.client.event.TagClickedEvent;
import com.beyobe.client.event.TagClickedEventHandler;
import com.beyobe.client.views.LoginView;
import com.beyobe.client.views.LoginViewImpl;
import com.beyobe.client.views.TagdayView;
import com.beyobe.client.views.TagdayViewImpl;
import com.beyobe.client.widgets.AnswerForm;
import com.beyobe.client.widgets.QuestionForm;
import com.beyobe.client.widgets.TagButton;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.dialog.PopinDialog;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.RoundPanel;

/**
 * @author Daniel Kurka
 * 
 */
public class App {

	public static final EventBus eventBus = new SimpleEventBus();
	public static final PlaceController placeController = new PlaceController(eventBus);
	public static final TagdayView tagdayView = new TagdayViewImpl();
	public static final LoginView loginView = new LoginViewImpl();
	public static Participant participant;
	public static DataBus dataBus = new DataBus();
	public static Teller teller = new Teller();
	
	protected static PopinDialog answerPopin;
	private static Logger log = Logger.getLogger(App.class.getName());
	
	public static void registerEvents() {
		eventBus.addHandler(EditQuestionEvent.TYPE, new EditQuestionEventHandler() {
			//TODO add real event
			@Override
			public void onEditQuestion(EditQuestionEvent event) {
				answerPopin = new PopinDialog();
				RoundPanel editQuestionPanel = new RoundPanel();
				editQuestionPanel.addStyleName("ttd-editQuestionPanel");
				Button closeButton = new Button("x");
				closeButton.setImportant(true);
				closeButton.setSmall(true);
				closeButton.getElement().getStyle().setProperty("float", "right");
				closeButton.addTapHandler(new TapHandler() {
					@Override
					public void onTap(TapEvent event) {
						answerPopin.hide();
						answerPopin.clear();
					}
				});
				editQuestionPanel.add(closeButton);
				editQuestionPanel.add(new QuestionForm(event.getQuestion()));
				answerPopin.add(editQuestionPanel);
				
				answerPopin.show();
			}
		});
		
		eventBus.addHandler(TagClickedEvent.TYPE, new TagClickedEventHandler() {
			@Override
			public void onTagClicked(TagClickedEvent event) {
				TagButton tagButton = event.getTagButton();
				log.log(Level.INFO, "Tag clicked: " + tagButton.getQuestion().getLongForm());
				answerPopin = new PopinDialog();
				RoundPanel answerPanel = new RoundPanel();
				answerPanel.addStyleName("ttd-editAnswerPanel");
				Datum d = tagButton.getDatum();
				if (d==null) {
					answerPanel.getElement().getStyle().setBackgroundColor("pink");
				} else if (d.getStatus()!=Datum.STATUS_INFERRED) {
					answerPanel.getElement().getStyle().setBackgroundColor("light-green");
				} else {
					answerPanel.getElement().getStyle().setBackgroundColor("light-gray");
				}
				Button closeButton = new Button("x");
				closeButton.setImportant(true);
				closeButton.setSmall(true);
				closeButton.getElement().getStyle().setProperty("float", "right");
				closeButton.addTapHandler(new TapHandler() {
					@Override
					public void onTap(TapEvent event) {
						answerPopin.hide();
						answerPopin.clear();
					}
				});
				answerPanel.add(closeButton);
				answerPanel.add(new AnswerForm(tagButton));
				answerPopin.add(answerPanel);
				
				answerPopin.show();
			}
		});
		
		eventBus.addHandler(DataCapturedEvent.TYPE, new DataCapturedEventHandler() {
			@Override
			public void onDataCaptured(DataCapturedEvent event) {
				TagButton tagButton = event.getTagButton();
				
				answerPopin.hide();
				tagButton.refreshAppearance();
				
				dataBus.setDatum(tagButton.getDatum());
			}
		});
		
		eventBus.addHandler(QuestionSavedEvent.TYPE, new QuestionSavedEventHandler() {
			@Override
			public void onQuestionSaved(QuestionSavedEvent event) {
				Question question = event.getQuestion();
				
				answerPopin.hide();
				
				dataBus.saveQuestion(question);
			}
		});
		
	}

	public static long getParticipantId() {
		if (participant != null) return participant.getId();
		return 0;
	}

	public static void loadData() {
		
		
	}

	public static boolean isUserLoggedIn() {
		return (participant != null);
	}
}
