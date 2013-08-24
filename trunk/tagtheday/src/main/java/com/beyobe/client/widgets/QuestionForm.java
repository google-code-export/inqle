package com.beyobe.client.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.Constants;
import com.beyobe.client.beans.Choice;
import com.beyobe.client.beans.DataType;
import com.beyobe.client.beans.Measurement;
import com.beyobe.client.beans.Parcel;
import com.beyobe.client.beans.PrivacyType;
import com.beyobe.client.beans.Question;
import com.beyobe.client.beans.Unit;
import com.beyobe.client.data.BeanMaker;
import com.beyobe.client.event.QuestionSavedEvent;
import com.beyobe.client.util.UUID;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.Button;
import com.googlecode.mgwt.ui.client.widget.MDoubleBox;
import com.googlecode.mgwt.ui.client.widget.MIntegerBox;
import com.googlecode.mgwt.ui.client.widget.MListBox;
import com.googlecode.mgwt.ui.client.widget.MSearchBox;
import com.googlecode.mgwt.ui.client.widget.MTextArea;
import com.googlecode.mgwt.ui.client.widget.MTextBox;

public class QuestionForm extends Composite implements TapHandler, ValueChangeHandler<Choice>, KeyUpHandler, ChangeHandler {

	private static final int LONG_FORM_MAX_LENGTH = 250;
	private static final int ABBREV_LENGTH = 10;
	private static final int DATATYPEINDEX_NUMBER = 0;
	private static final int DATATYPEINDEX_LABEL = 1;
	private static final int DATATYPEINDEX_MEMO = 2;
	private static final Integer PRIVACYTYPEINDEX_PRIVATE = 1;
	private static final Integer PRIVACYTYPEINDEX_PUBLIC = 0;
	private static final String ABBREV_SEARCH_BOX_NAME = "AbbrevSearchBox";
	private Question q;
//	private MTextBox shortForm;
	private MSearchBox abbrev;
	private MTextArea longForm;
	private ChoicePicker dataTypePicker;
	private ChoicePicker measurmentPicker;
	private ChoicePicker privacyTypePicker;
	private MDoubleBox minVal = new MDoubleBox();
	private MDoubleBox maxVal = new MDoubleBox();
	private MIntegerBox maxLength;
	private Button saveButton;
	private VerticalPanel numericParamsPanel;
	private VerticalPanel privacyPanel;
	private MTextBox minBox;
	private MTextBox maxBox;
	private VerticalPanel maxLengthPanel;
//	private ScrollPanel scrollPanel;
//	private boolean notWaititngForResponse = true;
	private MListBox abbrevLB = new MListBox();
	
	private boolean searching = false;
//	private Typeahead abbrevTA;
//	private MultiWordSuggestOracle abbrevOracle;
	
