package com.beyobe.client;

import org.junit.BeforeClass;
import org.junit.Test;


import com.beyobe.client.App;
import com.beyobe.client.AppPlaceHistoryMapper;
import com.beyobe.client.Constants;
import com.beyobe.client.activities.LoginPlace;
import com.beyobe.client.beans.Parcel;
import com.beyobe.client.beans.Question;
import com.beyobe.client.event.QuestionSavedEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.place.shared.PlaceHistoryHandler;

public class TestCreateData extends GWTTestCase {

	private static final int SUBSCRIPTION_SIZE = 5;

	@Override
	public String getModuleName() {
		return "com.beyobe.tagtheday";
	}
	
	@Test
	public void testCreateParticipant() {
//		App.registerEvents();
//		AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
//		final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
//		historyHandler.register(App.placeController, App.eventBus, new LoginPlace());
		
		Parcel parcel = App.dataBus.newParcel();
		parcel.setUsername("TestCreateData");
		parcel.setPassword("password");
		parcel.setAction(Constants.SERVERACTION_SIGNUP);
		App.parcelClient.sendParcel(parcel);
		delayTestFinish(5000);
	}
	
//	@Test
//	public void testCreateRandomQuestions() {
//		Question[] qs = new Question[SUBSCRIPTION_SIZE];
//		for (int i=0; i<SUBSCRIPTION_SIZE; i++) {
//			Question q = Maker.q(App.participant);
//			qs[i] = q;
//			App.eventBus.fireEvent(new QuestionSavedEvent(q));
//		}
//	}
	
//	public void testAnswerQuestions() {
//		
//	}

}
