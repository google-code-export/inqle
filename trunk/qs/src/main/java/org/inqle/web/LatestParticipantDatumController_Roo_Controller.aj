// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.inqle.domain.Concept;
import org.inqle.domain.LatestParticipantDatum;
import org.inqle.domain.Participant;
import org.inqle.repository.DatumRepository;
import org.inqle.web.LatestParticipantDatumController;
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

privileged aspect LatestParticipantDatumController_Roo_Controller {
    
    @Autowired
    DatumRepository LatestParticipantDatumController.datumRepository;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String LatestParticipantDatumController.create(@Valid LatestParticipantDatum latestParticipantDatum, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, latestParticipantDatum);
            return "latestparticipantdatums/create";
        }
        uiModel.asMap().clear();
        latestParticipantDatum.persist();
        return "redirect:/latestparticipantdatums/" + encodeUrlPathSegment(latestParticipantDatum.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String LatestParticipantDatumController.createForm(Model uiModel) {
        populateEditForm(uiModel, new LatestParticipantDatum());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Concept.countConcepts() == 0) {
            dependencies.add(new String[] { "concept", "concepts" });
        }
        if (Participant.countParticipants() == 0) {
            dependencies.add(new String[] { "participant", "participants" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "latestparticipantdatums/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String LatestParticipantDatumController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("latestparticipantdatum", LatestParticipantDatum.findLatestParticipantDatum(id));
        uiModel.addAttribute("itemId", id);
        return "latestparticipantdatums/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String LatestParticipantDatumController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("latestparticipantdatums", LatestParticipantDatum.findLatestParticipantDatumEntries(firstResult, sizeNo));
            float nrOfPages = (float) LatestParticipantDatum.countLatestParticipantDatums() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("latestparticipantdatums", LatestParticipantDatum.findAllLatestParticipantDatums());
        }
        addDateTimeFormatPatterns(uiModel);
        return "latestparticipantdatums/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String LatestParticipantDatumController.update(@Valid LatestParticipantDatum latestParticipantDatum, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, latestParticipantDatum);
            return "latestparticipantdatums/update";
        }
        uiModel.asMap().clear();
        latestParticipantDatum.merge();
        return "redirect:/latestparticipantdatums/" + encodeUrlPathSegment(latestParticipantDatum.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String LatestParticipantDatumController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LatestParticipantDatum.findLatestParticipantDatum(id));
        return "latestparticipantdatums/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String LatestParticipantDatumController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LatestParticipantDatum latestParticipantDatum = LatestParticipantDatum.findLatestParticipantDatum(id);
        latestParticipantDatum.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/latestparticipantdatums";
    }
    
    void LatestParticipantDatumController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("latestParticipantDatum_created_date_format", DateTimeFormat.patternForStyle("FF", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("latestParticipantDatum_askableafter_date_format", DateTimeFormat.patternForStyle("FF", LocaleContextHolder.getLocale()));
    }
    
    void LatestParticipantDatumController.populateEditForm(Model uiModel, LatestParticipantDatum latestParticipantDatum) {
        uiModel.addAttribute("latestParticipantDatum", latestParticipantDatum);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("concepts", Concept.findAllConcepts());
        uiModel.addAttribute("data", datumRepository.findAll());
        uiModel.addAttribute("participants", Participant.findAllParticipants());
    }
    
    String LatestParticipantDatumController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
