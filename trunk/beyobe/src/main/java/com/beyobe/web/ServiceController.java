package com.beyobe.web;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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

	private QuestionRepository questionRepository;
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
			log.warn("Bad request: incoming JSON=" + jsonRequest);
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
	 	
	 	log.info("saved participant");
	 	
	 	//prepare the parcel for return
	 	Parcel returnParcel = new Parcel();
	 	returnParcel.setSessionToken(sessionToken);
	 	log.info("set session token");
	 	
	 	List<Question> questionQueue = questionRepository.getSubscribedQuestions(participant.getId(), SubscriptionType.ACTIVE_DAILY.name());
		returnParcel.setQuestionQueue(questionQueue);
		log.info("set session queue");
		
		List<Question> inactiveQuestions = questionRepository.getSubscribedQuestions(participant.getId(), SubscriptionType.INACTIVE.name());
		returnParcel.setOtherKnownQuestions(inactiveQuestions);
		log.info("set inactive questions");
		
	 	List<Datum> data = datumRepository.getParticipantData(participant.getId());
	 	returnParcel.setData(data);
	 	log.info("set data");
	 	
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
	    log.info("Sending back: " + returnParcel.toJson());
	    return new ResponseEntity<String>(returnParcel.toJson(), headers, HttpStatus.OK);
	}
	 
	
	@RequestMapping(value = "/storeQuestion", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> storeQuestion(
			@ModelAttribute("clientIpAddress") String clientIpAddress,
			@RequestBody String jsonRequest) {
	 	Parcel parcel = Parcel.fromJsonToParcel(jsonRequest);
	 	String sessionToken = parcel.getSessionToken();
	 	Participant participant = null;
	 	try {
	 		//TODO add session expiration datetime
			participant = Participant.findParticipantsBySessionTokenEqualsAndClientIpAddressEquals(sessionToken, clientIpAddress).getSingleResult();
			assert(participant.getEnabled()==true);
	 	} catch (Exception e) {
			//leave as null
			log.warn("Session not recognized or expired: sessionToken=" + sessionToken + "; clientIpAddress=" + clientIpAddress);
			HttpHeaders headers = new HttpHeaders();
		    headers.add("Content-Type", "application/json");
			return new ResponseEntity<String>(null, headers, HttpStatus.UNAUTHORIZED);
		}
	 	Question q = parcel.getQuestion();
	 	
	 	Subscription subscription = new Subscription();
	 	subscription.setCreated(new Date());
	 	subscription.setCreatedBy(participant.getId());
	 	subscription.setQuestion(q);
	 	subscription.setSubscriptionType(SubscriptionType.ACTIVE_DAILY);
	 	subscription.setParticipant(participant);
	 	subscription.persist();
	 	
	 	questionRepository.saveAndFlush(q);
	 	
	 	//prepare the parcel for return
	 	Parcel returnParcel = new Parcel();
	 	
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
	    return new ResponseEntity<String>(returnParcel.toJson(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/storeDatum", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> storeDatum(
			@ModelAttribute("clientIpAddress") String clientIpAddress,
			@RequestBody String jsonRequest) {
	 	Parcel parcel = Parcel.fromJsonToParcel(jsonRequest);
	 	String sessionToken = parcel.getSessionToken();
	 	Participant participant = null;
	 	try {
	 		//TODO add session expiration datetime
			participant = Participant.findParticipantsBySessionTokenEqualsAndClientIpAddressEquals(sessionToken, clientIpAddress).getSingleResult();
			assert(participant.getEnabled()==true);
	 	} catch (Exception e) {
			//leave as null
			log.warn("Session not recognized or expired: sessionToken=" + sessionToken + "; clientIpAddress=" + clientIpAddress);
			return new ResponseEntity<String>(null, null, HttpStatus.UNAUTHORIZED);
		}
	 	Datum d = parcel.getDatum();
	 	
	 	datumRepository.saveAndFlush(d);
	 	
	 	//prepare the parcel for return
	 	Parcel returnParcel = new Parcel();
	 	
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
	    return new ResponseEntity<String>(returnParcel.toJson(), headers, HttpStatus.OK);
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
}
