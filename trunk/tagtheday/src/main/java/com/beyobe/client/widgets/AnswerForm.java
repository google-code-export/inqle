package com.beyobe.client.widgets;

import java.util.Date;

import com.beyobe.client.App;
import com.beyobe.client.beans.Choice;
import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Question;
import com.beyobe.client.event.DataCapturedEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.MDoubleBox;
import com.googlecode.mgwt.ui.client.widget.MIntegerBox;
import com.googlecode.mgwt.ui.client.widget.MSlider;
import com.googlecode.mgwt.ui.client.widget.MTextArea;
import com.googlecode.mgwt.ui.client.widget.MTextBox;

public class AnswerForm extends Composite implements TapHandler {

	private Question q;
	private Datum d;
	private MDoubleBox doubleBox;
	private MIntegerBox integerBox;
	private UnitPicker unitPicker;
	private Button saveButton;
	private MTextBox textBox;
	private MTextArea textArea;
	private ChoicePicker choicePicker;
	private TagButton tagButton;
	private MSlider slider;

	public AnswerForm(TagButton tagButton) {
		this.tagButton = tagButton;
		if (tagButton.getQuestion() == null) return;
		this.q = tagButton.getQuestion();
		this.d = tagButton.getDatum();
		
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		panel.setHeight("100%");
		Label questionFull = new Label(q.getLongForm());
		panel.add(questionFull);
		
		//add input elements
		
		//DOUBLE
		if (q.getDataType()==Question.DATA_TYPE_DOUBLE) {
			doubleBox = new MDoubleBox();
			panel.add(doubleBox);
			if (q.getReferenceUnit() != null) {
				unitPicker = new UnitPicker(q.getReferenceUnit());
				if (d != null) unitPicker.setSelectedUnit(d.getUnit());
				panel.add(unitPicker);
			}
			if (d != null) doubleBox.setText(d.getTextValue());
		}
		
		//INTEGER
		if (q.getDataType()==Question.DATA_TYPE_INTEGER) {
			if (q.getMinValue()==0 && q.getMaxValue() != null && q.getMaxValue() > 0) {
				slider = new MSlider();
				slider.setMax((int)Math.round(q.getMaxValue()));
				if (d != null) slider.setValue(d.getIntegerValue());
			} else {
				integerBox = new MIntegerBox();
				panel.add(integerBox);
				if (q.getReferenceUnit() != null) {
					unitPicker = new UnitPicker(q.getReferenceUnit());
					panel.add(unitPicker);
				}
				if (d != null) integerBox.setText(d.getTextValue());
			}
		}
		
		//SHORT TEXT
		if (q.getDataType()==Question.DATA_TYPE_SHORT_TEXT) {
			textBox = new MTextBox();
			panel.add(textBox);
			if (d != null) textBox.setText(d.getTextValue());
		}
		
		//LONG TEXT
		if (q.getDataType()==Question.DATA_TYPE_LONG_TEXT) {
			textArea = new MTextArea();
			panel.add(textArea);
			if (d != null) textArea.setText(d.getTextValue());
		}
		
		//MULTIPLE CHOICE
		if (q.getDataType()==Question.DATA_TYPE_MULTIPLE_CHOICE) {
			choicePicker = new ChoicePicker(q.getChoices());
			panel.add(choicePicker);
			if (d != null) choicePicker.setSelectedChoice(d.getChoice());
		}
		
		saveButton = new Button("Save");
		saveButton.setSmall(true);
		saveButton.addTapHandler(this);
		panel.add(saveButton);
		initWidget(panel);
	}

	@Override
	public void onTap(TapEvent event) {
		if (saveButton.equals(event.getSource())) {
			saveData();
		}
	}
	
	public boolean saveData() {
		if (d==null) {
			d = new Datum();
			d.setEffectiveDate(tagButton.getEffectiveDate());
		}
		d.setParticipantId(App.participant.getId());
		d.setQuestionId(q.getId());
		d.setConceptId(q.getConceptId());
		d.setDataType(q.getDataType());
		d.setStatus(Datum.STATUS_ANSWERED_PERSONALLY);
		d.setUpdated(new Date());
		
		//DOUBLE
		if (q.getDataType()==Question.DATA_TYPE_DOUBLE) {
			Double val;
			try {
				val = Double.valueOf(doubleBox.getText());
			} catch (NumberFormatException e) {
				validateMessage("Unable to recognize your answer.  It should be a number.");
				return false;
			}
			if (q.getMinValue() != null && q.getMinValue() > val) {
				validateMessage("Your answer must be at least " + q.getMinValue());
				return false;
			}
			if (q.getMaxValue() != null && q.getMaxValue() < val) {
				validateMessage("Your answer must be no more than " + q.getMaxValue());
				return false;
			}
			d.setTextValue(doubleBox.getText());
			d.setLongTextValue(doubleBox.getText());
			
			d.setNumericValue(val);
			if (q.getReferenceUnit() != null) {
				d.setUnit(unitPicker.getSelectedUnit());
				double normalizedValue = unitPicker.getSelectedUnit().toReferenceValue(val);
				d.setNormalizedValue(normalizedValue);
			} else {
				d.setNormalizedValue(val);
			}
		}
		
		//INTEGER
		if (q.getDataType()==Question.DATA_TYPE_INTEGER) {
			Integer val = null;
			
			//Slider
			if (q.getMinValue()==0 && q.getMaxValue() != null && q.getMaxValue() > 0) {
				val = slider.getValue();
				d.setTextValue(String.valueOf(val));
				d.setLongTextValue(String.valueOf(val));
			} else {
				try {
					val = Integer.valueOf(integerBox.getText());
				} catch (NumberFormatException e) {
					validateMessage("Unable to recognize your answer.  It should be an integer.");
					return false;
				}
				if (q.getMinValue() != null && q.getMinValue() > val) {
					validateMessage("Your answer must be at least " + q.getMinValue());
					return false;
				}
				if (q.getMaxValue() != null && q.getMaxValue() < val) {
					validateMessage("Your answer must be no more than " + q.getMaxValue());
					return false;
				}
				d.setTextValue(integerBox.getText());
				d.setLongTextValue(integerBox.getText());
				
				d.setIntegerValue(val);
			}
			
			//handle units if present
			if (q.getReferenceUnit() != null) {
				d.setUnit(unitPicker.getSelectedUnit());
				double normalizedValue = unitPicker.getSelectedUnit().toReferenceValue(val);
				d.setNormalizedValue(normalizedValue);
			} else {
				d.setNormalizedValue((double)val);
			}
		}
		
		//SHORT TEXT
		if (q.getDataType()==Question.DATA_TYPE_SHORT_TEXT) {
			d.setTextValue(textBox.getText());
			d.setLongTextValue(textBox.getText());
		}
		
		//LONG TEXT
		if (q.getDataType()==Question.DATA_TYPE_LONG_TEXT) {
			d.setTextValue(textArea.getText());
			d.setLongTextValue(textArea.getText());
		}
		
		if (q.getDataType()==Question.DATA_TYPE_MULTIPLE_CHOICE) {
			Choice choice = choicePicker.getSelectedChoice();
			if (choice == null) {
				validateMessage("No choice selected");
			} else {
				d.setTextValue(choice.getShortForm());
				d.setLongTextValue(choice.getLongForm());
				d.setChoice(choice);
			}
		}
		tagButton.setDatum(d);
		App.eventBus.fireEvent(new DataCapturedEvent(tagButton));
		return true;
	}

	private void validateMessage(String message) {
		Window.alert(message);
	}
	
	

}
