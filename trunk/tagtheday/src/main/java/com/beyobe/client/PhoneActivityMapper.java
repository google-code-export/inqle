
package com.beyobe.client;

import com.beyobe.client.activities.TagdayActivity;
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
		return new TagdayActivity(place);
	}
}
