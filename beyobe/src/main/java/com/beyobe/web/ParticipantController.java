package com.beyobe.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beyobe.domain.Participant;

@RequestMapping("/participants")
@Controller
@RooWebScaffold(path = "participants", formBackingObject = Participant.class)
public class ParticipantController {
}
