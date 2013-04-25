package com.beyobe.client.beans;



public enum Unit {
	KG(null, 1.0), LB(KG, 0.453592), CM(null, 1.0), INCH(CM, 2.54);
	
	private Unit referenceUnit;
	private double multiplier;
	
	Unit(Unit referenceUnit, double multiplier) {
		this.referenceUnit = referenceUnit;
		this.multiplier = multiplier;
	}
	
	public double toReferenceValue(double value) {
		return value * multiplier;
	}

	public Unit getReferenceUnit() {
		if (referenceUnit==null) return this;
		return referenceUnit;
	}
}
