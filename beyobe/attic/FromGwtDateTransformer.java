package com.beyobe.client.util;

import java.util.Date;

import flexjson.transformer.Transformer;

public class FromGwtDateTransformer implements Transformer {

	@Override
	public void transform(Object object) {
		if (! (object instanceof Long)) {
			return;
		}
		Long longDate = (Long)object;
		Date date = new Date(longDate);
		object = date;
	}

}
