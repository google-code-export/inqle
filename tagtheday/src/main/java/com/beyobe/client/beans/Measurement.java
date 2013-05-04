package com.beyobe.client.beans;

import java.util.ArrayList;
import java.util.List;



public enum Measurement {
	LENGTH(1, "Short Length", Unit.CM),
	DISTANCE(2, "Distance", Unit.KM),
	LIGHT_WEIGHT(3, "Light Weight", Unit.GM),
	WEIGHT(4, "Weight", Unit.KG),
	SMALL_VOLUME(5, "Small Volume", Unit.ML),
	VOLUME(6, "Volume", Unit.L),
	DURATION(7, "Duration", Unit.SEC),
	OTHER(100, "Other", null)
	;
	
	private Unit referenceUnit;
	private int id;
	private String label;
	
	public String getLabel() {
		return label;
	}

	Measurement(int id, String label, Unit referenceUnit) {
		this.setReferenceUnit(referenceUnit);
		this.setId(id);
		this.label = label;
	}

	public Unit getReferenceUnit() {
		return referenceUnit;
	}

	public void setReferenceUnit(Unit referenceUnit) {
		this.referenceUnit = referenceUnit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Unit> getUnits() {
		List<Unit> units = new ArrayList<Unit>();
		units.add(referenceUnit);
		for (Unit unit: Unit.values()) {
			if (referenceUnit.equals(unit.getReferenceUnit())) {
				units.add(unit);
			}
		}
		return units;
	}

	public static Measurement getMeasurement(Long selectedId) {
		if (selectedId==null) return null;
		for (Measurement m: Measurement.values()) {
			if (selectedId == m.getId()) return m;
		}
		return null;
	}
	
}
