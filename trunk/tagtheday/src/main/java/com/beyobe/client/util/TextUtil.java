package com.beyobe.client.util;

public class TextUtil {

	public static String shortenText(String text, int maxLength) {
		if (text==null) return "";
		if (text.length() < maxLength) {
			return text;
		}
		return text.substring(0, maxLength-3) + "...";
	}

}
