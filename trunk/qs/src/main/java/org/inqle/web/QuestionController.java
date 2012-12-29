package org.inqle.web;

import org.inqle.domain.Question;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/questions")
@Controller
@RooWebScaffold(path = "questions", formBackingObject = Question.class)
@RooWebJson(jsonObject = Question.class)
public class QuestionController {
	
//	private QuestionRepository questionRepository;
//	
//	@RequestMapping(value = "/ask", method = RequestMethod.GET, headers = "Accept=application/json")
//	public ResponseEntity<String> getSubscribedQuestions(@RequestParam Integer participantId, @RequestParam Integer count, @RequestParam Integer offset) {
//	    HttpHeaders headers = new HttpHeaders();
//	    headers.add("Content-Type", "application/json");
//	    //TODO - figure out the questions to ask this participant
//	    List<Question> result = questionRepository.getSubscribedQuestions(participantId, count, offset);
//	    return new ResponseEntity<String>(Question.toJsonArray(result), headers, HttpStatus.OK);
//	}
}
