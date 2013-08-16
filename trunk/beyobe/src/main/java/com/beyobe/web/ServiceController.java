package com.beyobe.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beyobe.client.beans.Message;
import com.beyobe.client.beans.Parcel;
import com.beyobe.client.beans.SubscriptionType;
import com.beyobe.client.beans.UserRole;
import com.beyobe.domain.Datum;
import com.beyobe.domain.Participant;
import com.beyobe.domain.Question;
import com.beyobe.domain.Subscription;
import com.beyobe.repository.DatumRepository;
import com.beyobe.repository.QuestionRepository;
/**
 * Handle all requests from the GWT client
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

	private static final int MAXIMUM_SESSION_DAYS = 14;

	private static final long MAXIMUM_DATA_PER_USER = 100000;

	private static final long MAXIMUM_QUESTIONS_PER_USER = 10000;
	
	@ModelAttribute("clientIpAddress")
	public String populateClientIpAddress(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

	
	
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> signup(
			@RequestBody String jsonRequest,
			@ModelAttribute("clientIpAddress") String clientIpAddress) {
	 	Parcel parcel = null;
	 	String username = null;
	 	String password = null;
	 	Parcel returnParcel = new Parcel();
	 	try {
			parcel = Parcel.fromJsonToParcel(jsonRequest);
			username = parcel.getUsername();
			password = parcel.getPassword();
			log.info("signup service invoked for username: " + username);
		} catch (Exception e1) {
			log.error("Bad request: incoming JSON=" + jsonRequest, e1);
			returnParcel.setMessage(Message.BAD_REQUEST);
			return respond(returnParcel, HttpStatus.BAD_REQUEST);
		}
	 	Participant participant = null;
	 	//prepare the parcel for return
	 	
	 	boolean acctAlreadyExists = true;
	 	try {
			try {
				Participant shouldNotExist = Participant.findParticipantsByUsernameEquals(username).getSingleResult();
				if (shouldNotExist != null) {
					acctAlreadyExists = true;
				}
			} catch (NoResultException dne) {
				acctAlreadyExists = false;
			} catch (EmptyResultDataAccessException erdae) {
				acctAlreadyExists = false;
			}
		} catch (Exception e1) {
	 		log.error("Exception finding already existing account", e1);
			acctAlreadyExists = true;
		} 
	 	if (acctAlreadyExists) {
			log.warn("Username already exists=" + username);
			returnParcel.setMessage(Message.SIGNUP_FAILURE_ACCTOUNT_EXISTS);
	 		return respond(returnParcel, HttpStatus.UNAUTHORIZED);
	 	}
	 	
	 	//save the session token for future requests
	 	String sessionToken = UUID.randomUUID().toString();
	 	participant = new Participant();
	 	participant.setUsername(username);
	 	participant.setPassword(password);
	 	participant.setEmail(username);
	 	participant.setRole(UserRole.ROLE_BASIC);
	 	participant.setSessionToken(sessionToken);
	 	participant.setEnabled(true);
	 	participant.setClientIpAddress(clientIpAddress);
	 	participant.persist();
	 	
	 	log.info("saved NEW participant: " + username);
	 	
	 	returnParcel.setSessionToken(sessionToken);
	 	returnParcel.setParticipant(participant);
	 	returnParcel.setMessage(Message.SIGNED_UP);
	 	log.info("set session token: " + sessionToken);
	 	
	    log.info("signup service sending back: " + returnParcel.toJson());
	    return respond(returnParcel, HttpStatus.OK);
	}
	
	
	
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json")
//	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<java.lang.String> login(
			@RequestBody String jsonRequest,
			@ModelAttribute("clientIpAddress") String clientIpAddress) {
//	public ResponseEntity<java.lang.String> login(
//			@RequestBody String jsonRequest) {
//		log.info("login service invoked with json: " + jsonRequest);
	 	Parcel parcel = null;
	 	String username = null;
	 	String hashedPassword = null;
	 	Parcel returnParcel = new Parcel();
	 	try {
			parcel = Parcel.fromJsonToParcel(jsonRequest);
			username = parcel.getUsername();
			String password = parcel.getPassword();
			log.info("login service invoked for username: " + username);
			Participant dummyParticipant = new Participant();
			dummyParticipant.setPassword(password);
//			hashedPassword = Participant.hashString(password, username);
			hashedPassword = dummyParticipant.getPassword();
		} catch (Exception e1) {
			log.error("Bad request: incoming JSON=" + jsonRequest, e1);
			returnParcel.setMessage(Message.BAD_REQUEST);
			return respond(returnParcel, HttpStatus.BAD_REQUEST);
		}
	 	Participant participant = null;
	 	try {
	 		participant = Participant.findParticipantsByUsernameEqualsAndPasswordEqualsAndEnabledNot(username, hashedPassword, false).getSingleResult();
			assert(participant.getEnabled()==true);
	 	} catch (Exception e) {
			//leave as null
			log.warn("Login failure: username=" + username + "; passwordHash=" + hashedPassword);
			returnParcel.setMessage(Message.LOGIN_FAILED);
			return respond(returnParcel, HttpStatus.UNAUTHORIZED);
		}
	 	//save the session token for future requests
	 	String sessionToken = UUID.randomUUID().toString();
	 	participant.setSessionToken(sessionToken);
	 	participant.setSessionDate(new Date());
	 	participant.setClientIpAddress(clientIpAddress);
	 	participant.merge();
	 	
	 	log.trace("saved participant");
	 	
	 	//prepare the parcel for return
	 	returnParcel.setSessionToken(sessionToken);
	 	returnParcel.setParticipant(participant);
	 	log.info("login service: user=" + participant.getUsername() + "; set session token: " + sessionToken);
	 	
	 	try {
			List<Question> questionQueue = questionRepository.getSubscribedQuestions(participant.getId(), SubscriptionType.ACTIVE_DAILY);
			returnParcel.setQuestions(questionQueue);
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
			returnParcel.setMessage(Message.ALL_DATA_RETRIEVED);
			log.trace("set data");
		} catch (Exception e) {
			log.error("ERROR getting participant data", e);
		}
	 	returnParcel.setMessage(Message.LOGIN_SUCCEEDED);
	    log.info("login service sending back: " + returnParcel.toJson());
	    return respond(returnParcel, HttpStatus.OK);
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
	 	Parcel returnParcel = new Parcel();
		try {
			parcel = Parcel.fromJsonToParcel(jsonRequest);
		} catch (Exception e1) {
			log.error("storeQuestion service: Error parsing JSON: " + jsonRequest, e1);
			returnParcel.setMessage(Message.BAD_REQUEST);
			return respond(returnParcel, HttpStatus.BAD_REQUEST);
		}
//	 	log.info("got parcel: " + parcel);
	 	String sessionToken = parcel.getSessionToken();
	 	Participant participant = null;
	 	try {
	 		//TODO add session expiration datetime
	 		log.info("storeQuestion service getting current user participant...");
			participant = Participant.findParticipantsBySessionTokenEqualsAndSessionDateGreaterThanAndClientIpAddressEqualsAndEnabledNot(sessionToken, getEarliestSessionDate(), clientIpAddress, false).getSingleResult();
			log.info("storeQuestion service participant:" + participant);
			assert(participant.getEnabled()==true);
	 	} catch (Exception e) {
			//leave as null
			log.warn("Session not recognized or expired: sessionToken=" + sessionToken + "; clientIpAddress=" + clientIpAddress);
			returnParcel.setMessage(Message.INVALID_SESSION);
			return respond(returnParcel, HttpStatus.UNAUTHORIZED);
		}
	 	Long countQuestions = questionRepository.countQuestionsOwned(participant.getId());
	 	if (countQuestions > MAXIMUM_QUESTIONS_PER_USER) {
	 		log.warn("Participant " + participant.getUsername() + " has too many questions (" + countQuestions + ")");
		    returnParcel.setQuestion(parcel.getQuestion());
		    returnParcel.setMessage(Message.TOO_MANY_QUESTIONS);
		    return respond(returnParcel, HttpStatus.UNAUTHORIZED);
	 	}
	 	Question q = null;
		try {
			q = parcel.getQuestion();
			log.info("storeQuestion service received question: " + q);
			if (q.getOwnerId()==null) q.setOwnerId(participant.getId());
		} catch (Exception e1) {
			log.error("Unable to get Question from parcel", e1);
		    returnParcel.setMessage(Message.BAD_REQUEST);
		    return respond(returnParcel, HttpStatus.BAD_REQUEST);
		}
//	 	return saveQuestionAndSubscribe(returnParcel, q, participant);
		
		Question existingQuestion = questionRepository.findOne(q.getId());
		boolean loadData = false;
	 	if (existingQuestion != null) {
	 		loadData = true;
	 		if (! participant.getId().equals(existingQuestion.getOwnerId()) && ! (participant.getRole() == UserRole.ROLE_ADMIN)) {
	 			log.warn("storeQuestion service: insufficient privileges to modify existing question with this one:" + q);
	 		} else {
//	 			theQuestion = mergeAndSaveQuestion(q, existingQuestion);
	 			q = mergeAndSaveQuestion(q, existingQuestion);
	 		}
	 	} else {
	 		//new question: current user must be owner
	 		q.setOwner(participant);
//		 	questionRepository.save(theQuestion);
	 		questionRepository.save(q);
		 	questionRepository.flush();
		 	log.info("storeQuestion service saved new question: " + q);
	 	}
	 	
	 	//subscribe
	 	subscribe(participant, q);
	 	
	 	//signal to client that this question is saved
	 	List<String> savedQuestions = new ArrayList<String>();
	 	savedQuestions.add(q.getId());
	 	returnParcel.setSavedQuestions(savedQuestions);
	 	
	 	if (loadData) {
	 		List<Datum> data = datumRepository.getParticipantDataForQuestion(participant.getId(), q.getId());
	 		returnParcel.setData(data);
	 	}
	 	
	 	returnParcel.setMessage(Message.SAVED);
	    String returnJson = returnParcel.toJson();
	    log.info("storeQuestion service sending back: " + returnJson);
	    return respond(returnParcel, HttpStatus.OK);
	}
	

	@RequestMapping(value = "/storeDatum", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> storeDatum(
			@ModelAttribute("clientIpAddress") String clientIpAddress,
			@RequestBody String jsonRequest) {
		log.info("storeDatum service invoked with json: " + jsonRequest);
	 	Parcel parcel = null;
	 	Parcel returnParcel = new Parcel();
		String sessionToken;
		try {
			parcel = Parcel.fromJsonToParcel(jsonRequest);
			sessionToken = parcel.getSessionToken();
		} catch (Exception e1) {
			log.error("storeDatum service: Unable to parse json into parcel:" + jsonRequest, e1);
			returnParcel.setMessage(Message.BAD_REQUEST);
			return respond(returnParcel, HttpStatus.BAD_REQUEST);
		}
		log.info("storeDatum service: got parcel: " + parcel);
	 	Participant participant = null;
	 	try {
	 		//TODO add session expiration datetime
			participant = Participant.findParticipantsBySessionTokenEqualsAndSessionDateGreaterThanAndClientIpAddressEqualsAndEnabledNot(sessionToken, getEarliestSessionDate(), clientIpAddress, false).getSingleResult();
			log.info("storeDatum service: got participant: " + participant);
			assert(participant.getEnabled()==true);
	 	} catch (Exception e) {
			//leave as null
			log.warn("Session not recognized or expired: sessionToken=" + sessionToken + "; clientIpAddress=" + clientIpAddress);
			returnParcel.setMessage(Message.INVALID_SESSION);
			return respond(returnParcel, HttpStatus.UNAUTHORIZED);
		}
	 	
	 	//make sure the user has not exceeded quota
	 	long countData = datumRepository.countParticipantData(participant.getId());
	 	if (countData > MAXIMUM_DATA_PER_USER) {
	 		log.warn("Participant " + participant.getUsername() + " has too many data (" + countData + ")");
		    returnParcel.setDatum(parcel.getDatum());
		    returnParcel.setMessage(Message.TOO_MANY_DATA);
			return respond(returnParcel, HttpStatus.UNAUTHORIZED);
	 	}
	 	log.info("User " + participant.getUsername() + " created " + questionRepository.countQuestionsOwned(participant.getId()) + " questions and " + countData + " data.");
	 	Question q = null;
		try {
			q = parcel.getQuestion();
			log.info("storeDatum service received question: " + q);
			if (q.getOwnerId()==null) q.setOwnerId(participant.getId());
		} catch (Exception e1) {
			log.error("Unable to get Question from parcel", e1);
			returnParcel.setMessage(Message.BAD_REQUEST);
		    return respond(returnParcel, HttpStatus.BAD_REQUEST);
		}
		
		Question existingQuestion = questionRepository.findOne(q.getId());
		q = mergeAndSaveQuestion(q, existingQuestion);
		
		//signal to client that this question is saved
	 	List<String> savedQuestions = new ArrayList<String>();
	 	savedQuestions.add(q.getId());
	 	returnParcel.setSavedQuestions(savedQuestions);
	 	
		Datum d = null;
	 	try {
			d = parcel.getDatum();
//			d.setQuestion(existingQuestion);
			d.setQuestion(q);
			d.setParticipant(participant);
			log.info("storeDatum service: got datum: " + d);
			Datum existingDatum = datumRepository.findOne(d.getId());
			//TODO some day remove this nasty workaround because JPA gives error when we try to update a object (nutty thing to try to do I know)
//			if (existingDatum == null) {
//				datumRepository.save(d);
//				datumRepository.flush();
//			} else {
				mergeAndSaveDatum(d, existingDatum);
//			}
			
		} catch (Exception e) {
			log.error("Unable to get and save datum", e);
			returnParcel.setMessage(Message.SAVE_FAILED);
			returnParcel.setDatum(d);
		    return respond(returnParcel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 	
	 	//signal to client that this datum is saved
	 	List<String> savedData = new ArrayList<String>();
	 	savedData.add(d.getId());
	 	returnParcel.setSavedData(savedData);
	 	
	 	//save or update the question associated with this answer
//	 	return saveQuestionAndSubscribe(returnParcel, q, participant);
	 	
	 	subscribe(participant, q);
	 	returnParcel.setMessage(Message.SAVED);
	 	return respond(returnParcel, HttpStatus.OK);
	}
	
	

	@RequestMapping(value = "/searchForQuestions", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	private ResponseEntity<String> searchForQuestions(@ModelAttribute("clientIpAddress") String clientIpAddress,
			@RequestBody String jsonRequest) {
		
		Parcel parcel = null;
		Parcel returnParcel = new Parcel();
		try {
			parcel = Parcel.fromJsonToParcel(jsonRequest);
		} catch (Exception e1) {
			log.error("searchForQuestions service: Error parsing JSON: " + jsonRequest, e1);
			returnParcel.setMessage(Message.BAD_REQUEST);
			return respond(returnParcel, HttpStatus.BAD_REQUEST);
		}
//	 	log.info("got parcel: " + parcel);
	 	String sessionToken = parcel.getSessionToken();
	 	Participant participant = null;
	 	try {
	 		//TODO add session expiration datetime
	 		log.info("searchForQuestions service getting current user participant...");
			participant = Participant.findParticipantsBySessionTokenEqualsAndSessionDateGreaterThanAndClientIpAddressEqualsAndEnabledNot(sessionToken, getEarliestSessionDate(), clientIpAddress, false).getSingleResult();
			log.info("searchForQuestions service participant:" + participant);
			assert(participant.getEnabled()==true);
	 	} catch (Exception e) {
			//leave as null
			log.warn("searchForQuestions service: Session not recognized or expired: sessionToken=" + sessionToken + "; clientIpAddress=" + clientIpAddress);
			returnParcel.setMessage(Message.INVALID_SESSION);
			return respond(returnParcel, HttpStatus.UNAUTHORIZED);
	 	}
	 	
	 	String queryTerm = parcel.getQueryTerm();
	 	String qt = "%" + sqlEscape(queryTerm) + "%";
	 	List<Question> questions = questionRepository.searchForNewQuestionsUsingSql(participant.getId(), qt);
	 	log.info("searchForQuestions queried " + qt + " and found: " + questions);
	 	
	 	//prepare the parcel for return
	 	returnParcel.setQuestions(questions);
	 	returnParcel.setQueryTerm(queryTerm);
	 	returnParcel.setMessage(Message.MATCHING_QUESTIONS_RETURNED);
	 	
	    String returnJson = returnParcel.toJson();
	    log.info("searchForQuestions service sending back: " + returnJson);
	    return respond(returnParcel, HttpStatus.OK);
	}
	
	private String sqlEscape(String queryTerm) {
		queryTerm = queryTerm.replace("%", "[%]");
		queryTerm = queryTerm.replace("*", "[*]");
		return queryTerm;
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
	@RequestMapping(value = "/unsubscribe", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> unsubscribe(
			@ModelAttribute("clientIpAddress") String clientIpAddress,
			@RequestBody String jsonRequest) {
		log.info("unsubscribe service invoked with json: " + jsonRequest);
	 	Parcel parcel = null;
	 	Parcel returnParcel = new Parcel();
		try {
			parcel = Parcel.fromJsonToParcel(jsonRequest);
		} catch (Exception e1) {
			log.error("storeQuestion service: Error parsing JSON: " + jsonRequest, e1);
			returnParcel.setMessage(Message.BAD_REQUEST);
			return respond(returnParcel, HttpStatus.BAD_REQUEST);
		}
//	 	log.info("got parcel: " + parcel);
	 	String sessionToken = parcel.getSessionToken();
	 	Participant participant = null;
	 	try {
	 		//TODO add session expiration datetime
	 		log.info("unsubscribe service getting current user participant...");
			participant = Participant.findParticipantsBySessionTokenEqualsAndSessionDateGreaterThanAndClientIpAddressEqualsAndEnabledNot(sessionToken, getEarliestSessionDate(), clientIpAddress, false).getSingleResult();
			log.info("unsubscribe service participant:" + participant);
			assert(participant.getEnabled()==true);
	 	} catch (Exception e) {
			//leave as null
			log.warn("Session not recognized or expired: sessionToken=" + sessionToken + "; clientIpAddress=" + clientIpAddress);
			returnParcel.setMessage(Message.INVALID_SESSION);
			return respond(returnParcel, HttpStatus.UNAUTHORIZED);
		}
	 	
	 	Question q = null;
		try {
			q = parcel.getQuestion();
			log.info("unsubscribe service received question: " + q);
		} catch (Exception e1) {
			log.error("Unable to get Question from parcel", e1);
			returnParcel.setMessage(Message.UNSUBSCRIBE_FAILED);
			return respond(returnParcel, HttpStatus.BAD_REQUEST);
		}
//		Question existingQuestion = questionRepository.findOne(q.getId());
		Subscription existingSubscription = null;
	 	try {
			existingSubscription = 
					Subscription.findSubscriptionsByQuestionIdEqualsAndParticipantEquals(q.getId(), participant).getSingleResult();
	 	} catch (Exception e1) {
			log.info("No existing subscription found for question: " + q.getId());
			returnParcel.setMessage(Message.UNSUBSCRIBE_FAILED);
			return respond(returnParcel, HttpStatus.BAD_REQUEST);
		}
	 	
	 	//delete the subscription
	 	existingSubscription.remove();
	 	
	 	//prepare the parcel for return
	 	returnParcel.setMessage(Message.UNSUBSCRIBED);
	    String returnJson = returnParcel.toJson();
	    log.info("unsubscribe service sending back: " + returnJson);
	    return respond(returnParcel, HttpStatus.OK);
	}
	
//	private ResponseEntity<String> saveQuestion(Parcel returnParcel, Question q, Participant participant) {
//		
//		//test that current user is the owner of this question or is admin
//	 	Question existingQuestion = questionRepository.findOne(q.getId());
////	 	Question theQuestion = q;
//	 	boolean loadData = false;
//	 	if (existingQuestion != null) {
//	 		loadData = true;
//	 		if (! participant.getId().equals(existingQuestion.getOwnerId()) && ! (participant.getRole() == UserRole.ROLE_ADMIN)) {
//	 			log.warn("storeQuestion service: insufficient privileges to modify existing question with this one:" + q);
//	 		} else {
////	 			theQuestion = mergeAndSaveQuestion(q, existingQuestion);
//	 			q = mergeAndSaveQuestion(q, existingQuestion);
//	 		}
//	 	} else {
//	 		//new question: current user must be owner
//	 		q.setOwner(participant);
////		 	questionRepository.save(theQuestion);
//	 		questionRepository.save(q);
//		 	questionRepository.flush();
//		 	log.info("storeQuestion service saved new question: " + q);
//	 	}
//	 	
//	 	//signal to client that this datum is saved
//	 	List<String> savedQuestions = new ArrayList<String>();
//	 	savedQuestions.add(q.getId());
//	 	returnParcel.setSavedQuestions(savedQuestions);
//	 	
//	 	//check to see if subscription already exists.  If not create a new one
//	 	Subscription existingSubscription = null;
//	 	 try {
//			existingSubscription = 
//					Subscription.findSubscriptionsByQuestionIdEqualsAndParticipantEquals(q.getId(), participant).getSingleResult();
//
//	 	 } catch (Exception e1) {
//			log.info("No existing subscription found for question: " + q.getId());
//		}
//	 	if (existingSubscription == null) {
//		 	Subscription subscription = new Subscription();
//		 	subscription.setCreated(new Date());
//		 	subscription.setCreatedBy(participant.getId());
//		 	subscription.setQuestionId(q.getId());
////		 	subscription.setQuestion(theQuestion);
//		 	subscription.setSubscriptionType(SubscriptionType.ACTIVE_DAILY);
////		 	subscription.setParticipantId(participant.getId());
//		 	subscription.setParticipant(participant);
//		 	log.info("storeQuestion service to save subscription: " + subscription);
//		 	try {
//				subscription.persist();
//				subscription.flush();
//				log.info("storeQuestion service saved subscription");
//			} catch (Exception e) {
//				log.error("storeQuestion service unable to save subscription:" + subscription, e);
//				returnParcel.setMessage(Message.SUBSCRIBE_FAILED);
//				returnParcel.setQuestion(q);
//			    return respond(returnParcel, HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//	 	}
	 	
	 	//prepare the parcel for return
	 	
//	 	if (loadData) {
//	 		List<Datum> data = datumRepository.getParticipantDataForQuestion(participant.getId(), q.getId());
//	 		returnParcel.setData(data);
//	 	}
//	 	
//	 	returnParcel.setMessage(Message.SAVED);
//	    String returnJson = returnParcel.toJson();
//	    log.info("storeQuestion service sending back: " + returnJson);
//	    return respond(returnParcel, HttpStatus.OK);
//	}
	
	private void subscribe(Participant participant, Question q) {
		//check to see if subscription already exists.  If not create a new one
	 	Subscription existingSubscription = null;
	 	 try {
			existingSubscription = 
					Subscription.findSubscriptionsByQuestionIdEqualsAndParticipantEquals(q.getId(), participant).getSingleResult();

	 	 } catch (Exception e1) {
			log.info("No existing subscription found for question: " + q.getId());
		}
	 	if (existingSubscription == null) {
		 	Subscription subscription = new Subscription();
		 	subscription.setCreated(new Date());
		 	subscription.setCreatedBy(participant.getId());
		 	subscription.setQuestionId(q.getId());
//		 	subscription.setQuestion(theQuestion);
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
			}
	 	}
	}
	
	private Date getEarliestSessionDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1 * MAXIMUM_SESSION_DAYS);
		return c.getTime();
		
	}
	
	private void mergeAndSaveDatum(Datum newDatum, Datum existingDatum) {
		if (existingDatum == null) {
			existingDatum = newDatum;
			datumRepository.saveAndFlush(existingDatum);
			return;
		}
		if (newDatum.getAnswerStatus() != null) {
			existingDatum.setAnswerStatus(newDatum.getAnswerStatus());
		}
		if (newDatum.getChoice() != null) {
			existingDatum.setChoice(newDatum.getChoice());
		}
		if (newDatum.getUpdated() != null) {
			existingDatum.setUpdated(newDatum.getUpdated());
		}
		if (newDatum.getDataType() != null) {
			existingDatum.setDataType(newDatum.getDataType());
		}
		if (newDatum.getEffectiveDate() != null) {
			existingDatum.setEffectiveDate(newDatum.getEffectiveDate());
		}
		if (newDatum.getFormula() != null) {
			existingDatum.setFormula(newDatum.getFormula());
		}
		if (newDatum.getIntegerValue() != null) {
			existingDatum.setIntegerValue(newDatum.getIntegerValue());
		}
		if (newDatum.getNormalizedValue() != null) {
			existingDatum.setNormalizedValue(newDatum.getNormalizedValue());
		}
		if (newDatum.getNumericValue() != null) {
			existingDatum.setNumericValue(newDatum.getNumericValue());
		}
		if (newDatum.getQuestionConcept() != null) {
			existingDatum.setQuestionConcept(newDatum.getQuestionConcept());
		}
		if (newDatum.getTextValue() != null) {
			existingDatum.setTextValue(newDatum.getTextValue());
		}
		if (newDatum.getUnit() != null) {
			existingDatum.setUnit(newDatum.getUnit());
		}
		if (newDatum.getUpdatedBy() != null) {
			existingDatum.setUpdatedBy(newDatum.getUpdatedBy());
		}
		datumRepository.saveAndFlush(existingDatum);
	}
	
	private Question mergeAndSaveQuestion(Question newQuestion, Question existingQuestion) {
		if (existingQuestion == null) {
			existingQuestion = newQuestion;
			questionRepository.saveAndFlush(existingQuestion);
			return existingQuestion;
		}
		
		if (newQuestion.getAbbreviation() != null) {
			existingQuestion.setAbbreviation(newQuestion.getAbbreviation());
		}
		if (newQuestion.getLatency() != null) {
			existingQuestion.setLatency(newQuestion.getLatency());
		}
		if (newQuestion.getUpdated() != null) {
			existingQuestion.setUpdated(newQuestion.getUpdated());
		}
		if (newQuestion.getDataType() != null) {
			existingQuestion.setDataType(newQuestion.getDataType());
		}
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
		if (newQuestion.getMeasurement() != null) {
			existingQuestion.setMeasurement(newQuestion.getMeasurement());
		}
		if (newQuestion.getPriority() != null) {
			existingQuestion.setPriority(newQuestion.getPriority());
		}
		questionRepository.saveAndFlush(existingQuestion);
		return existingQuestion;
	}
	
	private ResponseEntity<java.lang.String> respond(Parcel returnParcel, HttpStatus status) {
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
	    headers.add("Access-Control-Allow-Origin", "*");
	    return new ResponseEntity<String>(returnParcel.toJson(), headers, status);
	}
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/saveUnsaved", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> saveUnsaved(
			@ModelAttribute("clientIpAddress") String clientIpAddress,
			@RequestBody String jsonRequest) {
		log.info("saveUnsaved service invoked with json: " + jsonRequest);
	 	Parcel parcel = null;
	 	Parcel returnParcel = new Parcel();
		String sessionToken;
		try {
			parcel = Parcel.fromJsonToParcel(jsonRequest);
			sessionToken = parcel.getSessionToken();
		} catch (Exception e1) {
			log.error("saveUnsaved service: Unable to parse json into parcel:" + jsonRequest, e1);
			returnParcel.setMessage(Message.BAD_REQUEST);
			return respond(returnParcel, HttpStatus.BAD_REQUEST);
		}
		log.info("saveUnsaved service: got parcel: " + parcel);
	 	Participant participant = null;
	 	try {
	 		//TODO add session expiration datetime
			participant = Participant.findParticipantsBySessionTokenEqualsAndSessionDateGreaterThanAndClientIpAddressEqualsAndEnabledNot(sessionToken, getEarliestSessionDate(), clientIpAddress, false).getSingleResult();
			log.info("saveUnsaved service: got participant: " + participant);
			assert(participant.getEnabled()==true);
	 	} catch (Exception e) {
			//leave as null
			log.warn("Session not recognized or expired: sessionToken=" + sessionToken + "; clientIpAddress=" + clientIpAddress);
			returnParcel.setMessage(Message.INVALID_SESSION);
			return respond(returnParcel, HttpStatus.UNAUTHORIZED);
		}
	 	
	 	List<Datum> data = parcel.getData();
	 	List<Question> questions = parcel.getQuestions();
	 	int dataQueueSize = 0;
	 	if (data != null) dataQueueSize = data.size();
	 	int questionsQueueSize = 0;
	 	if (questions != null) questionsQueueSize = questions.size();
	 	
	 	//make sure the user has not exceeded data quota
	 	long countData = datumRepository.countParticipantData(participant.getId());
	 	if (countData + dataQueueSize > MAXIMUM_DATA_PER_USER) {
	 		log.warn("Participant " + participant.getUsername() + " has too many data (" + countData + ")");
		    returnParcel.setMessage(Message.TOO_MANY_DATA);
			return respond(returnParcel, HttpStatus.UNAUTHORIZED);
	 	}
	 	
	 	//make sure the user has not exceeded question quota
	 	long countQuestions = questionRepository.countQuestionsOwned(participant.getId());
	 	if (countQuestions + questionsQueueSize > MAXIMUM_QUESTIONS_PER_USER) {
	 		log.warn("Participant " + participant.getUsername() + " has too many questions (" + countQuestions + ")");
		    returnParcel.setMessage(Message.TOO_MANY_QUESTIONS);
			return respond(returnParcel, HttpStatus.UNAUTHORIZED);
	 	}
	 	
	 	log.info("User " + participant.getUsername() + " has created " + countQuestions + " questions and " + countData + " data.");
	 	int countSavedData = 0;
	 	if (data != null) {
	 		List<String> savedData = new ArrayList<String>();
	 		for(Datum d: data) {
	 			Datum existingDatum = datumRepository.findOne(d.getId());
				mergeAndSaveDatum(d, existingDatum);
	 			savedData.add(d.getId());
	 			countSavedData++;
	 		}
	 		returnParcel.setSavedData(savedData);
	 	}
	 	
	 	int countSavedQuestions = 0;
	 	if (questions != null) {
	 		List<String> savedQuestions = new ArrayList<String>();
	 		for (Question q: questions) {
	 			Question existingQuestion = questionRepository.findOne(q.getId());
	 			mergeAndSaveQuestion(q, existingQuestion);
	 			subscribe(participant, q);
	 			savedQuestions.add(q.getId());
	 			countSavedQuestions++;
	 		}
	 		returnParcel.setSavedQuestions(savedQuestions);
	 	}
	 	
	 	returnParcel.setMessage(Message.SAVED);
//	    String returnJson = returnParcel.toJson();
	    log.info("saveUnsaved service for participant:" + participant.getUsername() + " saved " + countSavedQuestions + " questions and " + countSavedData + " data");
	    return respond(returnParcel, HttpStatus.OK);
	 	
	}
}
