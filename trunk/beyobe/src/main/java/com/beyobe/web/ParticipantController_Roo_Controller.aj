// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.beyobe.web;

import com.beyobe.client.beans.UserRole;
import com.beyobe.domain.Participant;
import com.beyobe.web.ParticipantController;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect ParticipantController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ParticipantController.create(@Valid Participant participant, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, participant);
            return "admin/participants/create";
        }
        uiModel.asMap().clear();
        participant.persist();
        return "redirect:/admin/participants/" + encodeUrlPathSegment(participant.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ParticipantController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Participant());
        return "admin/participants/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ParticipantController.show(@PathVariable("id") String id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("participant", Participant.findParticipant(id));
        uiModel.addAttribute("itemId", id);
        return "admin/participants/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ParticipantController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("participants", Participant.findParticipantEntries(firstResult, sizeNo));
            float nrOfPages = (float) Participant.countParticipants() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("participants", Participant.findAllParticipants());
        }
        addDateTimeFormatPatterns(uiModel);
        return "admin/participants/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ParticipantController.update(@Valid Participant participant, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, participant);
            return "admin/participants/update";
        }
        uiModel.asMap().clear();
        participant.merge();
        return "redirect:/admin/participants/" + encodeUrlPathSegment(participant.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ParticipantController.updateForm(@PathVariable("id") String id, Model uiModel) {
        populateEditForm(uiModel, Participant.findParticipant(id));
        return "admin/participants/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ParticipantController.delete(@PathVariable("id") String id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Participant participant = Participant.findParticipant(id);
        participant.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/admin/participants";
    }
    
    void ParticipantController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("participant_created_date_format", DateTimeFormat.patternForStyle("FF", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("participant_updated_date_format", DateTimeFormat.patternForStyle("FF", LocaleContextHolder.getLocale()));
    }
    
    void ParticipantController.populateEditForm(Model uiModel, Participant participant) {
        uiModel.addAttribute("participant", participant);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("userroles", Arrays.asList(UserRole.values()));
    }
    
    String ParticipantController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
