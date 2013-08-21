package com.beyobe.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.mortbay.log.Log;

import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Question;
import com.beyobe.client.util.UUID;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DataTimeline {

	private Map<String, Datum> data = new HashMap<String, Datum>();
	//Map<questionId, List<date>
	private Map<String, List<String>> answerDates = new HashMap<String, List<String>>();
	//Map<date, Map<questionId, datumId>>
	private static HashMap<String, Map<String, String>> dataByDate = new HashMap<String,  Map<String, String>>();
	
	private static Logger log = Logger.getLogger("DataTimeline");
	
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
	
	public Datum getDatum(Date date, String questionId) {
		if (questionId==null) return null;
		if (date==null) return null;
		Map<String, String> answersForDate = dataByDate.get(getDateStr(date));
		if (answersForDate==null) return null;
		String datumId = answersForDate.get(questionId);
		if (datumId==null) return null;
		return data.get(datumId);
	}
	
	public Datum getDatum(String dateStr, String questionId) {
		if (questionId==null) return null;
		if (dateStr==null) return null;
		Map<String, String> answersForDate = dataByDate.get(dateStr);
		if (answersForDate==null) return null;
		String datumId = answersForDate.get(questionId);
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
//		Collections.sort(datesQuestionAnswered);
		datesQuestionAnswered.add(dateStr);
		answerDates.put(d.getQuestionId(), datesQuestionAnswered);
	}
	
	public static String getDateStr(Date date) {
		return DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
	}
	
	public static Date getDate(String dateStr) {
		return DateTimeFormat.getFormat("yyyy-MM-dd").parse(dateStr);
	}
	
	public Datum getSubsequentAnswer(Question q, Date referenceDate) {
		if (q==null || referenceDate==null) return null;
		List<String> datesQuestionAnswered = answerDates.get(q.getId());
		Collections.sort(datesQuestionAnswered);
//		boolean afterDate = false;
		for (String dateStr: datesQuestionAnswered) {
			Date date = getDate(dateStr);
			if (date==null) continue;
			if (date.after(referenceDate)) {
				return getDatum(date, q);
			}
//			if (date.after(referenceDate)) {
//				afterDate = true;
//			}
		}
		return null;
	}

	public Datum getPriorAnswer(Question q, Date referenceDate) {
		if (q==null || referenceDate==null) return null;
		List<String> datesQuestionAnswered = getAnswerDates(q);
		Collections.sort(datesQuestionAnswered);
//		boolean beforeDate = false;
		for (int i=datesQuestionAnswered.size()-1; i >= 0; i--) {
			String dateStr = datesQuestionAnswered.get(i);
//			log.info("Question: " + q.getAbbreviation() + ": getPriorAnswer() on " + dateStr + "?");
			Date date = getDate(dateStr);
			if (date==null) continue;
			if (date.before(referenceDate)) {
//				log.info("Question: " + q.getAbbreviation() + ": getPriorAnswer() returning datum for " + dateStr);
				return getDatum(date, q);
			}
//			if (date.before(referenceDate)) {
//				beforeDate = true;
//			}
		}
		log.info("Question: " + q.getAbbreviation() + ": getPriorAnswer() returning NULL");
		return null;
	}
	
	private List<String> getAnswerDates(Question q) {
		List<String> datesQuestionAnswered = answerDates.get(q.getId());
		if (datesQuestionAnswered==null) {
			datesQuestionAnswered = new ArrayList<String>();
			answerDates.put(q.getId(), datesQuestionAnswered);
		}
		return datesQuestionAnswered;
	}

	public Map<String, Datum> getDataForDate(Date date) {
		String dateStr = getDateStr(date);
		Map<String, String> dataIdsForDate = dataByDate.get(dateStr);
		if (dataIdsForDate==null) return null;
		Map<String, Datum> dataObjsForDate = new HashMap<String, Datum>();
		for (Map.Entry<String, String> entry: dataIdsForDate.entrySet()) {
			String datumId = entry.getValue();
			String questionId = entry.getKey();
			Datum d = get(datumId);
			if (d!=null) log.info("getDataForDate(): set datum: " + d.getTextValue());
			dataObjsForDate.put(questionId, d);
		}
		return dataObjsForDate;
	}

	public void setData(List<Datum> data) {
		if (data == null) return;
		for (Datum d: data) {
			put(d);
		}
	}

	public List<String> getPastAnswers(Question q) {
		List<String> answers = new ArrayList<String>();
		for (Map.Entry<String, Map<String, String>> entry: dataByDate.entrySet()) {
			String dateStr = entry.getKey();
			Map<String, String> data = entry.getValue();
			for (String qid: data.keySet()) {
				if (qid.equals(q.getId())) {
					Datum datum = getDatum(dateStr, qid);
					String pastAnswer = datum.getTextValue();
					if (! answers.contains(pastAnswer)) {
						answers.add(pastAnswer);
					}
				}
			}
		}
		return answers;
	}

	public static Datum cloneDatum(Datum d1) {
		if (d1==null) return null;
		Datum d2 = BeanMaker.makeDatum();
		d2.setId(UUID.uuid());
		d2.setCreated(new Date());
		d2.setEffectiveDate(d1.getEffectiveDate());
		d2.setAnswerStatus(d1.getAnswerStatus());
		d2.setChoice(d1.getChoice());
		d2.setDataType(d1.getDataType());
		d2.setNumericValue(d1.getNumericValue());
		d2.setIntegerValue(d1.getIntegerValue());
		d2.setNormalizedValue(d1.getNormalizedValue());
		d2.setTextValue(d1.getTextValue());
		d2.setUnit(d1.getUnit());
		d2.setUserId(d1.getUserId());
		d2.setQuestionConcept(d1.getQuestionConcept());
		d2.setQuestionId(d1.getQuestionId());
		return d2;
	}

//	public void removeDatum(Datum datum) {
//		data.remove(datum.getId());
//	}
}