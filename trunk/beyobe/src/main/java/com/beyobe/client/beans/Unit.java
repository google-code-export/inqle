package com.beyobe.client.beans;



public enum Unit {
	KG(UnitSystem.METRIC, null, 1.0, "kilograms", "kg"), 
	LB(UnitSystem.ENGLISH, KG, 0.453592, "pounds", "lb"), 
	CM(UnitSystem.METRIC, null, 1.0, "centimeters", "cm"), 
	INCH(UnitSystem.ENGLISH, CM, 2.54, "inches", "in"), 
	KM(UnitSystem.METRIC, null, 1.0, "kilometers", "km"), 
	MILE(UnitSystem.ENGLISH, KM, 0.621371, "miles", "mi"),
	GM(UnitSystem.METRIC, null, 1.0, "grams", "gm"),
	OZ(UnitSystem.ENGLISH, GM, 0.035274, "ounces", "oz"),
	ML(UnitSystem.METRIC, null, 1.0, "milliliters", "ml"),
	FLOZ(UnitSystem.ENGLISH, ML, 0.035274, "fluid ounces", "oz"),
	L(UnitSystem.METRIC, null, 1.0, "liters", "L"),
	GAL(UnitSystem.ENGLISH, L, 0.264172, "gallons", "gal"), 
	SEC(UnitSystem.UNIVERSAL, null, 1.0, "seconds", "s"),
	MIN(UnitSystem.UNIVERSAL, SEC, 60.0, "minutes", "min"),
	HR(UnitSystem.UNIVERSAL, SEC, 3600.0, "hours", "hr"),
	;
	
	private Unit referenceUnit;
	private double multiplier;
	private String abbrev;
	private UnitSystem unitSystem;
	private String fullName;
	
	public String getAbbrev() {
		return abbrev;
	}

	Unit(UnitSystem unitSystem, Unit referenceUnit, double multiplier, String fullName, String abbrev) {
		this.setUnitSystem(unitSystem);
		this.referenceUnit = referenceUnit;
		this.multiplier = multiplier;
		this.fullName = fullName;
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
