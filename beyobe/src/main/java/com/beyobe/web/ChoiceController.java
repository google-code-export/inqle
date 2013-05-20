package com.beyobe.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beyobe.domain.Choice;

@RequestMapping("/choices")
@Controller
@RooWebScaffold(path = "choices", formBackingObject = Choice.class)
public class ChoiceController {
}
