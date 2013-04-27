package com.beyobe.client.beans;



public enum Unit {
	KG(null, 1.0, "kg"), LB(KG, 0.453592, "lb"), CM(null, 1.0, "cm"), INCH(CM, 2.54, "in");
	
	private Unit referenceUnit;
	private double multiplier;
	private String abbrev;
	
	public String getAbbrev() {
		return abbrev;
	}

	Unit(Unit referenceUnit, double multiplier, String abbrev) {
		this.referenceUnit = referenceUnit;
		this.multiplier = multiplier;
		this.abbrev = abbrev;
	}
	
	public double toReferenceValue(double value) {
		return value * multiplier;
	}

	public Unit getReferenceUnit() {
		if (referenceUnit==null) return this;
		return referenceUnit;
	}
}
