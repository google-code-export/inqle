package com.beyobe.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.beyobe.client.beans.Choice;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.MRadioButton;

public class ChoicePicker extends Composite implements TapHandler, HasValueChangeHandlers<Choice>, HasValue<Choice> {

	public List<Choice> choices;
	public List<MRadioButton> radioButtons;	

	public ChoicePicker(List<Choice> choices, int columns) {
		this.choices = choices;
		radioButtons = new ArrayList<MRadioButton>();
		FlowPanel panel = new FlowPanel();
		int i=0;
		int columnsCreated = 1;
		VerticalPanel columnPanel = new VerticalPanel();
		columnPanel.getElement().getStyle().setProperty("float", "left");
		panel.add(columnPanel);
		for (Choice choice: choices) {
			i++;
			if (columnsCreated < columns && i > columnsCreated * choices.size() / columns) {
				columnPanel = new VerticalPanel();
				columnPanel.getElement().getStyle().setProperty("float", "left");
				panel.add(columnPanel);
				columnsCreated++;
			}
			MRadioButton button = new MRadioButton(choice.getDescription());
			button.addTapHandler(this);
			radioButtons.add(button);
			FlowPanel choicePanel = new FlowPanel();
			button.getElement().getStyle().setProperty("float", "left");
			choicePanel.add(button);
			String labelText = choice.getText();
			if (choice.getDescription() != null && !(choice.getDescription().equals(choice.getText()))) {
				labelText += " (" + choice.getDescription() + ")";
			}
			Label label = new Label(labelText);
			label.addStyleName("ttd-choice");
			label.getElement().getStyle().setProperty("float", "left");
			choicePanel.add(label);
			columnPanel.add(choicePanel);
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
	
	public int getSelectedIndex() {
		for (int i = 0; i<radioButtons.size(); i++) {
			MRadioButton button = radioButtons.get(i);
			if (button.getValue()) {
				return i;
			}
		}
		return -1;
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
	
	@Override
	public void onTap(TapEvent event) {
		MRadioButton clickedButton = (MRadioButton)event.getSource();
		//if the value did not change then do nothing
		if (clickedButton.getValue()) return;
		for (int i = 0; i<radioButtons.size(); i++) {
			MRadioButton button = radioButtons.get(i);
			if (button.equals(clickedButton)) {
				button.setValue(true);
			} else {
				button.setValue(false);
			}
		}
		
		ValueChangeEvent.fire(this, getValue());
	}

	public void setSelectedIndex(int selectedIndex) {
		for (int i = 0; i<choices.size(); i++) {
			MRadioButton button = radioButtons.get(i);
			if (i==selectedIndex) {
				button.setValue(true);
			} else {
				button.setValue(false);
			}
		}
		
	}


	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Choice> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

//	@Override
//	public void onValueChange(ValueChangeEvent<Boolean> event) {
//		Window.alert("Value changed");
//		ValueChangeEvent.fire(this, getValue());
//	}

	@Override
	public Choice getValue() {
		return getSelectedChoice();
	}

	@Override
	public void setValue(Choice value) {
		setSelectedChoice(value);
	}

	@Override
	public void setValue(Choice value, boolean fireEvents) {
		Choice oldValue = getValue();
		setSelectedChoice(value);
		
		if (value.equals(oldValue)) {
			return;
		}
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}
}
