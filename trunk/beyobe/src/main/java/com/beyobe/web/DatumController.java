package com.beyobe.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beyobe.domain.Datum;

@RequestMapping("/data")
@Controller
@RooWebScaffold(path = "data", formBackingObject = Datum.class)
public class DatumController {
}
