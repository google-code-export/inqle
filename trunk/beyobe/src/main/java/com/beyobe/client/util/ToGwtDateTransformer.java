package com.beyobe.client.util;

import java.util.Date;

import flexjson.transformer.Transformer;

public class ToGwtDateTransformer implements Transformer {

	@Override
	public void transform(Object object) {
		if (! (object instanceof Date)) {
			return;
		}
		Date date = (Date)object;
		Long dateMs = date.getTime();
		object = dateMs;
	}

}
