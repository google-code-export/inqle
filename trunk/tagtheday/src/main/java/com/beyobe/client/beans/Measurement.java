package com.beyobe.client.beans;

import java.util.ArrayList;
import java.util.List;



public enum Measurement {
	NONE("None", null),
	LENGTH("Length", Unit.CM),
	DISTANCE("Distance", Unit.KM),
	LIGHT_WEIGHT("Light Weight", Unit.GM),
	WEIGHT("Weight", Unit.KG),
	SMALL_VOLUME("Small Volume", Unit.ML),
	VOLUME("Volume", Unit.L),
	DURATION("Duration", Unit.SEC)
	;
	
	private Unit referenceUnit;
	private String label;
	
	public String getLabel() {
		return label;
	}

	Measurement(String label, Unit referenceUnit) {
		this.setReferenceUnit(referenceUnit);
		this.label = label;
	}

	public Unit getReferenceUnit() {
		return referenceUnit;
	}

	public void setReferenceUnit(Unit referenceUnit) {
		this.referenceUnit = referenceUnit;
	}

	public List<Unit> getUnits() {
		List<Unit> units = new ArrayList<Unit>();
		if (referenceUnit==null) return units;
//		units.add(referenceUnit);
		for (Unit unit: Unit.values()) {
			if (referenceUnit.equals(unit.getReferenceUnit())) {
				units.add(unit);
			}
		}
		return units;
	}

//	public static Measurement getMeasurement(Long selectedId) {
//		if (selectedId==null) return null;
//		for (Measurement m: Measurement.values()) {
//			if (selectedId == m.getId()) return m;
//		}
//		return null;
//	}
	
}
