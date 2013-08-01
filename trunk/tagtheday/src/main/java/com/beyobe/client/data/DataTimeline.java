package com.beyobe.client.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Question;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DataTimeline {

	private Map<String, Datum> data = new HashMap<String, Datum>();
	//Map<questionId, List<date>
	private Map<String, List<String>> answerDates = new HashMap<String, List<String>>();
	//Map<date, Map<questionId, datumId>>
	private static HashMap<String, Map<String, String>> dataByDate = new HashMap<String,  Map<String, String>>();
	
	public Datum get(String datumId) {
		return data.get(datumId);
	}
	
	public Datum getDatum(Date date, Question question) {
		if (question==null) return null;
		if (date==null) return null;
		Map<String, String> answersForDate = dataByDate.get(getDateStr(date));
		if (answersForDate==null) return null;
		String datumId = answersForDate.get(question.getId());
		if (datumId==null) return null;
		return data.get(datumId);
	}
	
	public void put(Datum d) {
		if (d==null) return;
		data.put(d.getId(), d);
		
		String dateStr = getDateStr(d.getEffectiveDate());
		Map<String, String> dayData = dataByDate.get(dateStr);
		if (dayData == null) {
			dayData = new HashMap<String, String>();
		}
		dayData.put(d.getQuestionId(), d.getId());
		dataByDate.put(dateStr, dayData);
		
		List<String> datesQuestionAnswered = answerDates.get(d.getQuestionId());
		if (datesQuestionAnswered == null) {
			datesQuestionAnswered = new ArrayList<String>();
		}
	}
	
	public static String getDateStr(Date date) {
		return DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
	}
	
	public Datum getSubsequentAnswer(Question q, Date date) {
		
	}

	public Datum getPriorAnswer(Question q, Date date) {
		// TODO Auto-generated method stub
		return null;
	}
}