package com.beyobe.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.beyobe.client.beans.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.MRadioButton;

public class UnitPicker extends Composite implements TapHandler {

	public List<Unit> units;
	public List<MRadioButton> radioButtons;
	
	public UnitPicker(Unit baseUnit) {
		units = getApplicableUnits(baseUnit);
		radioButtons = new ArrayList<MRadioButton>();
		FlowPanel panel = new FlowPanel();
		for (Unit unit: units) {
			FlowPanel choicePanel = new FlowPanel();
//			choicePanel.getElement().getStyle().setProperty("float", "left");
			MRadioButton button = new MRadioButton(unit.getAbbrev());
			button.addTapHandler(this);
			radioButtons.add(button);
			button.getElement().getStyle().setProperty("float", "left");
			choicePanel.add(button);
			Label label = new Label(unit.getAbbrev());
			choicePanel.add(label);
			panel.add(choicePanel);
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

	@Override
	public void onTap(TapEvent event) {
		MRadioButton clickedButton = (MRadioButton)event.getSource();
		for (int i = 0; i<radioButtons.size(); i++) {
			MRadioButton button = radioButtons.get(i);
			if (button.equals(clickedButton)) {
				button.setValue(true);
			} else {
				button.setValue(false);
			}
		}
		
	}
}
