package com.beyobe.client.util;

import java.lang.reflect.Type;
import java.util.Date;

import org.apache.log4j.Logger;

import com.beyobe.web.ServiceController;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

public class FromGwtDateObjectFactory implements ObjectFactory {

	private static final Logger log = Logger.getLogger(FromGwtDateObjectFactory.class);
	
	@Override
	public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
//		log.info("FromGwtDateObjectFactory:" + value.getClass());
		if (value == null || ! (value instanceof String)) {
			log.warn("FromGwtDateObjectFactory:" + value + " is not String not should be a string, containing a long epoch time");
			return null;
		}
		Long longDate = Long.parseLong((String)value);
		Date date = new Date(longDate);
		log.trace("FromGwtDateObjectFactory:" + value + " becomes date:" + date);
		return date;
	}

}
