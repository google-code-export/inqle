package com.beyobe.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.beyobe.client.beans.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.googlecode.mgwt.ui.client.widget.MRadioButton;

public class UnitPicker extends Composite {

	public List<Unit> units;
	public List<MRadioButton> radioButtons;
	
	public UnitPicker(Unit baseUnit) {
		units = getApplicableUnits(baseUnit);
		radioButtons = new ArrayList<MRadioButton>();
		FlowPanel panel = new FlowPanel();
		for (Unit unit: units) {
			MRadioButton button = new MRadioButton(unit.getAbbrev());
			radioButtons.add(button);
			panel.add(button);
		}
		initWidget(panel);
	}
	
	public static List<Unit> getApplicableUnits(Unit baseUnit) {
		List<Unit> units = new ArrayList<Unit>();
		for (Unit unit: Unit.values()) {
			if (unit.getReferenceUnit().equals(baseUnit)) {
				units.add(unit);
			}
		}
		return units;
	}
	
	public Unit getSelectedUnit() {
		for (int i = 0; i<radioButtons.size(); i++) {
			MRadioButton button = radioButtons.get(i);
			if (button.getValue()) {
				return units.get(i);
			}
		}
		return null;
	}
	
	public void setSelectedUnit(Unit selectedUnit) {
		for (int i = 0; i<units.size(); i++) {
			Unit unit = units.get(i);
			MRadioButton button = radioButtons.get(i);
			if (unit.equals(selectedUnit)) {
				button.setValue(true);
			} else {
				button.setValue(false);
			}
		}
	}
}
