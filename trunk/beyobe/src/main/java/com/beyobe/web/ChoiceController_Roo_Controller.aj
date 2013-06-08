// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.beyobe.web;

import com.beyobe.domain.Choice;
import com.beyobe.domain.ChoiceConcept;
import com.beyobe.web.ChoiceController;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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

privileged aspect ChoiceController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ChoiceController.create(@Valid Choice choice, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, choice);
            return "admin/choices/create";
        }
        uiModel.asMap().clear();
        choice.persist();
        return "redirect:/admin/choices/" + encodeUrlPathSegment(choice.getId_().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ChoiceController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Choice());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (ChoiceConcept.countChoiceConcepts() == 0) {
            dependencies.add(new String[] { "choiceconcept", "admin/choiceconcepts" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "admin/choices/create";
    }
    
    @RequestMapping(value = "/{id_}", produces = "text/html")
    public String ChoiceController.show(@PathVariable("id_") Long id_, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("choice", Choice.findChoice(id_));
        uiModel.addAttribute("itemId", id_);
        return "admin/choices/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ChoiceController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("choices", Choice.findChoiceEntries(firstResult, sizeNo));
            float nrOfPages = (float) Choice.countChoices() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("choices", Choice.findAllChoices());
        }
        addDateTimeFormatPatterns(uiModel);
        return "admin/choices/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ChoiceController.update(@Valid Choice choice, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, choice);
            return "admin/choices/update";
        }
        uiModel.asMap().clear();
        choice.merge();
        return "redirect:/admin/choices/" + encodeUrlPathSegment(choice.getId_().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id_}", params = "form", produces = "text/html")
    public String ChoiceController.updateForm(@PathVariable("id_") Long id_, Model uiModel) {
        populateEditForm(uiModel, Choice.findChoice(id_));
        return "admin/choices/update";
    }
    
    @RequestMapping(value = "/{id_}", method = RequestMethod.DELETE, produces = "text/html")
    public String ChoiceController.delete(@PathVariable("id_") Long id_, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Choice choice = Choice.findChoice(id_);
        choice.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/admin/choices";
    }
    
    void ChoiceController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("choice_created_date_format", DateTimeFormat.patternForStyle("FF", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("choice_updated_date_format", DateTimeFormat.patternForStyle("FF", LocaleContextHolder.getLocale()));
    }
    
    void ChoiceController.populateEditForm(Model uiModel, Choice choice) {
        uiModel.addAttribute("choice", choice);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("choiceconcepts", ChoiceConcept.findAllChoiceConcepts());
    }
    
    String ChoiceController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
