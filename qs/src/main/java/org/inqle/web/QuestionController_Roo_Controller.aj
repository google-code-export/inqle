// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.web;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.inqle.domain.Concept;
import org.inqle.domain.Question;
import org.inqle.domain.security.Principal;
import org.inqle.web.QuestionController;
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

privileged aspect QuestionController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String QuestionController.create(@Valid Question question, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, question);
            return "questions/create";
        }
        uiModel.asMap().clear();
        questionRepository.save(question);
        return "redirect:/questions/" + encodeUrlPathSegment(question.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String QuestionController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Question());
        return "questions/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String QuestionController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("question", questionRepository.findOne(id));
        uiModel.addAttribute("itemId", id);
        return "questions/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String QuestionController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("questions", questionRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / sizeNo, sizeNo)).getContent());
            float nrOfPages = (float) questionRepository.count() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("questions", questionRepository.findAll());
        }
        addDateTimeFormatPatterns(uiModel);
        return "questions/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String QuestionController.update(@Valid Question question, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, question);
            return "questions/update";
        }
        uiModel.asMap().clear();
        questionRepository.save(question);
        return "redirect:/questions/" + encodeUrlPathSegment(question.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String QuestionController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, questionRepository.findOne(id));
        return "questions/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String QuestionController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Question question = questionRepository.findOne(id);
        questionRepository.delete(question);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/questions";
    }
    
    void QuestionController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("question_created_date_format", DateTimeFormat.patternForStyle("FF", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("question_updated_date_format", DateTimeFormat.patternForStyle("FF", LocaleContextHolder.getLocale()));
    }
    
    void QuestionController.populateEditForm(Model uiModel, Question question) {
        uiModel.addAttribute("question", question);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("concepts", Concept.findAllConcepts());
        uiModel.addAttribute("principals", Principal.findAllPrincipals());
    }
    
    String QuestionController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
