package com.beyobe.client.beans;



public enum Unit {
	KG(UnitSystem.METRIC, null, 1.0, "kg"), 
	LB(UnitSystem.ENGLISH, KG, 0.453592, "lb"), 
	CM(UnitSystem.METRIC, null, 1.0, "cm"), 
	INCH(UnitSystem.ENGLISH, CM, 2.54, "in"), 
	KM(UnitSystem.METRIC, null, 1.0, "km"), 
	MILE(UnitSystem.ENGLISH, KM, 0.621371, "mi"),
	GM(UnitSystem.METRIC, null, 1.0, "gm"),
	OZ(UnitSystem.ENGLISH, GM, 0.035274, "oz"),
	ML(UnitSystem.METRIC, null, 1.0, "ml"),
	FLOZ(UnitSystem.ENGLISH, ML, 0.035274, "oz"),
	L(UnitSystem.METRIC, null, 1.0, "L"),
	GAL(UnitSystem.ENGLISH, L, 0.264172, "gal");
	
	private Unit referenceUnit;
	private double multiplier;
	private String abbrev;
	private UnitSystem unitSystem;
	
	public String getAbbrev() {
		return abbrev;
	}

	Unit(UnitSystem unitSystem, Unit referenceUnit, double multiplier, String abbrev) {
		this.setUnitSystem(unitSystem);
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

	public UnitSystem getUnitSystem() {
		return unitSystem;
	}

	public void setUnitSystem(UnitSystem unitSystem) {
		this.unitSystem = unitSystem;
	}
}
