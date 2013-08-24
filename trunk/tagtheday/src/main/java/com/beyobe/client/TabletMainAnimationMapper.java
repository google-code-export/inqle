package com.beyobe.client;

import com.beyobe.client.activities.LoginPlace;
import com.beyobe.client.activities.QuestionPlace;
import com.google.gwt.place.shared.Place;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.mvp.client.AnimationMapper;

public class TabletMainAnimationMapper implements AnimationMapper {

	@Override
	public Animation getAnimation(Place oldPlace, Place newPlace) {
		if (newPlace instanceof QuestionPlace) {
			return Animation.SLIDE_UP_REVERSE;
		}
		
		if (oldPlace instanceof QuestionPlace) {
			return Animation.SLIDE_UP;
		}
//		if (newPlace instanceof SignupPlace) {
//			return Animation.SLIDE;
//		}
		if (newPlace instanceof LoginPlace) {
			return Animation.SLIDE_REVERSE;
		}
		return Animation.SLIDE;
	}

}