	Logger log = Logger.getLogger("QuestionForm");
	private List<Question> matchingQuestions = new ArrayList<Question>();
	private boolean editMode;
	
	
	public QuestionForm(Question originalQuestion) {
		if (originalQuestion == null) {
			editMode = false;
		} else {
			editMode = true;
		}
//		scrollPanel = new ScrollPanel();
//		scrollPanel.setShowScrollBarX(true);
//	    scrollPanel.setShowScrollBarY(true);
//	    scrollPanel.setScrollingEnabledX(false);
//	    scrollPanel.setScrollingEnabledY(true);
//	    scrollPanel.setAutoHandleResize(true);
//	    scrollPanel.setUsePos(true);
//	    scrollPanel.setSnap(false);
//	    scrollPanel.setBounce(true);
//	    scrollPanel.setWidget(panel);
	    
//	    scrollPanel.setUsePos(true);
		VerticalPanel panel = new VerticalPanel();
		
		panel.setWidth("100%");
		panel.setHeight("100%");
		Label questionFull = new Label();
		if (editMode) {
			questionFull.setText("Edit Question");
		} else {
			questionFull.setText("Add a Question");
		}
		questionFull.addStyleName("ttd-form-header");
		panel.add(questionFull);
		
		Label abbrevLabel = new Label("Short Label (example: Happy)");
		abbrevLabel.addStyleName("ttd-form-label");
		panel.add(abbrevLabel);
//		abbrevTA = new Typeahead();
//		abbrevTA.setMinLength(2);
//		abbrevTA.setDisplayItemCount(7);
//		abbrevTA.
//		abbrevOracle = (MultiWordSuggestOracle) abbrevTA.getSuggestOracle();
		abbrev = new MSearchBox();
		abbrev.setName(ABBREV_SEARCH_BOX_NAME);
//		abbrev.setMaxLength(ABBREV_LENGTH);
//		abbrev.setVisibleLength(ABBREV_LENGTH);
		abbrev.addKeyUpHandler(this);
		panel.add(abbrev);
		
		if (! editMode) {
			abbrevLB = new MListBox();
//			abbrevLB.setSize("90%", "200px");
			abbrevLB.setVisibleItemCount(1);
			abbrevLB.addChangeHandler(this);
			
			panel.add(abbrevLB);
		}
		
		//add input elements
		//long form
		Label longLabel = new Label("Question (example: How happy were you?)");
		longLabel.addStyleName("ttd-form-label");
//		longLabel.getElement().getStyle().setFontSize(0.7, Unit.EM);
		panel.add(longLabel);
		longForm = new MTextArea();
		longForm.setAutoCorrectEnabled(true);
		longForm.setCharacterWidth(30);
		longForm.setVisibleLines(3);
		
		panel.add(longForm);
		
		Label dtLabel = new Label("What type of answer?");
		dtLabel.addStyleName("ttd-form-label");
		panel.add(dtLabel);
		dataTypePicker = new ChoicePicker(getDataTypeChoices(), 1);
		dataTypePicker.setSelectedIndex(DATATYPEINDEX_NUMBER);
		dataTypePicker.addValueChangeHandler(this);
		panel.add(dataTypePicker);
		
		//minMaxPanel for numeric questions
		numericParamsPanel = new VerticalPanel();
		numericParamsPanel.setVisible(false);
		if (DATATYPEINDEX_NUMBER == dataTypePicker.getSelectedIndex()) {
			numericParamsPanel.setVisible(true);
		}
		Label minLabel = new Label("Minimum value (if any)");
		minLabel.addStyleName("ttd-form-label");
		numericParamsPanel.add(minLabel);
		minBox = new MTextBox();
		
		minBox.setMaxLength(8);
		minBox.setVisibleLength(8);
		numericParamsPanel.add(minBox);
		Label maxLabel = new Label("Maximum value (if any)");
		maxLabel.addStyleName("ttd-form-label");
		numericParamsPanel.add(maxLabel);
		maxBox = new MTextBox();
		
		maxBox.setMaxLength(8);
		maxBox.setVisibleLength(8);
		numericParamsPanel.add(maxBox);
		Label measLabel = new Label("Unit of measure (if any)");
		measLabel.addStyleName("ttd-form-label");
		numericParamsPanel.add(measLabel);
		measurmentPicker = new ChoicePicker(getMeasurementChoices(), 2);
		
		panel.add(numericParamsPanel);
		numericParamsPanel.add(measurmentPicker);
		
		privacyPanel = new VerticalPanel();
		Label privacyLabel = new Label("Would anyone else want to answer this question?");
		privacyLabel.addStyleName("ttd-form-label");
		privacyPanel.add(privacyLabel);
		privacyTypePicker = new ChoicePicker(getPrivacyTypeChoices(), 1);
		privacyPanel.add(privacyTypePicker);
		panel.add(privacyPanel);
		
		saveButton = new Button("Save");
		saveButton.setSmall(true);
		saveButton.addTapHandler(this);
		panel.add(saveButton);
		
		boolean disableForm = true;
		if (originalQuestion != null && (originalQuestion.getOwnerId().equals(App.session.getUserUid()) || App.isAdminUser())) {
			disableForm = false;
		}
		setQuestion(originalQuestion, disableForm);
		
//		initWidget(scrollPanel);
		initWidget(panel);
	}


	public void setQuestion(Question q, boolean disableForm) {
		searching = false;
		this.q = q;
		
		if (q == null) return;
			
		if (q.getAbbreviation() != null) abbrev.setText(q.getAbbreviation());
		longForm.setText(q.getLongForm());
		DataType dataType = q.getDataType();
		log.info("dataType=" + dataType);
		int selectedIndex = getDataTypeIndex(dataType);
		dataTypePicker.setSelectedIndex(selectedIndex);
		
		if (q.getMinValue() != null) {
			minBox.setValue(String.valueOf(q.getMinValue()));
		} 
//			else {
//				minBox.setValue("0");
//			}
		
		if (q.getMaxValue() != null) {
			maxBox.setValue(String.valueOf(q.getMaxValue()));
		}
		
		if ( q.getMeasurement() != null) {
			measurmentPicker.setSelectedIndex(q.getMeasurement().ordinal());
		} else {
			measurmentPicker.setSelectedIndex(0);
		}
		PrivacyType privacyType = q.getPrivacyType();
		int selectedPrivacyIndex = getPrivacyTypeIndex(privacyType);
		privacyTypePicker.setSelectedIndex(selectedPrivacyIndex);
		
		setVisibilityOfSubFields();
		
		disableForm(disableForm);
	}

