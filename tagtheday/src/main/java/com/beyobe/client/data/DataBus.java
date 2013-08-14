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
import com.google.gwt.user.client.Window;
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

	private static Map<String, Question> unsavedQuestions = new HashMap<String, Question>();
	private static Map<String, Datum> unsavedData = new HashMap<String, Datum>();
	
	public Participant participant;

	private DataTimeline dataTimeline = new DataTimeline();
	
	public DataBus() {
		//TODO load questions and data from local storage
		//TODO sync local questions and data with remote DB
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
	
	public List<TagButton> getTagButtonsForDate(Date effectiveDate) {
		List<TagButton> buttons = new ArrayList<TagButton>();
		Map<String, Datum> data = dataTimeline.getDataForDate(effectiveDate);
		if (data==null) return null;
		for (Question q: questionQueue) {
			Datum d = data.get(q.getId());
			TagButton button = new TagButton(effectiveDate, q, d);
			buttons.add(button);
		}
		return buttons;
	}
	
	public void setDatum(Datum datumToSave, Question questionAnswered) {
		dataTimeline.put(datumToSave);
		
		//next save to server
		Parcel parcel = newParcel();
		parcel.setDatum(datumToSave);
		parcel.setQuestion(questionAnswered);
		parcel.setAction(Constants.SERVERACTION_STORE_DATUM);
		App.parcelClient.sendParcel(parcel);
		
		//TODO save in local storage
	}
	
	public Day createDay(Date date, boolean navigatingToPast) {
		Day day = new Day(date);
		addTagsToDay(day, navigatingToPast);
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

	public List<String> getPastAnswers(Question q) {
		return dataTimeline.getPastAnswers(q);
	}

	public void saveQuestion(Question question) {
		if (! questionQueue.contains(question)) {
			questionQueue.add(question);
		}
		knownQuestions.put(question.getId(), question);
		log.info("Saved question.  Queue=" + questionQueue);
		Parcel parcel = newParcel();
		parcel.setQuestion(question);
		parcel.setAction(Constants.SERVERACTION_STORE_QUESTION);
		App.parcelClient.sendParcel(parcel);
		//TODO save locally
	}
	
	public Parcel newParcel() {
	    AutoBean<Parcel> parcelAB = App.tagthedayAutoBeanFactory.parcel();
	    Parcel parcel = parcelAB.as();
	    parcel.setSessionToken(App.sessionToken);
	    return parcel;
	}

	private void setKnownQuestions(List<Question> qq, List<Question> otherKnownQuestions) {
//		knownQuestions = new HashMap<String, Question>();
		if (qq != null) {
			for (Question q: qq) {
				knownQuestions.put(q.getId(), q);
			}
		}
		if (otherKnownQuestions==null) return;
		for (Question q: otherKnownQuestions) {
			knownQuestions.put(q.getId(), q);
		}
		
	}

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
		parcel.setAction(Constants.SERVERACTION_UNSUBSCRIBE);
		App.parcelClient.sendParcel(parcel);
	}

	public void handleServerResponse(Parcel parcel) {
		boolean gotoTagdayPlace = false;
		try {
		    Message message = parcel.getMessage();
		    
		    if (parcel.getSessionToken() != null) {
		    	App.sessionToken = parcel.getSessionToken();
		    	gotoTagdayPlace = true;
		    }
		    
		    if (parcel.getMessage()==Message.MATCHING_QUESTIONS_RETURNED) {
		    	App.questionForm.onSearchQuestionsReturns(parcel);
		    	return;
		    }
		    
//		    if (parcel.getMessage()==Message.ALL_DATA_RETRIEVED && parcel.getQuestions() != null) {
		    if ((parcel.getMessage()==Message.LOGIN_SUCCEEDED || parcel.getMessage()==Message.SIGNED_UP) && parcel.getQuestions() != null) {
		    	log.info("Received question queue: " + parcel.getQuestions());
		    	setQuestionQueue(parcel.getQuestions());
		    	setKnownQuestions(parcel.getQuestions(), parcel.getOtherKnownQuestions());
		    }
//		    if (parcel.getMessage()==Message.ALL_DATA_RETRIEVED && parcel.getData() != null) {
	    	if ((parcel.getMessage()==Message.LOGIN_SUCCEEDED || parcel.getMessage()==Message.SIGNED_UP) && parcel.getData() != null) {
		    	 dataTimeline.setData(parcel.getData());
		    }
		    
//		    if (parcel.getMessage()==Message.SAVED && parcel.getData() != null) {
//		    	 dataTimeline.setData(parcel.getData());
//		    }
		    
		    if (parcel.getParticipant() != null) {
		    	log.info("Received participant: " + parcel.getParticipant());
		    	App.participant = parcel.getParticipant();
		    }
		    
		    if (gotoTagdayPlace) {
			    App.placeController.goTo(new TagdayPlace());
		    }
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error on refreshDataFromJson", e);
		}
	}

	public void handleConnectionError(Parcel parcel) {
		//TODO undo the change in the UI
		//TODO put the unsaved object into a queue, and try to save that queue each time
		
		if (Constants.SERVERACTION_SEARCH_QUESTIONS.equals(parcel.getAction())) {
			App.questionForm.onSearchQuestionError();
			return;
		}
	    
	    Window.alert("Error.  Your last operation was not saved.");
	}

	public void handleServerException(Parcel parcel) {
		Message message = parcel.getMessage();
		
		if (message == Message.SIGNUP_FAILURE_ACCTOUNT_EXISTS) {
	    	App.signupView.setMessage("Account already exists");
	    }
		
	    //TODO handle more kinds of exceptions
		if (message == Message.TOO_MANY_QUESTIONS && parcel.getQuestion() != null) {
	    	Window.alert("You have created too many questions.  Unable to store any more.");
	    	//delete the question?
//	    	questionQueue.remove(parcel.getQuestion());
//	    	App.tagdayView.removeQuestion(parcel.getQuestion().getId());
	    	return;
	    }
	    if (message == Message.TOO_MANY_DATA && parcel.getDatum() != null) {
	    	Window.alert("You have answered too many questions.  Unable to store any more answers.");
	    	//delete the datum?
//	    	dataTimeline.removeDatum(parcel.getDatum().getId());
//	    	App.tagdayView.removeDatum(parcel.getDatum().getId());
	    	return;
	    }
	    
	    if (Constants.SERVERACTION_SEARCH_QUESTIONS.equals(parcel.getAction())) {
			App.questionForm.onSearchQuestionError();
			return;
		}
	    
	    Window.alert("Error from Beyobe server: " + parcel.getMessage().name());
	    
	    storeUnsavedInfo(parcel);
	}

	public void handleTimeout(Parcel parcel) {
		if (Constants.SERVERACTION_SEARCH_QUESTIONS.equals(parcel.getAction())) {
			App.questionForm.onSearchQuestionError();
			return;
		}
			
		Window.alert("Unable to connect: " + parcel);
		storeUnsavedInfo(parcel);
	}

	private void storeUnsavedInfo(Parcel parcel) {
		Datum d = parcel.getDatum();
		if (d!= null) {
			Window.alert("Saving datum: '" + d.getTextValue() + "' in the unsavedData queue");
			unsavedData.put(d.getId(), d);
		}
		Question q = parcel.getQuestion();
		if (q!=null) {
			Window.alert("Saving question: '" + q.getAbbreviation() + "' in the unsavedQuestions queue");
			unsavedQuestions.put(q.getId(), q);
		}
	}
	
}
