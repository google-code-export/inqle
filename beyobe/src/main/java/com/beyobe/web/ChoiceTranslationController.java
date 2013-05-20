package com.beyobe.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beyobe.domain.ChoiceTranslation;

@RequestMapping("/choicetranslations")
@Controller
@RooWebScaffold(path = "choicetranslations", formBackingObject = ChoiceTranslation.class)
public class ChoiceTranslationController {
}
