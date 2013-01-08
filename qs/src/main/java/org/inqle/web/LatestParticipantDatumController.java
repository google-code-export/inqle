package org.inqle.web;

import org.inqle.domain.LatestParticipantDatum;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/latestparticipantdatums")
@Controller
@RooWebScaffold(path = "latestparticipantdatums", formBackingObject = LatestParticipantDatum.class)
public class LatestParticipantDatumController {
}
