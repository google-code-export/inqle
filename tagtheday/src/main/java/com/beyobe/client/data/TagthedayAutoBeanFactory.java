package com.beyobe.client.data;

import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Parcel;
import com.beyobe.client.beans.Question;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface TagthedayAutoBeanFactory extends AutoBeanFactory {

	AutoBean<Parcel> parcel();
	AutoBean<Question> question();
	AutoBean<Datum> datum();
}
