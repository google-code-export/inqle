package com.beyobe.client;

import com.beyobe.client.activities.TagdayActivity;
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

	@Override
	public Activity getActivity(Place place) {
		return new TagdayActivity(place);
	}

}