	private void disableForm(boolean disable) {
		if (disable) {
			saveButton.setText("Subscribe");
		} else {
			saveButton.setText("Save");
		}
//		abbrev.setReadOnly(disable);
		longForm.setReadOnly(disable);
		
		dataTypePicker.setDisabled(disable);
		measurmentPicker.setDisabled(disable);
		privacyTypePicker.setDisabled(disable);
		if (minVal != null) {
			minVal.setReadOnly(disable);
		}
		if (maxVal != null) maxVal.setReadOnly(disable);
		if (maxLength != null) maxLength.setReadOnly(disable);
		
	}

	private Integer getDataTypeIndex(DataType dataType) {
		if (dataType == DataType.DOUBLE) {
			return DATATYPEINDEX_NUMBER;
		}
		if (dataType == DataType.SHORT_TEXT) {
			return DATATYPEINDEX_LABEL;
		}
		if (dataType == DataType.LONG_TEXT) {
			return DATATYPEINDEX_MEMO;
		}
		return null;
	}

	private List<Choice> getDataTypeChoices() {
		List<Choice> dataTypeChoices = new ArrayList<Choice>();
		Choice c = new Choice("Number", "Number");
		dataTypeChoices.add(c);
		c = new Choice("Label", "Label");
		dataTypeChoices.add(c);
		c = new Choice("Memo", "Memo");
		dataTypeChoices.add(c);
		return dataTypeChoices;
	}

	private Integer getPrivacyTypeIndex(PrivacyType privacyType) {
		if (privacyType == PrivacyType.PRIVATE) {
			return PRIVACYTYPEINDEX_PRIVATE;
		}
		
		return PRIVACYTYPEINDEX_PUBLIC;
	}
	
