package org.inqle.data.sampling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import org.osgi.service.component.ComponentContext;

/**
 * This acts as a factory for creating an implementation of ISampler
 * (DES) at random.
 * 
 * It uses this algorithm:
 * If any DES have negative weight, only these are considered.  Always select the one
 * that has the lowest (most negative) number.
 * Otherwise, select among them where the probability of a given DES being selected = 
 * its weight / the total weight of all available DES
 * 
 * @author David Donohue
 * Dec 26, 2007
 * 
 * TODO permit selection on basis of user input: decide() method should take some argument which contains such info
 * TODO permit weighting of DES by user using RDF annotations, via the GUI
 */
public class SamplerDecider implements ISamplerDecider {

	private int totalWeight = 0;

	private HashMap<ISampler, Integer> normalPriorityDES = new HashMap<ISampler, Integer>();
	private HashMap<ISampler, Integer> topPriorityDES = new HashMap<ISampler, Integer>();
	//public static Logger log = Logger.getLogger(SamplerDecider.class);
	
	public ISampler decide() {
		
		if (normalPriorityDES.size() == 0 && topPriorityDES.size() == 0) {
			throw new RuntimeException("No ISampler components are available.");
		}
		if (topPriorityDES.size() > 0) {
			LinkedHashMap<ISampler, Integer> sortedTopDes = sortHashMapByValues(topPriorityDES, true);
			Iterator<ISampler> keyI = sortedTopDes.keySet().iterator();
			ISampler chosen = (ISampler)keyI.next();
			return chosen;
		}
		LinkedHashMap<ISampler, Integer> sortedNormalDes = sortHashMapByValues(normalPriorityDES, true);
		Iterator<ISampler> keyI = sortedNormalDes.keySet().iterator();
		Random random = new Random();
		int randomNum = random.nextInt(totalWeight);
		ISampler key = null;
		while (keyI.hasNext()) {
			key = (ISampler)keyI.next();
			Integer val = (Integer)sortedNormalDes.get(key);
			if (val.intValue() > randomNum) {
				return key;
			}
		}
		return key;
	}

	public LinkedHashMap<ISampler, Integer> sortHashMapByValues(HashMap passedMap, boolean ascending) {

		List mapKeys = new ArrayList(passedMap.keySet());
		List mapValues = new ArrayList(passedMap.values());
		Collections.sort(mapValues);
		Collections.sort(mapKeys);
	
		if (!ascending)
		Collections.reverse(mapValues);
	
		LinkedHashMap someMap = new LinkedHashMap();
		Iterator valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();
			while (keyIt.hasNext()) {
				Object key = keyIt.next();
				if (passedMap.get(key).toString().equals(val.toString())) {
					passedMap.remove(key);
					mapKeys.remove(key);
					someMap.put(key, val);
					break;
				}
			}
		}
		return someMap;
	} 
	
	/*
	private void sortByValue(HashMap<ISampler, Integer> topPriorityDES2) {
    Map<ISampler, Integer> map = new HashMap<ISampler, Integer>();

    // --- Put entries into map here ---

    // Get a list of the entries in the map
    List<Map.Entry<ISampler, Integer>> list = new Vector<Map.Entry<ISampler, Integer>>(map.entrySet());

    // Sort the list using an annonymous inner class implementing Comparator for the compare method
    java.util.Collections.sort(list, new Comparator<Map.Entry<ISampler, Integer>>(){
        public int compare(Map.Entry<ISampler, Integer> entry, Map.Entry<ISampler, Integer> entry1)
        {
            // Return 0 for a match, -1 for less than and +1 for more then
            return (entry.getValue().equals(entry1.getValue()) ? 0 : (entry.getValue() > entry1.getValue() ? 1 : -1));
        }
    });

    // Clear the map
    map.clear();

    // Copy back the entries now in order
    for (Map.Entry<ISampler, Integer> entry: list)
    {
        map.put(entry.getKey(), entry.getValue());
    } 
		
	}
	*/
	
	public void addSampler(ISampler des) {
	//protected void addDataExtractionStrategy(ComponentContext context) {
		System.out.println("Running SamplerDecider.addSampler()...");
		/*ISampler[] deStrategies = (ISampler[])context.locateServices("org.inqle.data.sampling.ISampler");
		for (ISampler des: deStrategies) {*/
		System.out.println("Registering Sampler " + des.getClass());
		Dictionary<?, ?> properties = des.getProperties();
		if (properties == null) {
			//log.error("Unable to add ISampler " + des + " as it does not have any properties.  At a minimum, should have property '" + IStrategy.PROPERTY_WEIGHT + "'.");
			return;
		}
		int weight = 1;
		try {
			Integer weightInt = Integer.parseInt((String)des.getProperties().get(ISampler.PROPERTY_WEIGHT));
			weight = weightInt.intValue();
		} catch (NumberFormatException e) {
			//default weight is 1
			//log.warn("ISampler of class " + des.getClass() + " does not have a recognizable numeric value for attribute " + IStrategy.PROPERTY_WEIGHT + "  Assigning it a value of 1");
			weight = 1;
		}
		if (weight > 0) {
			totalWeight += weight;
			
			normalPriorityDES.put(des, new Integer(totalWeight));
		} else {
			topPriorityDES.put(des, new Integer(weight));
		}
	}
	
	public int countSamplers() {
		return normalPriorityDES.size() + topPriorityDES.size();
	}
	
	public void removeSampler(ISampler des) {
		normalPriorityDES.remove(des);
		topPriorityDES.remove(des);
	}
	
	/*
	protected void addComponentFactory(ComponentFactory factory) {
		System.out.println("Running addComponentFactory()...");
	}*/
	
	protected void activate(ComponentContext context ) {
		System.out.println("Running SamplerDecider.activate()...");
	}
}
