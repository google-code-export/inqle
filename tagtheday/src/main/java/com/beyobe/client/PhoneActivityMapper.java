
package com.beyobe.client;

import com.beyobe.client.activities.InfoActivity;
import com.beyobe.client.activities.InfoPlace;
import com.beyobe.client.activities.LoginActivity;
import com.beyobe.client.activities.LoginPlace;
import com.beyobe.client.activities.QuestionActivity;
import com.beyobe.client.activities.QuestionPlace;
import com.beyobe.client.activities.TagdayActivity;
import com.beyobe.client.activities.TagdayPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;


/**
 * 
 */
public class PhoneActivityMapper implements ActivityMapper {

//	private final ClientFactory clientFactory;
//
//	public PhoneActivityMapper(ClientFactory clientFactory) {
//		this.clientFactory = clientFactory;
//	}

	@Override
	//TODO have more than 1 place?
	public Activity getActivity(Place place) {
		if (place instanceof TagdayPlace) {
			return new TagdayActivity(place);
		}
		if (place instanceof LoginPlace) {
			return new LoginActivity(place);
		}
		if (place instanceof InfoPlace) {
			return new InfoActivity((InfoPlace)place);
		}
//		if (place instanceof SignupPlace) {
//			return new SignupActivity(place);
//		}
		if (place instanceof QuestionPlace) {
			return new QuestionActivity(place);
		}
		return new TagdayActivity(place);
	}
}
