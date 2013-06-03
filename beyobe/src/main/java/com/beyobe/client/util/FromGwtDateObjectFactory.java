package com.beyobe.client.util;

import java.lang.reflect.Type;
import java.util.Date;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

public class FromGwtDateObjectFactory implements ObjectFactory {

	@Override
	public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
		if (! (value instanceof Long)) {
			return null;
		}
		Long longDate = (Long)value;
		Date date = new Date(longDate);
		return date;
	}

}
