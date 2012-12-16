package org.inqle.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public class RandomListChooser {

	/**
	 * 
	 * @param origList the original list, from which to select
	 * @param minSize the minimum number of entries to select
	 * @param maxSize the maximum number of entries to select
	 * @return randomly chosen list of entries
	 */
	public static <T> List<T> chooseRandomItemsSubtractively(List<T> origList, int size) {
		List<T> tempList = new ArrayList<T>();
		tempList.addAll(origList);
		List<T> newList = new ArrayList<T>();
		
		while (newList.size() < size) {
			Random random = new Random();
			int randomIndex = random.nextInt(tempList.size());
			T selectedObject = tempList.get(randomIndex);
			newList.add(selectedObject);
			tempList.remove(randomIndex);
		}
		return newList;
	}
	
	public static int getRandomNumber(int minSize, int maxSize) {
		int difference = maxSize - minSize;
		int targetSize = minSize;
		if (difference >= 1) {
			Random random = new Random();
			int randomDiff = random.nextInt(difference);
			targetSize += randomDiff;
		}
		return targetSize;
	}

	/**
	 * Selects a small collection, from a large collection.  Avoids adding any of the items specified in the 
	 * itemsToExclude.  This will not add the same element twice to the collection.
	 * @param itemsToSelectFrom
	 * @param itemsToExclude
	 * @param size the number of items to select
	 * @return the randomly selected collection of items
	 */
	public static Collection<?> chooseRandomItemsAdditively(Collection<?> itemsToSelectFrom, Collection<?> itemsToExclude, int size) {
		if (itemsToSelectFrom == null) return null;
		List<Object> selectedItems = new ArrayList<Object>();
		if (itemsToSelectFrom.size()==0) return selectedItems;
		
		List<Object> listToSelectFrom = new ArrayList<Object>(itemsToSelectFrom);
		int maxNumTries = itemsToSelectFrom.size() * 2;
		List<Integer> triedIndexes = new ArrayList<Integer>();
		int numTries = 0;
		while (selectedItems.size() < size && numTries <= maxNumTries) {
			numTries++;
			Random random = new Random();
			int randomIndex = random.nextInt(itemsToSelectFrom.size());
			if (triedIndexes.contains(randomIndex)) continue;
			triedIndexes.add(randomIndex);
			Object selectedObject = listToSelectFrom.get(randomIndex);
			if (itemsToExclude != null && itemsToExclude.contains(selectedObject)) continue;
			selectedItems.add(selectedObject);
		} 
		return selectedItems;
	}
	
	public static int chooseRandomIndex(int size) {
		Random random = new Random();
		int randomIndex = random.nextInt(size);
		return randomIndex;
	}
}
