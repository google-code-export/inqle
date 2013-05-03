package com.beyobe.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.beyobe.client.beans.Choice;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.MRadioButton;

public class ChoicePicker extends Composite implements TapHandler, ValueChangeHandler<Boolean>, HasValueChangeHandlers<Choice>, HasValue<Choice> {

	public List<Choice> choices;
	public List<MRadioButton> radioButtons;
	
	public ChoicePicker(List<Choice> choices) {
		this.choices = choices;
		radioButtons = new ArrayList<MRadioButton>();
		FlowPanel panel = new FlowPanel();
		for (Choice choice: choices) {
			MRadioButton button = new MRadioButton(choice.getLongForm());
			button.addTapHandler(this);
			button.addValueChangeHandler(this);
			radioButtons.add(button);
			FlowPanel choicePanel = new FlowPanel();
			button.getElement().getStyle().setProperty("float", "left");
			choicePanel.add(button);
			Label label = new Label(choice.getLongForm() + " (" + choice.getShortForm() + ")");
			label.getElement().getStyle().setProperty("float", "left");
			choicePanel.add(label);
			panel.add(choicePanel);
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
	
	public Long getSelectedId() {
		Choice selectedChoice = getSelectedChoice();
		if (selectedChoice==null) return null;
		return selectedChoice.getId();
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
		for (int i = 0; i<radioButtons.size(); i++) {
			MRadioButton button = radioButtons.get(i);
			if (button.equals(clickedButton)) {
				button.setValue(true);
			} else {
				button.setValue(false);
			}
		}
		
	}

	public void setSelectedId(long selectedId) {
		for (int i = 0; i<choices.size(); i++) {
			Choice choice = choices.get(i);
			MRadioButton button = radioButtons.get(i);
			if (choice.getId()==selectedId) {
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

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		ValueChangeEvent.fire(this, getValue());
	}

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
