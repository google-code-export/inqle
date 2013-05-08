package com.beyobe.client.activities;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class TagdayPlace extends Place {

	public static class TagdayPlaceTokenizer implements PlaceTokenizer<TagdayPlace> {

		@Override
		public TagdayPlace getPlace(String token) {
			return new TagdayPlace();
		}

		@Override
		public String getToken(TagdayPlace place) {
			return "";
		}

	}

}
