package com.beyobe.web;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beyobe.client.beans.Parcel;
import com.beyobe.client.beans.SubscriptionType;
import com.beyobe.domain.Datum;
import com.beyobe.domain.Participant;
import com.beyobe.domain.Question;
import com.beyobe.domain.Subscription;
import com.beyobe.repository.DatumRepository;
import com.beyobe.repository.QuestionRepository;
/**
 * Handle all requests from the GWT client
 * TODO unsubscribe from questions
 * TODO windowing data to avoid overload after prolonged data accumulation
 * TODO change password
 * TODO logout
 * TODO expire sessionToken at 2 weeks?
 */
@RequestMapping("/service/**")
@Controller
public class ServiceController {

	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private DatumRepository datumRepository;
	
	private static final Logger log = Logger.getLogger(ServiceController.class);
	
	@ModelAttribute("clientIpAddress")
	public String populateClientIpAddress(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

//	@RequestMapping(method = RequestMethod.GET)
//	public String handleRequest(
//			@ModelAttribute("clientIpAddress") String clientIpAddress,
//			ModelMap model) throws Exception { 
//
//		// handle request without referencing servlet API
//
//		return "view";
//	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> login(
			@RequestBody String jsonRequest,
			@ModelAttribute("clientIpAddress") String clientIpAddress) {
//	public ResponseEntity<java.lang.String> login(
//			@RequestBody String jsonRequest) {
		log.info("login service invoked with json: " + jsonRequest);
	 	Parcel parcel = null;
	 	String username = null;
	 	String hashedPassword = null;
	 	try {
			parcel = Parcel.fromJsonToParcel(jsonRequest);
			username = parcel.getUsername();
			String password = parcel.getPassword();
			Participant dummyParticipant = new Participant();
			dummyParticipant.setPassword(password);
//			hashedPassword = Participant.hashString(password, username);
			hashedPassword = dummyParticipant.getPassword();
		} catch (Exception e1) {
			HttpHeaders headers = new HttpHeaders();
			log.error("Bad request: incoming JSON=" + jsonRequest, e1);
		    headers.add("Content-Type", "application/json");
			return new ResponseEntity<String>(null, headers, HttpStatus.BAD_REQUEST);
		}
	 	Participant participant = null;
	 	try {
			participant = Participant.findParticipantsByUsernameEqualsAndPasswordEquals(username, hashedPassword).getSingleResult();
			assert(participant.getEnabled()==true);
	 	} catch (Exception e) {
			//leave as null
			log.warn("Login failure: username=" + username + "; passwordHash=" + hashedPassword);
			HttpHeaders headers = new HttpHeaders();
		    headers.add("Content-Type", "application/json");
			return new ResponseEntity<String>(null, headers, HttpStatus.UNAUTHORIZED);
		}
	 	//save the session token for future requests
	 	String sessionToken = UUID.randomUUID().toString();
	 	participant.setSessionToken(sessionToken);
	 	participant.setClientIpAddress(clientIpAddress);
	 	participant.merge();
	 	
	 	log.trace("saved participant");
	 	
	 	//prepare the parcel for return
	 	Parcel returnParcel = new Parcel();
	 	returnParcel.setSessionToken(sessionToken);
	 	returnParcel.setParticipant(participant);
	 	log.info("set session token: " + sessionToken);
	 	
	 	try {
			List<Question> questionQueue = questionRepository.getSubscribedQuestions(participant.getId(), SubscriptionType.ACTIVE_DAILY);
			returnParcel.setQuestionQueue(questionQueue);
			log.trace("login service: set question queue");
		} catch (Exception e) {
			log.error("login service: ERROR getting question queue", e);
		}
		
		try {
			List<Question> inactiveQuestions = questionRepository.getSubscribedQuestions(participant.getId(), SubscriptionType.INACTIVE);
			returnParcel.setOtherKnownQuestions(inactiveQuestions);
			log.trace("set inactive questions");
		} catch (Exception e) {
			log.error("login service: ERROR getting inactive questions", e);
		}
		
	 	try {
			List<Datum> data = datumRepository.getParticipantData(participant.getId());
			returnParcel.setData(data);
			log.trace("set data");
		} catch (Exception e) {
			log.error("ERROR getting participant data", e);
		}
	 	
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
	    String returnJson = returnParcel.toJson();
	    log.info("login service sending back: " + returnJson);
	    return new ResponseEntity<String>(returnJson, headers, HttpStatus.OK);
	}
	 
	
	/**
	 * Checks if the current 
	 * Receives a Question object
	 * Retrieves the participant, given the participantId
	 * 
	 * @param clientIpAddress
	 * @param jsonRequest
	 * @return
	 */
	@RequestMapping(value = "/storeQuestion", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> storeQuestion(
			@ModelAttribute("clientIpAddress") String clientIpAddress,
			@RequestBody String jsonRequest) {
		log.info("storeQuestion service invoked with json: " + jsonRequest);
	 	Parcel parcel = null;
		try {
			parcel = Parcel.fromJsonToParcel(jsonRequest);
		} catch (Exception e1) {
			log.error("storeQuestion service: Error parsing JSON: " + jsonRequest, e1);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json");
			return new ResponseEntity<String>(null, headers, HttpStatus.BAD_REQUEST);
		}
//	 	log.info("got parcel: " + parcel);
	 	String sessionToken = parcel.getSessionToken();
	 	Participant participant = null;
	 	try {
	 		//TODO add session expiration datetime
	 		log.info("storeQuestion service getting current user participant...");
			participant = Participant.findParticipantsBySessionTokenEqualsAndClientIpAddressEquals(sessionToken, clientIpAddress).getSingleResult();
			log.info("storeQuestion service participant:" + participant);
			assert(participant.getEnabled()==true);
	 	} catch (Exception e) {
			//leave as null
			log.warn("Session not recognized or expired: sessionToken=" + sessionToken + "; clientIpAddress=" + clientIpAddress);
			HttpHeaders headers = new HttpHeaders();
		    headers.add("Content-Type", "application/json");
			return new ResponseEntity<String>(null, headers, HttpStatus.UNAUTHORIZED);
		}
	 	
	 	Question q = null;
		try {
			q = parcel.getQuestion();
			log.info("storeQuestion service received question: " + q);
			if (q.getOwnerId()==null) q.setOwnerId(participant.getId());
		} catch (Exception e1) {
			log.error("Unable to get Question from parcel", e1);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json");
			return new ResponseEntity<String>(null, headers, HttpStatus.BAD_REQUEST);
		}
		
	 	return saveQuestionAndSubscribe(q, participant);
	}
	
	
	
	

	@RequestMapping(value = "/storeDatum", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> storeDatum(
			@ModelAttribute("clientIpAddress") String clientIpAddress,
			@RequestBody String jsonRequest) {
		log.info("storeDatum service invoked with json: " + jsonRequest);
	 	Parcel parcel;
		String sessionToken;
		try {
			parcel = Parcel.fromJsonToParcel(jsonRequest);
			sessionToken = parcel.getSessionToken();
		} catch (Exception e1) {
			log.error("storeDatum service: Unable to parse json into parcel:" + jsonRequest, e1);
			HttpHeaders headers = new HttpHeaders();
		    headers.add("Content-Type", "application/json");
			return new ResponseEntity<String>(null, headers, HttpStatus.UNAUTHORIZED);
		}
		log.info("storeDatum service: got parcel: " + parcel);
	 	Participant participant = null;
	 	try {
	 		//TODO add session expiration datetime
			participant = Participant.findParticipantsBySessionTokenEqualsAndClientIpAddressEquals(sessionToken, clientIpAddress).getSingleResult();
			log.info("storeDatum service: got participant: " + participant);
			assert(participant.getEnabled()==true);
	 	} catch (Exception e) {
			//leave as null
			log.warn("Session not recognized or expired: sessionToken=" + sessionToken + "; clientIpAddress=" + clientIpAddress);
			HttpHeaders headers = new HttpHeaders();
		    headers.add("Content-Type", "application/json");
			return new ResponseEntity<String>(null, headers, HttpStatus.UNAUTHORIZED);
		}
	 	
	 	Question q = null;
		try {
			q = parcel.getQuestion();
			log.info("storeDatum service received question: " + q);
			if (q.getOwnerId()==null) q.setOwnerId(participant.getId());
		} catch (Exception e1) {
			log.error("Unable to get Question from parcel", e1);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json");
			return new ResponseEntity<String>(null, headers, HttpStatus.BAD_REQUEST);
		}
		
		Question existingQuestion = questionRepository.findOne(q.getId());
		mergeAndSaveQuestion(q, existingQuestion);
	 	try {
			Datum d = parcel.getDatum();
			d.setQuestion(existingQuestion);
			d.setParticipant(participant);
			log.info("storeDatum service: got datum: " + d);
			Datum existingDatum = datumRepository.findOne(d.getId());
			//TODO some day remove this nasty workaround because JPA gives error when we try to update a object (nutty thing to try to do I know)
			if (existingDatum == null) {
				datumRepository.save(d);
				datumRepository.flush();
			} else {
				mergeAndSaveDatum(d, existingDatum);
			}
			
		} catch (Exception e) {
			log.error("Unable to get and save datum", e);
			HttpHeaders headers = new HttpHeaders();
		    headers.add("Content-Type", "application/json");
			return new ResponseEntity<String>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 	
//	 	//prepare the parcel for return
//	 	Parcel returnParcel = new Parcel();
//	 	
//	    HttpHeaders headers = new HttpHeaders();
//	    headers.add("Content-Type", "application/json");
//	    String returnJson = returnParcel.toJson();
//	    log.info("storeDatum service sending back: " + returnJson);
//	    return new ResponseEntity<String>(returnJson, headers, HttpStatus.OK);
	 	
	 	//save or update the question associated with this answer
	 	return saveQuestionAndSubscribe(q, participant);
	}
	
	
	private void mergeAndSaveDatum(Datum newQuestion, Datum existingQuestion) {
		if (newQuestion.getAnswerStatus() != null) {
			existingQuestion.setAnswerStatus(newQuestion.getAnswerStatus());
		}
		if (newQuestion.getChoice() != null) {
			existingQuestion.setChoice(newQuestion.getChoice());
		}
		if (newQuestion.getUpdated() != null) {
			existingQuestion.setUpdated(newQuestion.getUpdated());
		}
		if (newQuestion.getDataType() != null) {
			existingQuestion.setDataType(newQuestion.getDataType());
		}
		if (newQuestion.getEffectiveDate() != null) {
			existingQuestion.setEffectiveDate(newQuestion.getEffectiveDate());
		}
		if (newQuestion.getFormula() != null) {
			existingQuestion.setFormula(newQuestion.getFormula());
		}
		if (newQuestion.getIntegerValue() != null) {
			existingQuestion.setIntegerValue(newQuestion.getIntegerValue());
		}
		if (newQuestion.getNormalizedValue() != null) {
			existingQuestion.setNormalizedValue(newQuestion.getNormalizedValue());
		}
		if (newQuestion.getNumericValue() != null) {
			existingQuestion.setNumericValue(newQuestion.getNumericValue());
		}
		if (newQuestion.getQuestionConcept() != null) {
			existingQuestion.setQuestionConcept(newQuestion.getQuestionConcept());
		}
		if (newQuestion.getTextValue() != null) {
			existingQuestion.setTextValue(newQuestion.getTextValue());
		}
		if (newQuestion.getUnit() != null) {
			existingQuestion.setUnit(newQuestion.getUnit());
		}
		if (newQuestion.getUpdatedBy() != null) {
			existingQuestion.setUpdatedBy(newQuestion.getUpdatedBy());
		}
		datumRepository.saveAndFlush(existingQuestion);
	}
	
	private Question mergeAndSaveQuestion(Question newQuestion, Question existingQuestion) {
		if (newQuestion.getAbbreviation() != null) {
			existingQuestion.setAbbreviation(newQuestion.getAbbreviation());
		}
		if (newQuestion.getLatency() != null) {
			existingQuestion.setLatency(newQuestion.getLatency());
		}
		if (newQuestion.getUpdated() != null) {
			existingQuestion.setUpdated(newQuestion.getUpdated());
		}
//		if (newQuestion.getDataType() != null) {
//			existingQuestion.setDataType(newQuestion.getDataType());
//		}
		if (newQuestion.getLongForm() != null) {
			existingQuestion.setLongForm(newQuestion.getLongForm());
		}
		if (newQuestion.getMaxLength() != null) {
			existingQuestion.setMaxLength(newQuestion.getMaxLength());
		}
		if (newQuestion.getMinValue() != null) {
			existingQuestion.setMinValue(newQuestion.getMinValue());
		}
		if (newQuestion.getMaxValue() != null) {
			existingQuestion.setMaxValue(newQuestion.getMaxValue());
		}
//		if (newQuestion.getMeasurement() != null) {
//			existingQuestion.setMeasurement(newQuestion.getMeasurement());
//		}
		if (newQuestion.getPriority() != null) {
			existingQuestion.setPriority(newQuestion.getPriority());
		}
		questionRepository.saveAndFlush(existingQuestion);
		return existingQuestion;
	}

	private ResponseEntity<String> saveQuestionAndSubscribe(Question q, Participant participant) {
		
		//test that current user is the owner of this question or is admin
	 	Question existingQuestion = questionRepository.findOne(q.getId());
	 	Question theQuestion = q;
	 	if (existingQuestion != null) {
	 		if (! participant.getId().equals(existingQuestion.getOwnerId())) {
	 			log.warn("storeQuestion service: insufficient privileges to modify existing question with this one:" + q);
				HttpHeaders headers = new HttpHeaders();
			    headers.add("Content-Type", "application/json");
				return new ResponseEntity<String>(null, headers, HttpStatus.UNAUTHORIZED);
	 		}
	 		 theQuestion = mergeAndSaveQuestion(q, existingQuestion);
	 	} else {
	 		//new question: current user must be owner
	 		q.setOwner(participant);
		 	questionRepository.save(theQuestion);
		 	questionRepository.flush();
		 	log.info("storeQuestion service saved new question: " + q);
	 	}
	 	
	 	//check to see if subscription already exists.  If not create a new one
	 	Subscription existingSubscription = null;
	 	 try {
			existingSubscription = 
//					Subscription.findSubscriptionsByQuestionIdEqualsAndParticipantIdEquals(q.getId(), participant.getId()).getSingleResult();
					Subscription.findSubscriptionsByQuestionEqualsAndParticipantEquals(q, participant).getSingleResult();

	 	 } catch (Exception e1) {
			log.info("No existing subscription found for question: " + q.getId());
		}
	 	if (existingSubscription == null) {
		 	Subscription subscription = new Subscription();
		 	subscription.setCreated(new Date());
		 	subscription.setCreatedBy(participant.getId());
//		 	subscription.setQuestionId(q.getId());
		 	subscription.setQuestion(theQuestion);
		 	subscription.setSubscriptionType(SubscriptionType.ACTIVE_DAILY);
//		 	subscription.setParticipantId(participant.getId());
		 	subscription.setParticipant(participant);
		 	log.info("storeQuestion service to save subscription: " + subscription);
		 	try {
				subscription.persist();
				subscription.flush();
				log.info("storeQuestion service saved subscription");
			} catch (Exception e) {
				log.error("storeQuestion service unable to save subscription:" + subscription, e);
				HttpHeaders headers = new HttpHeaders();
			    headers.add("Content-Type", "application/json");
				return new ResponseEntity<String>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
			}
	 	}
	 	
	 	//prepare the parcel for return
	 	Parcel returnParcel = new Parcel();
	 	
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
	    String returnJson = returnParcel.toJson();
	    log.info("storeQuestion service sending back: " + returnJson);
	    return new ResponseEntity<String>(returnJson, headers, HttpStatus.OK);
	}
	
	/*
    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    @RequestMapping
    public String index() {
        return "service/index";
    }
    
    @RequestMapping(value = "/subscribedQuestions", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getSubscribedQuestions(@RequestParam Long participantId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        List<Question> questions = questionRepository.getSubscribedQuestions(participantId);
        return new ResponseEntity<String>(Question.toJsonArray(questions), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/availableSubscribedQuestions", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getAvailableSubscribedQuestions(@RequestParam Long participantId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        List<Question> questions = questionRepository.getSubscribedQuestions(participantId);
        return new ResponseEntity<String>(Question.toJsonArray(questions), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/searchQuestions", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getSubscribedQuestions(@RequestParam Long participantId, @RequestParam String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        QueryResponse queryResponse = Question.search(query);
        List<Question> questions = queryResponseToQuestions(queryResponse);
        return new ResponseEntity<String>(Question.toJsonArray(questions), headers, HttpStatus.OK);
    }

    private List<org.inqle.domain.Question> queryResponseToQuestions(QueryResponse queryResponse) {
        List<Question> questions = new ArrayList<Question>();
        for (SolrDocument sd : queryResponse.getResults()) {
            questions.add(Question.findQuestion((Long) sd.getFieldValue("blurb.id_l")));
        }
        return questions;
    }
    */
	
	public static void main(String[] args) {
		UUID uuid = UUID.fromString("329BD5D9-B793-49E2-8206-36B65DEEC216");
		System.out.println("UUID is a UUID");
	}
}
