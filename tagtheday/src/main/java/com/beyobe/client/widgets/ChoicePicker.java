package com.beyobe.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.beyobe.client.beans.Choice;
import com.beyobe.client.beans.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.googlecode.mgwt.ui.client.widget.MRadioButton;

public class ChoicePicker extends Composite {

	public List<Choice> choices;
	public List<MRadioButton> radioButtons;
	
	public ChoicePicker(List<Choice> choices) {
		this.choices = choices;
		radioButtons = new ArrayList<MRadioButton>();
		FlowPanel panel = new FlowPanel();
		for (Choice choice: choices) {
			MRadioButton button = new MRadioButton(choice.getLongForm());
			radioButtons.add(button);
			panel.add(button);
		}
		initWidget(panel);
	}
	
	public Choice getSelectedChoice() {
		for (int i = 0; i<radioButtons.size(); i++) {
			MRadioButton button = radioButtons.get(i);
			if (button.getValue()) {
				return choices.get(i);
			}
		}
		return null;
	}
	
	public void setSelectedChoice(Choice selectedChoice) {
		for (int i = 0; i<choices.size(); i++) {
			Choice choice = choices.get(i);
			MRadioButton button = radioButtons.get(i);
			if (choice.equals(selectedChoice)) {
				button.setValue(true);
			} else {
				button.setValue(false);
			}
		}
	}
}
