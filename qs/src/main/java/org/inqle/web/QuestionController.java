package org.inqle.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.inqle.domain.Question;
import org.inqle.repository.QuestionRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/questions")
@Controller
@RooWebScaffold(path = "questions", formBackingObject = Question.class)
@RooWebJson(jsonObject = Question.class)
public class QuestionController {
	
	private QuestionRepository questionRepository;
	
	@RequestMapping(value = "/subscribedQuestions", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> getSubscribedQuestions(@RequestParam Long participantId) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
	    List<Question> questions = questionRepository.getSubscribedQuestions(participantId);
	    return new ResponseEntity<String>(Question.toJsonArray(questions), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/searchQuestions", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> getSubscribedQuestions(@RequestParam Long participantId, @RequestParam String query) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
	    QueryResponse queryResponse = Question.search(query);
	    List<Question> questions = queryResponseToQuestions(queryResponse);
	    return new ResponseEntity<String>(Question.toJsonArray(questions), headers, HttpStatus.OK);
	}

	private List<Question> queryResponseToQuestions(QueryResponse queryResponse) {
		List<Question> questions = new ArrayList<Question>();
		for (SolrDocument sd : queryResponse.getResults()) { 
			questions.add(Question.findQuestion((Long)sd.getFieldValue("blurb.id_l"))); 
		}
		return questions;
	}
}
