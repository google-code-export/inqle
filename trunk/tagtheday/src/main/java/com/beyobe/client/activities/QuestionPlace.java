package com.beyobe.client.activities;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class QuestionPlace extends Place {

	public static class QuestionPlaceTokenizer implements PlaceTokenizer<QuestionPlace> {

		@Override
		public QuestionPlace getPlace(String token) {
			return new QuestionPlace();
		}

		@Override
		public String getToken(QuestionPlace place) {
			return "question";
		}

	}

}
