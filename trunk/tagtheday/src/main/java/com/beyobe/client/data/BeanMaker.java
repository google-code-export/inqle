package com.beyobe.client.data;

import com.beyobe.client.App;
import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Question;
import com.google.web.bindery.autobean.shared.AutoBean;

public class BeanMaker {

	public static Question makeQuestion() {
		AutoBean<Question> question = App.tagthedayAutoBeanFactory.question();
	    return question.as();
	}
	
	public static Datum makeDatum() {
		AutoBean<Datum> datum = App.tagthedayAutoBeanFactory.datum();
	    return datum.as();
	}
	
}
