package com.beyobe.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beyobe.domain.QuestionConcept;

@RequestMapping("/admin/questionconcepts")
@Controller
@RooWebScaffold(path = "admin/questionconcepts", formBackingObject = QuestionConcept.class)
public class QuestionConceptController {
}
