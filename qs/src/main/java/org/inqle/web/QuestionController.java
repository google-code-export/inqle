package org.inqle.web;

import java.util.List;

import org.inqle.domain.Question;
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
	
	@RequestMapping(value = "/ask", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> getQuestionsToAsk(@RequestParam String participantId) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/json");
	    //TODO - figure out the questions to ask this participant
	    List<Question> result = Question.findAllQuestions();
	    return new ResponseEntity<String>(Question.toJsonArray(result), headers, HttpStatus.OK);
	}
}
