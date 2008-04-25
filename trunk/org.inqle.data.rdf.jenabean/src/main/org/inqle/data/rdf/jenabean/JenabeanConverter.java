/**
 * 
 */
package org.inqle.data.rdf.jenabean;

import java.util.ArrayList;
import java.util.List;

import org.inqle.data.rdf.jena.NamedModel;

import thewebsemantic.TypeWrapper;

/**
 * Utility class for converting Jenabeans to other objects
 * @author David Donohue
 * Mar 13, 2008
 */
public class JenabeanConverter {

//	public static List<String> getIds(List<? extends BasicJenabean> jenabeans) {
//		List<String> idList = new ArrayList<String>();
//		for (BasicJenabean jenabean: jenabeans) {
//			idList.add(jenabean.getId());
//		}
//		return idList;
//	}

	public static List<String> getIds(List<?> jenabeans) {
		List<String> idList = new ArrayList<String>();
		for (Object jenabean: jenabeans) {
			
			String id = getId(jenabean);
			idList.add(id);
		}
		return idList;
	}

	public static String getId(Object jenabean) {
		TypeWrapper typeWrapper = TypeWrapper.wrap(jenabean.getClass());
		return typeWrapper.id(jenabean);
	}
}
