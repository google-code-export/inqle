package com.beyobe.web;

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.beyobe.client.beans.Parcel;
import com.beyobe.domain.Participant;
import com.beyobe.domain.Question;
import com.beyobe.repository.DatumRepository;
import com.beyobe.repository.QuestionRepository;

@RequestMapping("/service/**")
@Controller
public class ServiceController {

	private QuestionRepository questionRepository;
	private DatumRepository datumRepository;
	private static final Logger log = Logger.getLogger(ServiceController.class);
	
	 @RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json")
	    public ResponseEntity<java.lang.String> getSubscribedQuestions(@RequestBody String jsonRequest) {
		 	Parcel parcel = Parcel.fromJsonToParcel(jsonRequest);
		 	String username = parcel.getUsername();
		 	String password = parcel.getPassword();
		 	String passwordHash = Participant.hashString(password, username);
		 	Participant participant = null;
		 	try {
				participant = Participant.findParticipantsByUsernameEqualsAndPasswordEquals(username, passwordHash).getSingleResult();
				assert(participant != null);
		 	} catch (Exception e) {
				//leave as null
				log.warn("Login failure: username=" + username + "; passwordHash=" + passwordHash);
				return null;
			}
		 	//prepare the parcel for return
		 	Parcel returnParcel = new Parcel();
		 	//new session: use UUID
		 	returnParcel.setSessionToken(UUID.randomUUID().toString());
		 	participant.setSessionToken(returnParcel.getSessionToken());
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/json");
	        List<Question> questionQueue = questionRepository.getSubscribedQuestions(participant.getId());
	        returnParcel.setQuestionQueue(questionQueue);
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
