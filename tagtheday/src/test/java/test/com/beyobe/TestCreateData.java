package test.com.beyobe;

import org.junit.BeforeClass;
import org.junit.Test;

import test.com.beyobe.util.Maker;

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
		return "tagtheday";
	}
	
	@BeforeClass
	public static void createParticipant() {
		App.registerEvents();
		AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
		final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(App.placeController, App.eventBus, new LoginPlace());
		
		Parcel parcel = App.dataBus.newParcel();
		parcel.setUsername("user1");
		parcel.setPassword("password");
		App.parcelClient.sendParcel(parcel, Constants.SERVERACTION_SIGNUP);
	}
	
	@Test
	public void testCreateRandomQuestions() {
		Question[] qs = new Question[SUBSCRIPTION_SIZE];
		for (int i=0; i<SUBSCRIPTION_SIZE; i++) {
			Question q = Maker.q(App.participant);
			qs[i] = q;
			App.eventBus.fireEvent(new QuestionSavedEvent(q));
		}
	}
	
//	public void testAnswerQuestions() {
//		
//	}

}
