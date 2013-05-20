package com.beyobe.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beyobe.domain.Formula;

@RequestMapping("/formulas")
@Controller
@RooWebScaffold(path = "formulas", formBackingObject = Formula.class)
public class FormulaController {
}
