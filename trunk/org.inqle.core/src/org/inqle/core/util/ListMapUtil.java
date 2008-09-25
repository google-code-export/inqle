package org.inqle.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ListMapUtil {

	private static Logger log = Logger.getLogger(ListMapUtil.class);
	/**
	 * This method merges the results of 2 SPARQL
	 * queries, each having been converted to XML format
	 * The second Document (addDocument) is added to the first.
	 * 
	 * Note that it assumes that the 2 Documents 
	 * have identical structure in terms of variables
	 * (columns) present.
	 * @param originalDocument
	 * @param addDocument
	 * @return
	 */
	public static List<SortedMap<String, String>> merge(List<SortedMap<String, String>> originalListMap, List<SortedMap<String, String>> addListMap) {
		if (originalListMap==null || originalListMap.size()==0) {
			return addListMap;
		}
		if (addListMap==null || addListMap.size()==0) {
			return originalListMap;
		}
		List<SortedMap<String, String>> finalListMap = deduplicate(originalListMap);
		for (SortedMap<String, String> record: addListMap) {
			if (! finalListMap.contains(record)) {
				finalListMap.add(record);
			} else {
				log.info("Did not merge duplicate: " + record);
			}
		}
		return finalListMap;
	}
	
	public static List<SortedMap<String, String>> deduplicate(List<SortedMap<String, String>> listMap) {
		List<SortedMap<String, String>> deduplicatedListMap = new ArrayList<SortedMap<String, String>>();
		for (SortedMap<String, String> record: listMap) {
			if (! deduplicatedListMap.contains(record)) {
				deduplicatedListMap.add(record);
			} else {
				log.info("Removed duplicate: " + record);
			}
		}
		return deduplicatedListMap;
	}
	
}
