package org.inqle.web;

import org.inqle.domain.Datum;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/data")
@Controller
@RooWebScaffold(path = "data", formBackingObject = Datum.class)
@RooWebJson(jsonObject = Datum.class)
public class DatumController {
}
