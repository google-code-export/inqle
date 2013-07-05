package com.beyobe.client.util;

import java.util.logging.Logger;

import com.google.gwt.regexp.shared.RegExp;

public class Validator {

	private static Logger log = Logger.getLogger("Validator");
	
	public static boolean isValidEmail(String email) {
//		if (email == null) return false;
//		RegExp regExp = RegExp.compile("^[\\a0-9._%+-]+@[\\a0-9.-]+\\.[\\a]{2,4}$");
//		return regExp.test(email);
		return true;
	}
	
	public static void main(String[] args) {
		RegExp regExp = RegExp.compile("\\a*");
		String email = "aaaa";
		log.info("email: " + email + " is valid? " + regExp.test(email));
	}

}
