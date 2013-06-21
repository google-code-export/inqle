package com.beyobe.client.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.beyobe.client.App;
import com.beyobe.client.Constants;
import com.beyobe.client.beans.Choice;
import com.beyobe.client.beans.DataType;
import com.beyobe.client.beans.Measurement;
import com.beyobe.client.beans.Question;
import com.beyobe.client.beans.Unit;
import com.beyobe.client.data.BeanMaker;
import com.beyobe.client.event.QuestionSavedEvent;
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
import com.googlecode.mgwt.ui.client.widget.MTextArea;
import com.googlecode.mgwt.ui.client.widget.MTextBox;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

public class QuestionForm extends Composite implements TapHandler, ValueChangeHandler<Choice> {

	private static final int LONG_FORM_MAX_LENGTH = 250;
	private static final int ABBREV_LENGTH = 10;
	private Question q;
//	private MTextBox shortForm;
	private MTextBox abbrev;
	private MTextArea longForm;
	private ChoicePicker dataTypePicker;
	private ChoicePicker measurmentPicker;
	private MDoubleBox minVal;
	private MDoubleBox maxVal;
	private MIntegerBox maxLength;
	private Button saveButton;
	private VerticalPanel numericParamsPanel;
	private MTextBox minBox;
	private MTextBox maxBox;
	private VerticalPanel maxLengthPanel;
	private ScrollPanel scrollPanel;
	
	public QuestionForm(Question q) {
		this.q = q;
		scrollPanel = new ScrollPanel();
		scrollPanel.setShowScrollBarX(false);
	    scrollPanel.setShowScrollBarY(true);
	    scrollPanel.setScrollingEnabledX(false);
	    scrollPanel.setScrollingEnabledY(true);
	    scrollPanel.setAutoHandleResize(true);
	    scrollPanel.setSnap(false);
	    scrollPanel.setBounce(false);
//	    scrollPanel.setUsePos(true);
		VerticalPanel panel = new VerticalPanel();
		scrollPanel.add(panel);
		panel.setWidth("100%");
		panel.setHeight("100%");
		Label questionFull = new Label("Add a Question");
		questionFull.addStyleName("ttd-form-header");
		panel.add(questionFull);
		
		//add input elements
		//long form
//		HTML spacer = new HTML("<br/>");
//		spacer.getElement().getStyle().setFontSize(0.7, Unit.EM);
//		panel.add(spacer);
		Label longLabel = new Label("Question (example: How happy were you?)");
		longLabel.addStyleName("ttd-form-label");
//		longLabel.getElement().getStyle().setFontSize(0.7, Unit.EM);
		panel.add(longLabel);
		longForm = new MTextArea();
		longForm.setAutoCorrectEnabled(true);
		longForm.setCharacterWidth(30);
		longForm.setVisibleLines(3);
		if (q != null) {
			longForm.setText(q.getLongForm());
		}
		panel.add(longForm);
			
//		//short form
//		panel.add(new Label("Abbreviated version of the question - Example: Happy?"));
//		shortForm = new MTextBox();
//		shortForm.setMaxLength(14);
//		panel.add(shortForm);
		
		//abbrev
//		panel.add(spacer);
		Label abbrevLabel = new Label("Short Label (example: Happy)");
		abbrevLabel.addStyleName("ttd-form-label");
		panel.add(abbrevLabel);
		abbrev = new MTextBox();
		abbrev.setMaxLength(ABBREV_LENGTH);
		abbrev.setVisibleLength(ABBREV_LENGTH);
		if (q != null) {
			abbrev.setText(q.getAbbreviation());
		}
		panel.add(abbrev);
		
		Label dtLabel = new Label("What type of answer?");
		dtLabel.addStyleName("ttd-form-label");
		panel.add(dtLabel);
		dataTypePicker = new ChoicePicker(getDataTypeChoices(), 1);
		if (q != null) {
			dataTypePicker.setSelectedIndex(getDataTypeIndex(q.getDataType()));
		}
		dataTypePicker.addValueChangeHandler(this);
		panel.add(dataTypePicker);
		
		//minMaxPanel for numeric questions
		numericParamsPanel = new VerticalPanel();
		numericParamsPanel.setVisible(false);
		if (DataType.DOUBLE.ordinal() == dataTypePicker.getSelectedIndex() || DataType.INTEGER.ordinal() == dataTypePicker.getSelectedIndex()) {
			numericParamsPanel.setVisible(true);
		}
		Label minLabel = new Label("Minimum value (if any)");
		minLabel.addStyleName("ttd-form-label");
		numericParamsPanel.add(minLabel);
		minBox = new MTextBox();
		if (q != null && q.getMinValue() != null) {
			minBox.setValue(String.valueOf(q.getMinValue()));
		} else {
			minBox.setValue("0");
		}
		minBox.setMaxLength(8);
		minBox.setVisibleLength(8);
		numericParamsPanel.add(minBox);
		Label maxLabel = new Label("Maximum value (if any)");
		maxLabel.addStyleName("ttd-form-label");
		numericParamsPanel.add(maxLabel);
		maxBox = new MTextBox();
		if (q != null && q.getMaxValue() != null) {
			maxBox.setValue(String.valueOf(q.getMaxValue()));
		}
		maxBox.setMaxLength(8);
		maxBox.setVisibleLength(8);
		numericParamsPanel.add(maxBox);
		Label measLabel = new Label("Unit of measure (if any)");
		measLabel.addStyleName("ttd-form-label");
		numericParamsPanel.add(measLabel);
		measurmentPicker = new ChoicePicker(getMeasurementChoices(), 2);
		if (q != null && q.getMeasurement() != null) {
			measurmentPicker.setSelectedIndex(q.getMeasurement().ordinal());
		}
		panel.add(numericParamsPanel);
		numericParamsPanel.add(measurmentPicker);
		
		saveButton = new Button("Save");
		saveButton.setSmall(true);
		saveButton.addTapHandler(this);
		panel.add(saveButton);
		initWidget(scrollPanel);
	}

