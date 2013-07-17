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
import com.beyobe.client.beans.Participant;
import com.beyobe.client.beans.Question;
import com.beyobe.client.beans.UserRole;
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
import com.beyobe.client.views.LoginView;
import com.beyobe.client.views.LoginViewImpl;
import com.beyobe.client.views.QuestionView;
import com.beyobe.client.views.QuestionViewImpl;
import com.beyobe.client.views.SignupView;
import com.beyobe.client.views.SignupViewImpl;
import com.beyobe.client.views.TagdayView;
import com.beyobe.client.views.TagdayViewImpl;
import com.beyobe.client.widgets.AnswerForm;
import com.beyobe.client.widgets.QuestionForm;
import com.beyobe.client.widgets.TagButton;
import com.google.gwt.core.shared.GWT;
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
	public static QuestionForm questionForm = new QuestionForm(null);
	public static final QuestionView questionView = new QuestionViewImpl();
	public static SignupView signupView  = new SignupViewImpl();
	public static Participant participant;
	public static DataBus dataBus = new DataBus();
	public static ParcelClient parcelClient = new ParcelClient();
	public static String sessionToken;
//	public static RoundPanel editQuestionPanel;
	
	
	public static TagthedayAutoBeanFactory tagthedayAutoBeanFactory = GWT.create(TagthedayAutoBeanFactory.class);
	
	protected static PopinDialog answerPopin;
	private static Logger log = Logger.getLogger(App.class.getName());
	
	
	public static void registerEvents() {
		eventBus.addHandler(EditQuestionEvent.TYPE, new EditQuestionEventHandler() {

			//TODO add real event
			@Override
			public void onEditQuestion(EditQuestionEvent event) {
//				answerPopin = new PopinDialog();
//				RoundPanel editQuestionPanel = new RoundPanel();
//				editQuestionPanel.addStyleName("ttd-editQuestionPanel");
//				Button closeButton = new Button("x");
//				closeButton.setImportant(true);
//				closeButton.setSmall(true);
//				closeButton.getElement().getStyle().setProperty("float", "right");
//				closeButton.addTapHandler(new TapHandler() {
//					@Override
//					public void onTap(TapEvent event) {
//						answerPopin.hide();
//						answerPopin.clear();
//					}
//				});
//				editQuestionPanel.add(closeButton);
//				questionForm = new QuestionForm(event.getQuestion());
//				
//				editQuestionPanel.add(questionForm);
//				ScrollPanel scrollPanel = new ScrollPanel();
//				scrollPanel.setWidget(editQuestionPanel);
//				scrollPanel.setScrollingEnabledX(false);
//				scrollPanel.setScrollingEnabledY(true);
//				scrollPanel.setHeight("1000px");
////				editQuestionPanel.add(new HTML("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur tincidunt, arcu eget accumsan ullamcorper, ante nisl viverra enim, id consequat ante metus sed nibh. Sed orci nisl, dictum sit amet bibendum id, mollis ac arcu. Mauris venenatis orci sed dui lacinia vulputate. Proin in commodo nisl. Curabitur libero sem, tincidunt et eleifend et, euismod ac arcu. Etiam sit amet nulla mauris, id pulvinar enim. Quisque tincidunt accumsan tempor. Donec et euismod augue. Quisque ultricies mollis metus cursus consectetur. Ut sollicitudin magna in velit vulputate tempus. Sed metus metus, tincidunt nec consectetur vitae, sagittis ac velit Vestibulum consectetur, velit sed consectetur tempor, sapien odio tempor nulla, vel interdum mauris mi ac sem. Proin fermentum dictum mattis. Praesent eleifend posuere orci, vel rhoncus ante consequat eu. Vivamus eu nisl ornare nibh pellentesque fringilla. Sed tincidunt felis gravida mauris gravida sed venenatis mauris tincidunt. Pellentesque varius neque non arcu dictum consequat. Nulla vitae orci felis, ac egestas nisl. Vivamus semper sollicitudin mollis. Donec ac diam ut magna tempor consectetur. Donec at metus ligula, sed hendrerit sapien. Proin quis urna dui, id tincidunt tellus. Morbi enim ligula, mollis ut congue non, commodo nec magna. Quisque sagittis vehicula dui, ac aliquam tortor scelerisque a. Morbi urna ipsum, feugiat vitae fringilla in, blandit adipiscing tellus. Donec in est id tortor viverra viverra. Proin eu arcu sem, eget tincidunt est. Nunc in erat risus. Praesent pharetra pulvinar volutpat. Donec semper diam in enim luctus in viverra nisi tincidunt. Aliquam cursus interdum posuere."));
//				answerPopin.add(scrollPanel);
//				
//				answerPopin.show();
				
				questionView.setQuestion(event.getQuestion());
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
				Datum d = tagButton.getDatum();
				if (d==null) {
					answerPanel.getElement().getStyle().setBackgroundColor("pink");
				} else if (d.getAnswerStatus()!=AnswerStatus.INFERRED) {
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
				
				//if user is admin or owner of the question, show the edit button
				if (UserRole.ROLE_ADMIN == participant.getRole() || participant.getId().equals(tagButton.getQuestion().getOwnerId())) {
					Button editButton = new Button("edit");
					editButton.setSmall(true);
					editButton.getElement().getStyle().setProperty("float", "right");
					editButton.addTapHandler(new TapHandler() {
						@Override
						public void onTap(TapEvent event) {
							answerPopin.hide();
							answerPopin.clear();
							App.eventBus.fireEvent(new EditQuestionEvent(tagButton.getQuestion()));
						}
					});
					answerPanel.add(editButton);
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
				
				answerPopin.hide();
				tagButton.refreshAppearance();
				
				dataBus.setDatum(tagButton.getDatum(), tagButton.getQuestion());
			}
		});
		
		eventBus.addHandler(QuestionSavedEvent.TYPE, new QuestionSavedEventHandler() {
			@Override
			public void onQuestionSaved(QuestionSavedEvent event) {
				Question question = event.getQuestion();
//				answerPopin.hide();
				dataBus.saveQuestion(question);
				placeController.goTo(new TagdayPlace());
			}
		});
		
	}

	public static String getParticipantId() {
		if (participant != null) return participant.getId();
		return null;
	}

	public static void loadData() {
		
		
	}

	public static boolean isUserLoggedIn() {
		return (sessionToken != null);
	}
}
