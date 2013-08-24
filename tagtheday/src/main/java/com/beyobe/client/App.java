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

import com.beyobe.client.activities.QuestionPlace;
import com.beyobe.client.activities.TagdayPlace;
import com.beyobe.client.beans.AnswerStatus;
import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Question;
import com.beyobe.client.beans.Session;
import com.beyobe.client.data.DataBus;
import com.beyobe.client.data.ParcelClient;
import com.beyobe.client.data.TagthedayAutoBeanFactory;
import com.beyobe.client.event.DataCapturedEvent;
import com.beyobe.client.event.DataCapturedEventHandler;
import com.beyobe.client.event.EditQuestionEvent;
import com.beyobe.client.event.EditQuestionEventHandler;
import com.beyobe.client.event.QuestionSavedEvent;
import com.beyobe.client.event.QuestionSavedEventHandler;
import com.beyobe.client.event.TagClickedEvent;
import com.beyobe.client.event.TagClickedEventHandler;
import com.beyobe.client.event.UnsubscribeEvent;
import com.beyobe.client.event.UnsubscribeEventHandler;
import com.beyobe.client.icons.Icons;
import com.beyobe.client.views.LoginView;
import com.beyobe.client.views.LoginViewImpl;
import com.beyobe.client.views.QuestionView;
import com.beyobe.client.views.QuestionViewImpl;
import com.beyobe.client.views.TagdayView;
import com.beyobe.client.views.TagdayViewImpl;
import com.beyobe.client.widgets.AnswerForm;
import com.beyobe.client.widgets.QuestionForm;
import com.beyobe.client.widgets.TagButton;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.dialog.PopinDialog;
import com.googlecode.mgwt.ui.client.widget.RoundPanel;
import com.googlecode.mgwt.ui.client.widget.touch.TouchDelegate;

/**
 * @author Dave Donohue
 * Provide singleton properties and static methods for looking up application objects and for handling application-wide events
 */
public class App {

	public static final EventBus eventBus = new SimpleEventBus();
	public static final PlaceController placeController = new PlaceController(eventBus);
	public static final TagdayView tagdayView = new TagdayViewImpl();
	public static final LoginView loginView = new LoginViewImpl();
	
	public static QuestionView questionView = new QuestionViewImpl();
//	public static SignupView signupView  = new SignupViewImpl();
	public static Session session;
	public static DataBus dataBus = new DataBus();
	public static ParcelClient parcelClient = new ParcelClient();
	public static String sessionToken;
	public static QuestionForm questionForm = new QuestionForm(null);
	public static Question question;
//	public static RoundPanel editQuestionPanel;
	
	
	public static TagthedayAutoBeanFactory tagthedayAutoBeanFactory = GWT.create(TagthedayAutoBeanFactory.class);
	