	private Integer getDataTypeIndex(DataType dataType) {
		if (dataType == DataType.DOUBLE) {
			return 0;
		}
		if (dataType == DataType.SHORT_TEXT) {
			return 1;
		}
		if (dataType == DataType.LONG_TEXT) {
			return 2;
		}
		return null;
	}

	private List<Choice> getDataTypeChoices() {
		List<Choice> dataTypeChoices = new ArrayList<Choice>();
		Choice c = new Choice("Number", "Number");
		dataTypeChoices.add(c);
//		c = new Choice(DataType.INTEGER, "Integer", "for discreet values like a rating system");
//		dataTypeChoices.add(c);
//		c = new Choice(DataType.MULTIPLE_CHOICE, "Multiple Choice", "Multiple Choice");
//		dataTypeChoices.add(c);
		c = new Choice("Label", "Label");
		dataTypeChoices.add(c);
		c = new Choice("Memo", "Memo");
		dataTypeChoices.add(c);
//		c = new Choice(DataType.STARS, "Stars", "Stars");
//		dataTypeChoices.add(c);
		return dataTypeChoices;
	}

	public int getSelectedDataType() {
		int dt = DataType.DOUBLE.ordinal();
		if(dataTypePicker.getSelectedIndex()==1) {
			dt = DataType.SHORT_TEXT.ordinal();
		}
		if(dataTypePicker.getSelectedIndex()==2) {
			dt = DataType.LONG_TEXT.ordinal();
		}
		return dt;
	}
	
	private List<Choice> getMeasurementChoices() {
		List<Choice> choices = new ArrayList<Choice>();
		for (Measurement m: Measurement.values()) {
			String unitsStr = "";
			for (Unit unit: m.getUnits()) {
				if (unitsStr.length() > 0) unitsStr += ",";
				unitsStr += unit.getAbbrev();
			}
			if (unitsStr.length()==0) unitsStr = null;
			Choice c = new Choice(m.getLabel(), unitsStr);
			choices.add(c);
		}
		return choices;
	}
	
	@Override
	public void onTap(TapEvent event) {
		if (saveButton.equals(event.getSource())) {
			saveQuestion();
		}
	}
	
	public boolean saveQuestion() {
		int dataTypeIndex = getSelectedDataType();
		if (dataTypeIndex < 0) {
			validateMessage("Please select a Type of Question.");
			return false;
		}
		
		String longFormStr = longForm.getText();
		if (longFormStr==null || longFormStr.trim().length()==0) {
			validateMessage("Please enter a Question");
			return false;
		}
		if (longFormStr==null || longFormStr.trim().length() > LONG_FORM_MAX_LENGTH) {
			validateMessage("Your question should be shorter than " + LONG_FORM_MAX_LENGTH + " characters.");
			return false;
		}
//		String shortFormStr = shortForm.getText();
//		if (shortFormStr==null || shortFormStr.trim().length()==0) {
//			validateMessage("Please enter a Short Version");
//			return false;
//		}
		String abbrevStr = abbrev.getText();
		if (abbrevStr==null || abbrevStr.trim().length()==0) {
			validateMessage("Please enter a Label, which is an abbreviated form of your question.");
			return false;
		}
		
		if (q==null) {
//			q = new Question();
			q = BeanMaker.makeQuestion();
			q.setId(UUID.uuid());
			q.setCreated(new Date());
			q.setCreatedBy(App.getParticipantId());
			q.setOwnerId(App.getParticipantId());
			q.setLang(App.participant.getLang());
			q.setMaxLength(Constants.DEFAULT_TEXTFIELD_MAX_LENGTH);
//			q.setCreatorName(App.participant.getName());
		}
		
		q.setDataType(DataType.values()[dataTypeIndex]);
		q.setLongForm(longFormStr);
		q.setAbbreviation(abbrevStr);
		q.setUpdated(new Date());
		
		if (dataTypeIndex == DataType.LONG_TEXT.ordinal()) {
			q.setMaxLength(Constants.DEFAULT_MEMOFIELD_MAX_LENGTH);
		}
		q.setMinValue(null);
		if (minBox != null && minBox.getValue() != null) {
			try {
				double minVal = Double.parseDouble(minBox.getValue());
				q.setMinValue(minVal);
			} catch (NumberFormatException e) {
				//leave as null
			}
		}
		
		q.setMaxValue(null);
		if (maxBox != null && maxBox.getValue() != null) {
			try {
				double maxVal = Double.parseDouble(maxBox.getValue());
				q.setMaxValue(maxVal);
			} catch (NumberFormatException e) {
				//leave as null
			}
		}
		
		if (measurmentPicker != null && measurmentPicker.getSelectedChoice() != null && measurmentPicker.getSelectedIndex() != Measurement.NONE.ordinal()) {
			q.setMeasurement(Measurement.values()[measurmentPicker.getSelectedIndex()]);
		} else {
			q.setMeasurement(null);
		}
		App.eventBus.fireEvent(new QuestionSavedEvent(q));
		return true;
	}

	private void validateMessage(String message) {
		Window.alert(message);
	}

	@Override
	public void onValueChange(ValueChangeEvent<Choice> event) {
//		Choice selectedChoice = event.getValue();
		int dataTypeIndex = dataTypePicker.getSelectedIndex();
		
		if (dataTypeIndex==0) {
			numericParamsPanel.setVisible(true);
		} else {
			numericParamsPanel.setVisible(false);
		}
		scrollPanel.refresh();
	}
	
	

}
