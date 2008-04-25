package org.inqle.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomListChooser {

	/**
	 * 
	 * @param origList the original list, from which to select
	 * @param minSize the minimum number of entries to select
	 * @param maxSize the maximum number of entries to select
	 * @return randomly chosen list of entries
	 */
	public static List<?> chooseRandomItems(List<?> origList, int minSize, int maxSize) {
		List<Object> tempList = new ArrayList<Object>();
		tempList.addAll(origList);
		List<Object> newList = new ArrayList<Object>();
		
		int difference = maxSize - minSize;
		int targetSize = minSize;
		if (difference >= 1) {
			Random random = new Random();
			int randomDiff = random.nextInt(difference);
			targetSize += randomDiff;
		}
		
		if (targetSize > origList.size()) {
			targetSize = origList.size();
		}
		while (newList.size() < targetSize) {
			Random random = new Random();
			int randomIndex = random.nextInt(tempList.size());
			Object selectedObject = tempList.get(randomIndex);
			newList.add(selectedObject);
			tempList.remove(randomIndex);
		}
		return newList;
	}
	
	public static int chooseRandomIndex(int size) {
		Random random = new Random();
		int randomIndex = random.nextInt(size);
		return randomIndex;
	}
}
