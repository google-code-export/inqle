package com.beyobe.client.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Question;
import com.beyobe.client.widgets.TagButton;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DataBus {

	private Logger log = Logger.getLogger(getClass().getName());
	
	private Map<Long, Question> knownQuestions = new HashMap<Long, Question>();
	private Map<String, List<Datum>> dataByDate = new HashMap<String, List<Datum>>();
	
	public DataBus() {
		//TODO load questions and data from local storage
		//populate dataByDate, knownQuestions
		//TODO sync questions and data with remote DB
	}
	
	public static String getDateString(Date date) {
		return DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
	}
	
	public List<TagButton> getTagButtonsForDate(Date effectiveDate) {
		List<TagButton> buttons = new ArrayList<TagButton>();
		List<Datum> data = getDataForDate(effectiveDate);
		if (data==null) return null;
		for (Datum datum: data) {
			Question theQuestion = getQuestion(datum.getQuestionId());
			if (theQuestion==null) {
				log.log(Level.SEVERE, "Unable to find question: " + datum.getQuestionId());
			}
			TagButton button = new TagButton(effectiveDate, theQuestion, datum);
			buttons.add(button);
		}
		return buttons;
	}

	private List<Datum> getDataForDate(Date date) {
		return dataByDate.get(DataBus.getDateString(date));
	}

	private Question getQuestion(long questionId) {
		if (knownQuestions.get(questionId) != null) {
			return knownQuestions.get(questionId);
		}
		//TODO try to retrieve the question from the server
		
		//unable to find it
		return null;
	}
	
	public void saveDatum(Datum datumToSave) {
		Date effectiveDate = datumToSave.getEffectiveDate();
//		String dateStr = DataBus.getDateString(effectiveDate);
		
		//first save in memory
		List<Datum> dataForDay = getDataForDate(datumToSave.getEffectiveDate());
		int index = 0;
		boolean replaced = false;
		for (Datum datum: dataForDay) {
			if (datumToSave.getQuestionId() == datum.getQuestionId()) {
				dataForDay.remove(index);
				dataForDay.add(index, datumToSave);
				replaced = true;
			}
			index++;
		}
		if (! replaced) {
			dataForDay.add(datumToSave);
		}
		
		//TODO save in local storage
		
		//TODO send to server
	}
}
