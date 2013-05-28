package com.beyobe.client;

import java.util.logging.Logger;

import com.beyobe.client.activities.LoginActivity;
import com.beyobe.client.activities.TagdayActivity;
import com.beyobe.client.activities.TagdayPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class TabletMainActivityMapper implements ActivityMapper {

	private static Logger log = Logger.getLogger(TabletMainActivityMapper.class.getName());
	
//	private final ClientFactory clientFactory;
//
//	public TabletMainActivityMapper(ClientFactory clientFactory) {
//		this.clientFactory = clientFactory;
//
//	}

	public Activity getActivity(Place place) {
		log.info("Going to place: " + place);
		if (App.sessionToken==null) {
			return new LoginActivity(place);
		}
		if (place instanceof TagdayPlace) {
			return new TagdayActivity(place);
		}
		return new TagdayActivity(place);
	}

}
