package com.beyobe.client.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.beyobe.client.App;
import com.beyobe.client.beans.AnswerStatus;
import com.beyobe.client.beans.Choice;
import com.beyobe.client.beans.DataType;
import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Question;
import com.beyobe.client.data.BeanMaker;
import com.beyobe.client.event.DataCapturedEvent;
import com.beyobe.client.util.UUID;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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

public class AnswerForm extends Composite implements TapHandler, ValueChangeHandler<Choice> {

	public static final int MAXIMUM_LENGTH_SHORT_TEXT = 12;
	public static final int MAXIMUM_LENGTH_LONG_TEXT = 1000;
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
	private ChoicePicker pastAnswersPicker;

	public AnswerForm(TagButton tagButton) {
		this.tagButton = tagButton;
		if (tagButton.getQuestion() == null) return;
		this.q = tagButton.getQuestion();
		this.d = tagButton.getDatum();
		
		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		panel.setHeight("100%");
		Label questionFull = new Label(q.getLongForm());
//		questionFull.getElement().getStyle().setFontSize(0.7, Unit.EM);
		panel.add(questionFull);
		
		//add input elements
		
		//DOUBLE
		if (q.getDataType()==DataType.DOUBLE) {
			doubleBox = new MDoubleBox();
			panel.add(doubleBox);
			if (q.getMeasurement() != null) {
				unitPicker = new UnitPicker(q.getMeasurement());
				if (d != null) unitPicker.setSelectedUnit(d.getUnit());
				panel.add(unitPicker);
			}
			if (d != null) doubleBox.setText(d.getTextValue());
		}
		
		//INTEGER
		if (q.getDataType()==DataType.INTEGER) {
			if (q.getMinValue()==0 && q.getMaxValue() != null && q.getMaxValue() > 0) {
				slider = new MSlider();
				slider.setMax((int)Math.round(q.getMaxValue()));
				if (d != null) slider.setValue(d.getIntegerValue());
			} else {
				integerBox = new MIntegerBox();
				panel.add(integerBox);
				if (q.getMeasurement() != null) {
					unitPicker = new UnitPicker(q.getMeasurement());
					panel.add(unitPicker);
				}
				if (d != null) integerBox.setText(d.getTextValue());
			}
		}
		
		//SHORT TEXT
		if (q.getDataType()==DataType.SHORT_TEXT) {
			List<String> pastAnswers = App.dataBus.getPastAnswers(q);
			List<Choice> choices = new ArrayList<Choice>();
			for (String pastAnswer: pastAnswers) {
				Choice c  = new Choice(pastAnswer, pastAnswer);
				choices.add(c);
			}
			if (choices.size() > 3) {
				pastAnswersPicker = new ChoicePicker(choices, 2);
				pastAnswersPicker.addValueChangeHandler(this);
				panel.add(pastAnswersPicker);
			} else if (choices.size() > 0) {
				pastAnswersPicker = new ChoicePicker(choices, 1);
				pastAnswersPicker.addValueChangeHandler(this);
				panel.add(pastAnswersPicker);
			}
			textBox = new MTextBox();
			textBox.setMaxLength(q.getMaxLength());
			panel.add(textBox);
			if (d != null) textBox.setText(d.getTextValue());
		}
		
		//LONG TEXT
		if (q.getDataType()==DataType.LONG_TEXT) {
			textArea = new MTextArea();
			textArea.setCharacterWidth(30);
			textArea.setVisibleLines(3);
			panel.add(textArea);
			if (d != null) textArea.setText(d.getTextValue());
		}
		
		//MULTIPLE CHOICE
		if (q.getDataType()==DataType.MULTIPLE_CHOICE) {
			if (q.getChoices() != null) {
				if (q.getChoices().size() > 3) {
					choicePicker = new ChoicePicker(q.getChoices(), 2);
				} else {
					choicePicker = new ChoicePicker(q.getChoices(), 1);
				}
				choicePicker.setSelectedIndex(0);
				panel.add(choicePicker);
			}
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
//			d = new Datum();
			d = BeanMaker.makeDatum();
			d.setId(UUID.uuid());
			d.setEffectiveDate(tagButton.getEffectiveDate());
			d.setParticipantId(App.participant.getId());
			d.setQuestionId(q.getId());
		}
		d.setQuestionConcept(q.getQuestionConcept());
		d.setDataType(q.getDataType());
		d.setAnswerStatus(AnswerStatus.ANSWERED_PERSONALLY);
		d.setUpdated(new Date());
		
		//DOUBLE
		if (q.getDataType()==DataType.DOUBLE) {
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
			d.setTextValue(getShortenedText(doubleBox.getText()));
			
			d.setNumericValue(val);
			if (q.getMeasurement() != null) {
				d.setUnit(unitPicker.getSelectedUnit());
				double normalizedValue = unitPicker.getSelectedUnit().toReferenceValue(val);
				d.setNormalizedValue(normalizedValue);
			} else {
				d.setNormalizedValue(val);
			}
		}
		
		//INTEGER
		if (q.getDataType()==DataType.INTEGER) {
			Integer val = null;
			
			//Slider
			if (q.getMinValue()==0 && q.getMaxValue() != null && q.getMaxValue() > 0) {
				val = slider.getValue();
				d.setTextValue(getShortenedText(String.valueOf(val)));
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
				d.setTextValue(getShortenedText(integerBox.getText()));
				d.setNumericValue(val.doubleValue());
				d.setIntegerValue(val);
			}
			
			//handle units if present
			if (q.getMeasurement() != null) {
				d.setUnit(unitPicker.getSelectedUnit());
				double normalizedValue = unitPicker.getSelectedUnit().toReferenceValue(val);
				d.setNormalizedValue(normalizedValue);
			} else {
				d.setNormalizedValue((double)val);
			}
		}
		
		//SHORT TEXT
		if (q.getDataType()==DataType.SHORT_TEXT) {
			d.setTextValue(getShortenedText(textBox.getText()));
		}
		
		//LONG TEXT
		if (q.getDataType()==DataType.LONG_TEXT) {
			String memo = textArea.getText();
			if (memo.length() > q.getMaxLength()) {
				validateMessage("Your answer must be less than " + q.getMaxLength() + " characters.");
				return false;
			}
			d.setTextValue(getShortenedText(memo));
		}
		
		if (q.getDataType()==DataType.MULTIPLE_CHOICE) {
			Choice choice = choicePicker.getSelectedChoice();
			if (choice == null) {
				validateMessage("No choice selected");
			} else {
				d.setTextValue(getShortenedText(choice.getText()));
				d.setChoice(choice);
			}
		}
		tagButton.setDatum(d);
		App.eventBus.fireEvent(new DataCapturedEvent(tagButton));
		return true;
	}

//	private String getLongText(String text) {
//		if (text==null) return "";
//		return text.substring(0, MAXIMUM_LENGTH_LONG_TEXT);
//	}

	private String getShortenedText(String text) {
		if (text==null) return "";
		if (text.length() < q.getMaxLength()) {
			return text;
		}
		return text.substring(0, q.getMaxLength()-3) + "...";
	}

	private void validateMessage(String message) {
		Window.alert(message);
	}

	@Override
	public void onValueChange(ValueChangeEvent<Choice> event) {
		String selection = pastAnswersPicker.getSelectedChoice().getDescription();
		textBox.setText(selection);
	}
	
	

}
