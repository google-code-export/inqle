package org.inqle.web;

import org.inqle.domain.SurveyQuestion;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/surveyquestions")
@Controller
@RooWebScaffold(path = "surveyquestions", formBackingObject = SurveyQuestion.class)
public class SurveyQuestionController {
}
