package com.beyobe.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beyobe.domain.Concept;

@RequestMapping("/concepts")
@Controller
@RooWebScaffold(path = "concepts", formBackingObject = Concept.class)
public class ConceptController {
}
