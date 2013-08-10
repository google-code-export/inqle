package com.beyobe.client.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.beyobe.client.App;
import com.beyobe.client.Constants;
import com.beyobe.client.activities.TagdayPlace;
import com.beyobe.client.beans.AnswerStatus;
import com.beyobe.client.beans.Datum;
import com.beyobe.client.beans.Message;
import com.beyobe.client.beans.Parcel;
import com.beyobe.client.beans.Participant;
import com.beyobe.client.beans.Question;
import com.beyobe.client.widgets.Day;
import com.beyobe.client.widgets.TagButton;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Handle all data needs
 * 
 * TODO: keep a registry of what is known
 * TODO load windows of data when unknown data is requested
 * TODO: associate immediate past answer with future answers
 * @author donohue
 *
 */
public class DataBus {
	
	private static Logger log = Logger.getLogger(EventBus.class.getName());
	
	private static Map<String, Question> knownQuestions = new HashMap<String, Question>();
	private static List<Question> questionQueue = new ArrayList<Question>();
//	private static HashMap<String, Map<String, Datum>> dataByDate = new HashMap<String,  Map<String, Datum>>();

	public Participant participant;

//	private HashMap<String, Day> allDays = new HashMap<String, Day>();
	private DataTimeline dataTimeline = new DataTimeline();
	
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
	
//	public static String getDateString(Date date) {
//		return DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
//	}
	
	public List<TagButton> getTagButtonsForDate(Date effectiveDate) {
		List<TagButton> buttons = new ArrayList<TagButton>();
//		Map<String, Datum> data = getDataForDate(effectiveDate);
		Map<String, Datum> data = dataTimeline.getDataForDate(effectiveDate);
		if (data==null) return null;
		for (Question q: questionQueue) {
			Datum d = data.get(q.getId());
			TagButton button = new TagButton(effectiveDate, q, d);
			buttons.add(button);
		}
//		for (Datum datum: data.) {
//			Question theQuestion = getQuestion(datum.getQuestionId());
//			if (theQuestion==null) {
//				log.log(Level.SEVERE, "Unable to find question: " + datum.getQuestionId());
//			}
//			TagButton button = new TagButton(effectiveDate, theQuestion, datum);
//			buttons.add(button);
//		}
		return buttons;
	}

//	private static Map<String, Datum> getDataForDate(Date date) {
//		return dataByDate.get(DataTimeline.getDateStr(date));
//	}

//	private Question getQuestion(String questionUid) {
//		if (knownQuestions.get(questionUid) != null) {
//			return knownQuestions.get(questionUid);
//		}
//		//TODO try to retrieve the question from the server
//		
//		//unable to find it
//		return null;
//	}
	
	public void setDatum(Datum datumToSave, Question questionAnswered) {
		dataTimeline.put(datumToSave);
		
		//next save to server
		Parcel parcel = newParcel();
		parcel.setDatum(datumToSave);
		parcel.setQuestion(questionAnswered);
		App.parcelClient.sendParcel(parcel, Constants.SERVERACTION_STORE_DATUM);
		
		//TODO save in local storage
	}
	
//	public HashMap<String, Day> createAllDays() {
//		allDays = new HashMap<String, Day>();
//		
//		Date startOfToday = Constants.DAY_FORMATTER.parse(Constants.DAY_FORMATTER.format(new Date()));
//		
//		//if no data, just return today's day
//		if (dataByDate == null || dataByDate.size()==0) {
//			Day day = createDay(startOfToday);
//			allDays.put(getDateString(startOfToday), day);
//			return allDays;
//		}
//				
//		//find earliest date
//		Date earliestDate = null;
//		for (String key : dataByDate.entrySet()) {
//		    String key = entry.getKey();
//		    earliestDate = Constants.DAY_FORMATTER.parse(key);
//		    break;
//		}
//		
//		Date date = new Date(startOfToday.getTime());
//		if (earliestDate != null) {
//			date = earliestDate;
//		}
//		
//		while(date.before(startOfToday) || date.equals(startOfToday)) {
//			addDayOntoEnd(date);
//			
//			//advance date 24 hours
//			date = new Date(date.getTime());
//			CalendarUtil.addDaysToDate(date, 1);
//		}
//		
//		return allDays;
//	}
	
//	public Day addDay(Date d) {
//		log.info("addDay: " + d);
//		Day day = createDay(d);
//		allDays.put(getDateString(d), day);
//		return day;
//	}
	
//	public Day addDayOntoBeginning(Date d) {
//		log.info("addDayOntoBeginning: " + d);
//		Day day = createDay(d);
//		allDays.put(getDateString(d), day);
//		return day;
//	}

	public Day createDay(Date date, boolean navigatingToPast) {
		Day day = new Day(date);
//		log.info("created Day:" + day);
		addTagsToDay(day, navigatingToPast);
//		allDays.put(DataTimeline.getDateStr(date), day);
		return day;
	}

	private void addTagsToDay(Day day, boolean navigatingToPast) {
		Date date = day.getTimepoint();
		Map<String, Datum> dataForDay = dataTimeline.getDataForDate(date);
		if (dataForDay != null) {
			for (Question q: questionQueue) {
				Datum d = dataForDay.get(q.getId());
				if (d==null) {
					d = getInferredAnswer(q, date, navigatingToPast);
				}
				TagButton tagButton = new TagButton(date, q, d);
				day.addTagButton(tagButton);
			}
		}
		else {
			for (Question q: questionQueue) {
				Datum inferredAnswer = getInferredAnswer(q, date, navigatingToPast);
				TagButton tagButton = new TagButton(day.getTimepoint(), q, inferredAnswer);
				day.addTagButton(tagButton);
			}
		}
	}

