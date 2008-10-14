package org.inqle.data.rdf.jena.util;

import java.util.Date;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class TypeConverter {

	public static Literal parseStringToLiteral(String string) {
		try {
			Integer val = Integer.parseInt(string);
			return ResourceFactory.createTypedLiteral(val);
		} catch (Exception e) {}
		
		try {
			Double val = Double.parseDouble(string);
			return ResourceFactory.createTypedLiteral(val);
		} catch (Exception e) {}
		
		XSDDateTime val = DateUtil.tryToParseDate(string);
		if (val != null) {
			return ResourceFactory.createTypedLiteral(val);
		}
		
		return ResourceFactory.createTypedLiteral(string);
	}
	
	public static Object parseStringToType(String string) {
		try {
			Integer val = Integer.parseInt(string);
			return val;
		} catch (Exception e) {}
		
		try {
			Double val = Double.parseDouble(string);
			return val;
		} catch (Exception e) {}
		
		Date val = DateUtil.tryToParseDateObject(string);
		if (val != null) {
			return val;
		}
		
		return string;
	}
}
