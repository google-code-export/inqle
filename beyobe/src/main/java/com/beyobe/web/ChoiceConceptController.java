package com.beyobe.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beyobe.domain.ChoiceConcept;

@RequestMapping("/choiceconcepts")
@Controller
@RooWebScaffold(path = "choiceconcepts", formBackingObject = ChoiceConcept.class)
public class ChoiceConceptController {
}