	private Datum getInferredAnswer(Question q, Date date, boolean navigatingToPast) {
		Datum modelAnswer = null;
		if (! navigatingToPast) {
			modelAnswer = dataTimeline.getPriorAnswer(q, date);
			if (modelAnswer == null) modelAnswer = dataTimeline.getSubsequentAnswer(q, date);
		} else {
			modelAnswer = dataTimeline.getSubsequentAnswer(q, date);
			if (modelAnswer == null) modelAnswer = dataTimeline.getPriorAnswer(q, date);
		}
		
		Datum inferredAnswer = DataTimeline.cloneDatum(modelAnswer);
		if (inferredAnswer!=null) {
			inferredAnswer.setAnswerStatus(AnswerStatus.INFERRED);
			inferredAnswer.setUpdated(new Date());
			inferredAnswer.setEffectiveDate(date);
			log.info("Inferred Answer for " + q.getAbbreviation() + "? " + inferredAnswer.getTextValue());
		}
		return inferredAnswer;
	}

//	public HashMap<String, Day> getAllDays() {
//		return allDays;
//	}
	
	public List<String> getPastAnswers(Question q) {
		return dataTimeline.getPastAnswers(q);
	}

	public void saveQuestion(Question question) {
		if (! questionQueue.contains(question)) {
			questionQueue.add(question);
		}
		knownQuestions.put(question.getId(), question);
		
//		//add the question to each day
//		for (String dayStr: getAllDays().keySet()) {
//			Day day = allDays.get(dayStr);
//			if (day != null) {
//				day.addQuestion(question);
//			}
//		}
		
		Parcel parcel = newParcel();
		parcel.setQuestion(question);
		App.parcelClient.sendParcel(parcel, Constants.SERVERACTION_STORE_QUESTION);
		//TODO save locally
	}

	public void refreshDataFromJson(String text) {
//		log.info("RRRRRRRRRRRRRRRRRRRRRRRRR refreshDataFromJson...");
		boolean gotoTagdayPlace = false;
		try {
			AutoBean<Parcel> parcelAB = AutoBeanCodex.decode(App.tagthedayAutoBeanFactory, Parcel.class, text);
		    Parcel parcel = parcelAB.as();
		    log.info("Received Session Token? " + parcel.getSessionToken());
		    Message message = parcel.getMessage();
		    
		    if (message == Message.SIGNUP_FAILURE_ACCTOUNT_EXISTS) {
		    	App.signupView.setMessage("Account already exists");
		    }
		    
		    if (parcel.getSessionToken() != null) {
		    	App.sessionToken = parcel.getSessionToken();
		    	gotoTagdayPlace = true;
		    }
		    
		    if (parcel.getMessage()==Message.ALL_DATA_RETRIEVED && parcel.getQuestions() != null) {
		    	log.info("Received question queue: " + parcel.getQuestions());
		    	setQuestionQueue(parcel.getQuestions());
		    	setKnownQuestions(parcel.getQuestions(), parcel.getOtherKnownQuestions());
		    }
		    if (parcel.getMessage()==Message.ALL_DATA_RETRIEVED && parcel.getData() != null) {
		    	 dataTimeline.setData(parcel.getData());
		    }
		    
		    if (parcel.getMessage()==Message.SAVED && parcel.getData() != null) {
		    	 dataTimeline.setData(parcel.getData());
		    }
		    
		    if (parcel.getParticipant() != null) {
		    	log.info("Received participant: " + parcel.getParticipant());
		    	App.participant = parcel.getParticipant();
		    }
		    
		    if (parcel.getMessage()==Message.MATCHING_QUESTIONS_RETURNED && parcel.getQuestions() != null) {
		    	App.questionForm.onSearchQuestionsReturns(parcel);
		    }
		    
		    if (gotoTagdayPlace) {
			    App.placeController.goTo(new TagdayPlace());
		    }
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error on refreshDataFromJson", e);
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

//	private void setData(List<Datum> data) {
////		dataByDate = new HashMap<String, List<Datum>>();
//		for (Datum d: data) {
//			String dayStr = Constants.DAY_FORMATTER.format(d.getEffectiveDate());
//			Map<String, Datum> dayData = dataByDate.get(dayStr);
//			if (dayData==null) dayData = new HashMap<String, Datum>();
//			String key = d.getQuestionId();
//			//TODO: support formulas: if (key==null) key = d.getFormulaId();
//			dayData.put(key, d);
//			dataByDate.put(dayStr, dayData);
//		}
//	}

//	public Day getDay(Date date) {
//		if (allDays == null) return null;
//		return allDays.get(DataTimeline.getDateStr(date));
//	}

//	public Day loadDay(Date d, boolean navigatingToPast) {
//		Day day = getDay(d);
//		if (day == null) {
//			//TODO load data for that day
//			day = createDay(d, navigatingToPast);
//		}
//		return day;
//	}
	
	/**
	 * If we ever decide to cache days, then uncomment the above
	 * @param d
	 * @param navigatingToPast
	 * @return
	 */
	public Day loadDay(Date d, boolean navigatingToPast) {
		Day day = createDay(d, navigatingToPast);
		return day;
	}

	public void removeQuestion(TagButton tagButton) {
//		App.tagdayView.removeTagButton(tagButton);
		Question question = tagButton.getQuestion();
		questionQueue.remove(question);
		knownQuestions.remove(question);
		Parcel parcel = newParcel();
		parcel.setQuestion(question);
		App.parcelClient.sendParcel(parcel, Constants.SERVERACTION_UNSUBSCRIBE);
	}
	
}
