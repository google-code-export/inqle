package org.inqle.web;

import org.inqle.domain.Survey;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/surveys")
@Controller
@RooWebScaffold(path = "surveys", formBackingObject = Survey.class)
public class SurveyController {
}
