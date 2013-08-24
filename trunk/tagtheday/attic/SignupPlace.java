package com.beyobe.client.activities;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class SignupPlace extends Place {

	public static class SignupPlaceTokenizer implements PlaceTokenizer<SignupPlace> {

		@Override
		public SignupPlace getPlace(String token) {
			return new SignupPlace();
		}

		@Override
		public String getToken(SignupPlace place) {
			return "signup";
		}

	}

}