	protected static PopinDialog answerPopin;
	private static Logger log = Logger.getLogger(App.class.getName());
	
	
	public static void registerEvents() {
		eventBus.addHandler(EditQuestionEvent.TYPE, new EditQuestionEventHandler() {
			@Override
			public void onEditQuestion(EditQuestionEvent event) {
				App.question = event.getQuestion();
				placeController.goTo(new QuestionPlace());
			}
		});
		
		eventBus.addHandler(TagClickedEvent.TYPE, new TagClickedEventHandler() {
			@Override
			public void onTagClicked(TagClickedEvent event) {
				final TagButton tagButton = event.getTagButton();
				log.log(Level.INFO, "Tag clicked: " + tagButton);
				answerPopin = new PopinDialog();
				RoundPanel answerPanel = new RoundPanel();
				answerPanel.addStyleName("ttd-editAnswerPanel");
//				HorizontalPanel menuBar = new HorizontalPanel();
//				answerPanel.add(menuBar);
				
				Datum d = tagButton.getDatum();
				if (d==null) {
					answerPanel.getElement().getStyle().setBackgroundColor("pink");
				} else if (d.getAnswerStatus()!=AnswerStatus.INFERRED) {
					answerPanel.getElement().getStyle().setBackgroundColor("#98FB98");
				} else {
					answerPanel.getElement().getStyle().setBackgroundColor("light-gray");
				}
				
//				Button closeButton = new Button("x");
//				closeButton.setImportant(true);
//				closeButton.setSmall(true);
				Icons icons = GWT.create(Icons.class);
				
				Image closeIcon = new Image(icons.close32());
				closeIcon.getElement().getStyle().setProperty("float", "right");
				TouchDelegate closeTd = new TouchDelegate(closeIcon);
				closeTd.addTapHandler(new TapHandler() {
					@Override
					public void onTap(TapEvent event) {
						answerPopin.hide();
						answerPopin.clear();
					}
				});
				answerPanel.add(closeIcon);
				
				Image unsubscribeIcon = new Image(icons.recycle32());
				unsubscribeIcon.getElement().getStyle().setProperty("float", "top");
				TouchDelegate unsubscribeTd = new TouchDelegate(unsubscribeIcon);
				unsubscribeTd.addTapHandler(new TapHandler() {
					@Override
					public void onTap(TapEvent event) {
						if (Window.confirm("Are you sure you want to unsubscribe from this question?")) {
							App.eventBus.fireEvent(new UnsubscribeEvent(tagButton));
						}
					}
				});
				answerPanel.add(unsubscribeIcon);
				
				//if user is admin or owner of the question, show the edit button
				if (isAdminUser() || session.getUserUid().equals(tagButton.getQuestion().getOwnerId())) {
//					Button editButton = new Button("edit");
//					editButton.setSmall(true);
//					editButton.getElement().getStyle().setProperty("float", "right");
					
					Image editIcon = new Image(icons.pencil32());
					editIcon.getElement().getStyle().setProperty("float", "left");
					TouchDelegate editTd = new TouchDelegate(editIcon);
					editTd.addTapHandler(new TapHandler() {
						@Override
						public void onTap(TapEvent event) {
							answerPopin.hide();
							answerPopin.clear();
							App.eventBus.fireEvent(new EditQuestionEvent(tagButton.getQuestion()));
						}
					});
					answerPanel.add(editIcon);
				}
				
				answerPanel.add(new AnswerForm(tagButton));
				answerPopin.add(answerPanel);
				
				answerPopin.show();
			}

			
		});
		
		eventBus.addHandler(DataCapturedEvent.TYPE, new DataCapturedEventHandler() {
			@Override
			public void onDataCaptured(DataCapturedEvent event) {
				TagButton tagButton = event.getTagButton();
				
				if (answerPopin != null) answerPopin.hide();
				tagButton.refreshAppearance();
				
				dataBus.saveDatum(tagButton.getDatum(), tagButton.getQuestion());
			}
		});
		
		eventBus.addHandler(QuestionSavedEvent.TYPE, new QuestionSavedEventHandler() {
			@Override
			public void onQuestionSaved(QuestionSavedEvent event) {
				Question question = event.getQuestion();
//				answerPopin.hide();
				dataBus.saveQuestion(question);
				placeController.goTo(new TagdayPlace());
				App.question = null;
			}
		});
		
		eventBus.addHandler(UnsubscribeEvent.TYPE, new UnsubscribeEventHandler() {
			@Override
			public void onUnsubscribe(UnsubscribeEvent event) {
				TagButton tagButton = event.getTagButton();
				tagButton.setVisible(false);
				dataBus.removeQuestion(tagButton);
				if (answerPopin != null) answerPopin.hide();
			}
		});
		
	}

	public static boolean isAdminUser() {
		for (String role: session.getRoles()) {
			if (role.toLowerCase().indexOf("admin") >= 0) {
				return true;
			}
		}
		return false;
	}
	
	public static String getParticipantId() {
		if (session != null) return session.getUserUid();
		return null;
	}

//	public static void loadData() {
//		
//		
//	}

	public static boolean isUserLoggedIn() {
		return (sessionToken != null);
	}
}
