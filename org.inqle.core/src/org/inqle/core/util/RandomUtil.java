package org.inqle.core.util;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class RandomUtil {
	
	/**
	 * Returns a Set of 'count' random integers from 1 to 'max'
	 * @param count the number of integers to populate the returnset with
	 * @param max the maximum random number
	 * @return returnset the Set of random numbers
	 */
	public static Set getRandomNumberSet(int count, int max) {
		TreeSet<Integer> returnset = new TreeSet<Integer>();
		if (count > max) return returnset;
		
		int counter = 0;
		//loop until the returnset has count members.  loop a maximum of max times
		while (returnset.size() < count && counter < max) {
			counter++;
			Random r = new Random();
			int randint = r.nextInt(max);
			returnset.add(randint);
		}
		return returnset;
	}
	
	/**
	 * Returns a random number between start and end, inclusive
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getRandomInt(int start, int end) {
		int span = end-start;
		Random r = new Random();
		int randint = r.nextInt(span + 1);
		return start + randint;
	}
}
