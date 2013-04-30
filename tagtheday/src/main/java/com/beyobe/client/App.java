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
import com.beyobe.client.data.DataBus;
import com.beyobe.client.event.DataCapturedEvent;
import com.beyobe.client.event.DataCapturedEventHandler;
import com.beyobe.client.event.NewTagEvent;
import com.beyobe.client.event.NewTagEventHandler;
import com.beyobe.client.event.TagClickedEvent;
import com.beyobe.client.event.TagClickedEventHandler;
import com.beyobe.client.views.TagdayView;
import com.beyobe.client.views.TagdayViewImpl;
import com.beyobe.client.widgets.AnswerForm;
import com.beyobe.client.widgets.TagButton;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
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
	public static Participant participant;
	public static DataBus dataBus = new DataBus();
	
	protected static PopinDialog answerPopin;
	private static Logger log = Logger.getLogger(App.class.getName());
	
	public static void registerEvents() {
		eventBus.addHandler(NewTagEvent.TYPE, new NewTagEventHandler() {
			//TODO add real event
			@Override
			public void onNewTag(NewTagEvent event) {
				log.log(Level.INFO, "New Tag");
				Window.alert("New Tag");
			}
		});
		
		eventBus.addHandler(TagClickedEvent.TYPE, new TagClickedEventHandler() {
			@Override
			public void onTagClicked(TagClickedEvent event) {
				TagButton tagButton = event.getTagButton();
				log.log(Level.INFO, "Tag clicked: " + tagButton.getQuestion().getLongForm());
				answerPopin = new PopinDialog();
				RoundPanel answerPanel = new RoundPanel();
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
				//TODO real event
			}
		});
		
	}

	public static long getParticipantId() {
		if (participant != null) return participant.getId();
		return 0;
	}

	/**
	 * Get the participant (current user)
	 */
	public static void loadParticipant() {
		participant = new Participant();
		participant.setId(1L);
		
	}
}
