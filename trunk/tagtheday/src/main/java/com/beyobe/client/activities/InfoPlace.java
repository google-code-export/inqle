package com.beyobe.client.activities;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.beyobe.client.beans.InfoStatus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class InfoPlace extends Place {

	private static Logger log = Logger.getLogger(InfoPlace.class.getName());
	
	public static class LoginPlaceTokenizer implements PlaceTokenizer<InfoPlace> {

		@Override
		public InfoPlace getPlace(String token) {
			String status = token.substring(token.indexOf("/") + 1);
			InfoStatus theInfoStatus = InfoStatus.STANDARD;
			try {
				theInfoStatus = InfoStatus.valueOf(status);
			} catch (Exception e) {
				log.log(Level.SEVERE, "Unrecognized InfoStatus: " + status);
				//leave as default
			}
			return new InfoPlace(theInfoStatus);
		}

		@Override
		public String getToken(InfoPlace place) {
			return "info/" + place.getInfoStatus().name();
		}

	}

	private InfoStatus infoStatus;
	
	public InfoStatus getInfoStatus() {
		return infoStatus;
	}

	public InfoPlace(InfoStatus infoStatus) {
		this.infoStatus = infoStatus;
	}

}
