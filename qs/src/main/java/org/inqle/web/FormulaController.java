package org.inqle.web;

import org.inqle.domain.Formula;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/formulas")
@Controller
@RooWebScaffold(path = "formulas", formBackingObject = Formula.class)
public class FormulaController {
}
