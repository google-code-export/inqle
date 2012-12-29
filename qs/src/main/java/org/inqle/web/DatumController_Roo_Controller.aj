// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.inqle.domain.Account;
import org.inqle.domain.Choice;
import org.inqle.domain.Datum;
import org.inqle.domain.Formula;
import org.inqle.domain.Participant;
import org.inqle.domain.Unit;
import org.inqle.repository.QuestionRepository;
import org.inqle.web.DatumController;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect DatumController_Roo_Controller {
    
    @Autowired
    QuestionRepository DatumController.questionRepository;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DatumController.create(@Valid Datum datum, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, datum);
            return "data/create";
        }
        uiModel.asMap().clear();
        datum.persist();
        return "redirect:/data/" + encodeUrlPathSegment(datum.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DatumController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Datum());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Participant.countParticipants() == 0) {
            dependencies.add(new String[] { "participant", "participants" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "data/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String DatumController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("datum", Datum.findDatum(id));
        uiModel.addAttribute("itemId", id);
        return "data/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DatumController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("data", Datum.findDatumEntries(firstResult, sizeNo));
            float nrOfPages = (float) Datum.countData() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("data", Datum.findAllData());
        }
        addDateTimeFormatPatterns(uiModel);
        return "data/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DatumController.update(@Valid Datum datum, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, datum);
            return "data/update";
        }
        uiModel.asMap().clear();
        datum.merge();
        return "redirect:/data/" + encodeUrlPathSegment(datum.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String DatumController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Datum.findDatum(id));
        return "data/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String DatumController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Datum datum = Datum.findDatum(id);
        datum.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/data";
    }
    
    void DatumController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("datum_created_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("datum_updated_date_format", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }
    
    void DatumController.populateEditForm(Model uiModel, Datum datum) {
        uiModel.addAttribute("datum", datum);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("accounts", Account.findAllAccounts());
        uiModel.addAttribute("choices", Choice.findAllChoices());
        uiModel.addAttribute("formulas", Formula.findAllFormulas());
        uiModel.addAttribute("participants", Participant.findAllParticipants());
        uiModel.addAttribute("questions", questionRepository.findAll());
        uiModel.addAttribute("units", Unit.findAllUnits());
    }
    
    String DatumController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