	private List<Choice> getPrivacyTypeChoices() {
		List<Choice> privacyTypeChoices = new ArrayList<Choice>();
		Choice c = new Choice("Yes", "Others might want to answer this.");
		privacyTypeChoices.add(c);
		c = new Choice("No", "I am the only one who would answer this.");
		privacyTypeChoices.add(c);
		return privacyTypeChoices;
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
	

	private int getSelectedPrivacyType() {
		int pt = PrivacyType.PUBLIC.ordinal();
		if(privacyTypePicker.getSelectedIndex()==1) {
			pt = PrivacyType.PRIVATE.ordinal();
		}
		return pt;
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
		
		int privacyTypeIndex = getSelectedPrivacyType();
		if (privacyTypeIndex < 0) {
			validateMessage("Please select whether others might wish to answer this same question.");
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
			q = getNewQuestion();
//			q.setCreatorName(App.participant.getName());
		}
		
		q.setDataType(DataType.values()[dataTypeIndex]);
		q.setPrivacyType(PrivacyType.values()[privacyTypeIndex]);
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



	private Question getNewQuestion() {
		Question newQuestion = BeanMaker.makeQuestion();
		newQuestion.setId(UUID.uuid());
		newQuestion.setCreated(new Date());
		newQuestion.setCreatedBy(App.getParticipantId());
		newQuestion.setOwnerId(App.getParticipantId());
		newQuestion.setLang(App.session.getLang());
		newQuestion.setMaxLength(Constants.DEFAULT_TEXTFIELD_MAX_LENGTH);
		newQuestion.setDataType(DataType.DOUBLE);
		return newQuestion;
	}


	private void validateMessage(String message) {
		Window.alert(message);
	}

	@Override
	public void onValueChange(ValueChangeEvent<Choice> event) {
		setVisibilityOfSubFields();
	}
	
	public void setVisibilityOfSubFields() {
		int dataTypeIndex = dataTypePicker.getSelectedIndex();
		
		if (dataTypeIndex==DATATYPEINDEX_NUMBER) {
			numericParamsPanel.setVisible(true);
		} else {
			numericParamsPanel.setVisible(false);
		}
//		scrollPanel.setMaxScrollY(8000);
//		scrollPanel.refresh();
//		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
//
//            @Override
//            public void execute() {
//                scrollPanel.refresh();
//            }
//        });
	}
	
	public void onSearchQuestionError() {
		searching = false;
	}
	
	/**
	 * This is called when a search returns
	 * @param parcel
	 */
	public void onSearchQuestionsReturns(Parcel parcel) {
		log.info("onSearchQuestionsReturns called for: " + parcel.getQueryTerm());
//		notWaititngForResponse = true;
//		String theAbbrev = abbrev.getText();
		String theAbbrev = getAbbrev();
		updateAbbrev(theAbbrev);
		
		this.matchingQuestions = parcel.getQuestions();
		
		//if the term searched for does not equal the current abbrev, do another search
		if (! parcel.getQueryTerm().equals(theAbbrev)) {
			searchForQuestions();
		} else {
			searching = false;
		}
				
		if (matchingQuestions != null && matchingQuestions.size()> 0) {
			log.info("Received " + matchingQuestions.size() + " matching questions");
			abbrevLB.clear();
			
			abbrevLB.setSelectedIndex(0);
			int numItems = matchingQuestions.size();
			if (numItems > 7) numItems = 7;
			abbrevLB.setVisibleItemCount(numItems + 1);
			for (int i=0; i < numItems; i++) {
				Question q = parcel.getQuestions().get(i);
				abbrevLB.addItem(q.getAbbreviation() + ": " + q.getLongForm());
			}
		}
	}

	private void updateAbbrev(String theAbbrev) {
		try {
			abbrevLB.setItemText(0, theAbbrev);
			abbrevLB.setSelectedIndex(0);
		} catch (Exception e) {
			abbrevLB.clear();
			abbrevLB.addItem(theAbbrev);
		}		
	}


	private String getAbbrev() {
		String theAbbrev = abbrev.getText();
		theAbbrev = theAbbrev.trim();
		if (theAbbrev.endsWith("?") || theAbbrev.endsWith(":") || theAbbrev.endsWith("-")) {
			theAbbrev = theAbbrev.substring(0, theAbbrev.length() - 1);
		}
		if (theAbbrev.length() > ABBREV_LENGTH) {
			theAbbrev = theAbbrev.substring(0, ABBREV_LENGTH);
		}
		return theAbbrev;
	}


	@Override
	/**
	 * This is called when a user is typing in a text box
	 */
	public void onKeyUp(KeyUpEvent event) {
		Widget sender = (Widget) event.getSource();
		String senderName = null;
		if (sender instanceof TextBox) {
			TextBox textBox = (TextBox)sender;
			senderName = textBox.getName();
		}
		log.info("senderName=" + senderName + "; abbrev=" + getAbbrev());
		
		//if abbrev box is the source of the change, update the abbrev listbox and search
		if (! editMode && abbrev.getName().equals(senderName)) {
//		if (! editMode && ABBREV_SEARCH_BOX_NAME.equals(senderName)) {
//			setQuestion(getNewQuestion(), false);
			this.q = getNewQuestion();
			updateAbbrev(getAbbrev());
			searchForQuestions();
		}
	}

	private void searchForQuestions() {
		if (searching) return;
		log.info("searchForQuestions using: " + abbrev.getText());
		if (abbrev.getText().length() > 1) {
			searching = true;
			Parcel parcel = App.dataBus.newParcel();
			parcel.setQueryTerm(abbrev.getText());
//			notWaititngForResponse = false;
			log.info("searchForQuestions sending request...");
			parcel.setAction(Constants.SERVERACTION_SEARCH_QUESTIONS);
			App.parcelClient.sendParcel(parcel);
		}
	}


	@Override
	/**
	 * This is called when a user selects a abbreviation in the dropdown list
	 */
	public void onChange(ChangeEvent event) {
		if (abbrevLB.getSelectedIndex()==0) {
			abbrev.setText(abbrevLB.getItemText(0));
			setQuestion(getNewQuestion(), false);
		} else {
			disableForm(true);
			Question selectedQuestion = matchingQuestions.get(abbrevLB.getSelectedIndex() - 1);
			setQuestion(selectedQuestion, true);
		}
		
		
	}

}
