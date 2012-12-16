package org.inqle.qa.gdata;

import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class LocalizedText {

	private ArrayList<String> lang = new ArrayList<String>();
	private ArrayList<String> text = new ArrayList<String>();
	
}
