package com.beyobe.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beyobe.domain.Question;

@RequestMapping("/admin/questions")
@Controller
@RooWebScaffold(path = "admin/questions", formBackingObject = Question.class)
public class QuestionController {
}