package com.beyobe.client;

import com.beyobe.client.activities.LoginActivity;
import com.beyobe.client.activities.TagdayActivity;
import com.beyobe.client.activities.TagdayPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class TabletMainActivityMapper implements ActivityMapper {

//	private final ClientFactory clientFactory;
//
//	public TabletMainActivityMapper(ClientFactory clientFactory) {
//		this.clientFactory = clientFactory;
//
//	}

	public Activity getActivity(Place place) {
		if (place instanceof TagdayPlace) {
			return new TagdayActivity(place);
		}
		//default: login
		return new LoginActivity(place);
	}

}
