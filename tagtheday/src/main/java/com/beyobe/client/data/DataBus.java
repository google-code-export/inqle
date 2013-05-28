package com.beyobe.client.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.Constants;
import com.beyobe.client.activities.TagdayPlace;
import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Parcel;
import com.beyobe.client.beans.Participant;
import com.beyobe.client.beans.Question;
import com.beyobe.client.widgets.Day;
import com.beyobe.client.widgets.TagButton;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class DataBus {
	
	private static Logger log = Logger.getLogger(EventBus.class.getName());
	
	private static Map<String, Question> knownQuestions = new HashMap<String, Question>();
	private static List<Question> questionQueue = new ArrayList<Question>();
	private static LinkedHashMap<String, List<Datum>> dataByDate = new LinkedHashMap<String, List<Datum>>();

	public Participant participant;

	private ArrayList<Day> allDays;
	
	public DataBus() {
		//TODO load questions and data from local storage
		//populate dataByDate, knownQuestions
		//TODO sync questions and data with remote DB
	}
	
	public void addQuestionToQueue(Question q) {
		questionQueue.add(q);
	}
	
	public void setQuestionQueue(List<Question> questions) {
		questionQueue = questions;
	}
	
	public void addQuestionQueue(List<Question> questions) {
		for (Question q: questions) {
			if (! questionQueue.contains(q)) {
				questionQueue.add(q);
			}
		}
	}
	
	public void addQuestionToKnownQuestions(Question question) {
		knownQuestions.put(question.getId(), question);
	}
	
	public static String getDateString(Date date) {
		return DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
	}
	
	public static List<TagButton> getTagButtonsForDate(Date effectiveDate) {
		List<TagButton> buttons = new ArrayList<TagButton>();
		List<Datum> data = getDataForDate(effectiveDate);
		if (data==null) return null;
		for (Datum datum: data) {
			Question theQuestion = getQuestion(datum.getQuestionUid());
			if (theQuestion==null) {
				log.log(Level.SEVERE, "Unable to find question: " + datum.getQuestionUid());
			}
			TagButton button = new TagButton(effectiveDate, theQuestion, datum);
			buttons.add(button);
		}
		return buttons;
	}

	private static List<Datum> getDataForDate(Date date) {
		return dataByDate.get(DataBus.getDateString(date));
	}

	private static Question getQuestion(String questionUid) {
		if (knownQuestions.get(questionUid) != null) {
			return knownQuestions.get(questionUid);
		}
		//TODO try to retrieve the question from the server
		
		//unable to find it
		return null;
	}
	
	public void setDatum(Datum datumToSave) {
		Date effectiveDate = datumToSave.getEffectiveDate();
//		String dateStr = DataBus.getDateString(effectiveDate);
		
		//first save in memory
		List<Datum> dataForDay = getDataForDate(effectiveDate);
		if (dataForDay == null) dataForDay = new ArrayList<Datum>();
		List<Datum> dataForLooping = new ArrayList<Datum>(dataForDay);
		int index = 0;
		boolean replaced = false;
		for (Datum datum: dataForLooping) {
			if (datumToSave.getQuestionUid() == datum.getQuestionUid()) {
				dataForDay.remove(index);
				dataForDay.add(index, datumToSave);
				replaced = true;
			}
			index++;
		}
		if (! replaced) {
			dataForDay.add(datumToSave);
		}
		
		Parcel parcel = newParcel();
		parcel.setDatum(datumToSave);
		App.parcelClient.sendParcel(parcel, Constants.SERVERACTION_STORE_DATUM);
		
		//TODO save in local storage
		
		//save back to our in-memory map
		 dataByDate.put(DataBus.getDateString(effectiveDate), dataForDay);
	}
	
	public List<Day> createAllDays() {
		allDays = new ArrayList<Day>();
		
		//add days from the start of data collection to the present.
//		DateTimeFormat format = DateTimeFormat.getFormat("yyyy-MM-dd");
		Date startOfToday = Constants.DAY_FORMATTER.parse(Constants.DAY_FORMATTER.format(new Date()));
		
		//if no data, just return today's day
		if (dataByDate != null && dataByDate.size()>0) {
			Day day = new Day(startOfToday);
			allDays.add(day);
			return allDays;
		}
				
		//find earliest date
		Date earliestDate = null;
		for (Map.Entry<String,  List<Datum>> entry : dataByDate.entrySet()) {
		    String key = entry.getKey();
		    earliestDate = Constants.DAY_FORMATTER.parse(key);
		    break;
		}	
		
		Date date = new Date(startOfToday.getTime());
		if (earliestDate != null) {
			date = earliestDate;
		}
		
		while(date.before(startOfToday) || date.equals(startOfToday)) {
			String key = Constants.DAY_FORMATTER.format(date);
			List<Datum> dataForDay = dataByDate.get(key);
			Day day = new Day(date);
			//advance date 24 hours
			CalendarUtil.addDaysToDate(date, 1);
			date = new Date(date.getTime());
			
			if (dataForDay == null || dataForDay.size()==0) {
				allDays.add(day);
				continue;
			}
			
			//add all tagbuttons to this day
			for (Datum d: dataForDay) {
				Question q = knownQuestions.get(d.getQuestionUid());
				TagButton tagButton = new TagButton(d.getEffectiveDate(), q, d);
				day.addTagButton(tagButton);
			}
			
			allDays.add(day);
			
		}
		
		
		return allDays;
	}

	public List<Day> getAllDays() {
		return allDays;
	}
	
	public List<String> getPastAnswers(Question q) {
		List<String> answers = new ArrayList<String>();
		for (Map.Entry<String, List<Datum>> entry: dataByDate.entrySet()) {
			List<Datum> data = entry.getValue();
			for (Datum datum: data) {
				if (datum.getQuestionUid().equals(q.getId())) {
					String pastAnswer = datum.getTextValue();
					if (! answers.contains(pastAnswer)) {
						answers.add(pastAnswer);
					}
				}
			}
		}
		//TODO: reverse
//		answers = Lists.reverse(answers);
		return answers;
	}

	public void saveQuestion(Question question) {
		if (! questionQueue.contains(question)) {
			questionQueue.add(question);
		}
		knownQuestions.put(question.getId(), question);
		
		//add the question to each day
		for (Day day: getAllDays()) {
			day.addQuestion(question);
		}
		
		Parcel parcel = newParcel();
		parcel.setQuestion(question);
		App.parcelClient.sendParcel(parcel, Constants.SERVERACTION_STORE_QUESTION);
		//TODO save locally
	}

	public void refreshDataFromJson(String text) {
		try {
			AutoBean<Parcel> parcelAB = AutoBeanCodex.decode(App.tagthedayAutoBeanFactory, Parcel.class, text);
		    Parcel parcel = parcelAB.as();
		    if (parcel.getSessionToken() != null) {
		    	App.sessionToken = parcel.getSessionToken();
		    }
		    if (parcel.getQuestionQueue() != null) {
		    	setQuestionQueue(parcel.getQuestionQueue());
		    	setKnownQuestions(parcel.getQuestionQueue(), parcel.getOtherKnownQuestions());
		    }
		    if (parcel.getData() != null) {
		    	 setData(parcel.getData());
		    }
		    if (parcel.getParticipant() != null) {
		    	App.participant = parcel.getParticipant();
		    }
		    
		    //TODO determine which place to go based on data received?
		    
		    //default: go to TagdayPlace
		    log.info("Going to TagdayPlace...");
		    App.placeController.goTo(new TagdayPlace());
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error parsing JSON into Datum objects", e);
		}
	}

	public Parcel newParcel() {
	    AutoBean<Parcel> parcelAB = App.tagthedayAutoBeanFactory.parcel();
	    Parcel parcel = parcelAB.as();
	    parcel.setSessionToken(App.sessionToken);
	    return parcel;
	}

	private void setKnownQuestions(List<Question> qq, List<Question> otherKnownQuestions) {
//		knownQuestions = new HashMap<String, Question>();
		for (Question q: qq) {
			knownQuestions.put(q.getId(), q);
		}
		for (Question q: otherKnownQuestions) {
			knownQuestions.put(q.getId(), q);
		}
		
	}

	private void setData(List<Datum> data) {
//		dataByDate = new LinkedHashMap<String, List<Datum>>();
		for (Datum d: data) {
			String dayStr = Constants.DAY_FORMATTER.format(d.getEffectiveDate());
			List<Datum> dayData = dataByDate.get(dayStr);
			if (dayData==null) dayData = new ArrayList<Datum>();
			dayData.add(d);
			dataByDate.put(dayStr, dayData);
		}
	}
}
