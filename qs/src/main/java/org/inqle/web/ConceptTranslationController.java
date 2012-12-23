package org.inqle.web;

import org.inqle.domain.ConceptTranslation;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/concepttranslations")
@Controller
@RooWebScaffold(path = "concepttranslations", formBackingObject = ConceptTranslation.class)
@RooWebJson(jsonObject = ConceptTranslation.class)
public class ConceptTranslationController {
}
