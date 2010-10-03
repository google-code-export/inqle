package org.inqle.qa;



public interface PreferenceBroker {

	public void storePreference(Preference preference);
	
	public Preference getPreference(Object preferenceKey);
	
}
