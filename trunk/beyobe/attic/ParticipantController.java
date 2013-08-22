package com.beyobe.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.beyobe.domain.Participant;

@RequestMapping("/admin/participants")
@Controller
@RooWebScaffold(path = "admin/participants", formBackingObject = Participant.class)
public class ParticipantController {

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Participant participant, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, participant);
            return "admin/participants/update";
        }
        uiModel.asMap().clear();
        participant.mergeWithExisting();
        return "redirect:/admin/participants/" + encodeUrlPathSegment(participant.getId().toString(), httpServletRequest);
    }
}
