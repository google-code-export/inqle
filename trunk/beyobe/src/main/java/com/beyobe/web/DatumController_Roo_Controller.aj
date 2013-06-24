// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.beyobe.web;

import com.beyobe.client.beans.AnswerStatus;
import com.beyobe.client.beans.DataType;
import com.beyobe.client.beans.Unit;
import com.beyobe.domain.ChoiceConcept;
import com.beyobe.domain.Datum;
import com.beyobe.domain.Formula;
import com.beyobe.domain.Participant;
import com.beyobe.domain.QuestionConcept;
import com.beyobe.repository.DatumRepository;
import com.beyobe.repository.QuestionRepository;
import com.beyobe.web.DatumController;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    DatumRepository DatumController.datumRepository;
    
    @Autowired
    QuestionRepository DatumController.questionRepository;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DatumController.create(@Valid Datum datum, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, datum);
            return "admin/data/create";
        }
        uiModel.asMap().clear();
        datumRepository.save(datum);
        return "redirect:/admin/data/" + encodeUrlPathSegment(datum.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DatumController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Datum());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Participant.countParticipants() == 0) {
            dependencies.add(new String[] { "participant", "admin/participants" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "admin/data/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String DatumController.show(@PathVariable("id") String id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("datum", datumRepository.findOne(id));
        uiModel.addAttribute("itemId", id);
        return "admin/data/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DatumController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("data", datumRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / sizeNo, sizeNo)).getContent());
            float nrOfPages = (float) datumRepository.count() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("data", datumRepository.findAll());
        }
        addDateTimeFormatPatterns(uiModel);
        return "admin/data/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DatumController.update(@Valid Datum datum, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, datum);
            return "admin/data/update";
        }
        uiModel.asMap().clear();
        datumRepository.save(datum);
        return "redirect:/admin/data/" + encodeUrlPathSegment(datum.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String DatumController.updateForm(@PathVariable("id") String id, Model uiModel) {
        populateEditForm(uiModel, datumRepository.findOne(id));
        return "admin/data/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String DatumController.delete(@PathVariable("id") String id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Datum datum = datumRepository.findOne(id);
        datumRepository.delete(datum);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/admin/data";
    }
    
    void DatumController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("datum_created_date_format", DateTimeFormat.patternForStyle("FF", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("datum_updated_date_format", DateTimeFormat.patternForStyle("FF", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("datum_effectivedate_date_format", DateTimeFormat.patternForStyle("FF", LocaleContextHolder.getLocale()));
    }
    
    void DatumController.populateEditForm(Model uiModel, Datum datum) {
        uiModel.addAttribute("datum", datum);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("answerstatuses", Arrays.asList(AnswerStatus.values()));
        uiModel.addAttribute("datatypes", Arrays.asList(DataType.values()));
        uiModel.addAttribute("units", Arrays.asList(Unit.values()));
        uiModel.addAttribute("choiceconcepts", ChoiceConcept.findAllChoiceConcepts());
        uiModel.addAttribute("formulas", Formula.findAllFormulas());
        uiModel.addAttribute("participants", Participant.findAllParticipants());
        uiModel.addAttribute("questions", questionRepository.findAll());
        uiModel.addAttribute("questionconcepts", QuestionConcept.findAllQuestionConcepts());
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
