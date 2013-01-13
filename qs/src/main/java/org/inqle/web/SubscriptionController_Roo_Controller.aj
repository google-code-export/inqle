// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.inqle.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.inqle.domain.Participant;
import org.inqle.domain.Question;
import org.inqle.domain.Subscription;
import org.inqle.domain.security.Principal;
import org.inqle.repository.QuestionRepository;
import org.inqle.web.SubscriptionController;
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

privileged aspect SubscriptionController_Roo_Controller {
    
    @Autowired
    QuestionRepository SubscriptionController.questionRepository;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String SubscriptionController.create(@Valid Subscription subscription, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, subscription);
            return "subscriptions/create";
        }
        uiModel.asMap().clear();
        subscription.persist();
        return "redirect:/subscriptions/" + encodeUrlPathSegment(subscription.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String SubscriptionController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Subscription());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (questionRepository.count() == 0) {
            dependencies.add(new String[] { "question", "questions" });
        }
        if (Participant.countParticipants() == 0) {
            dependencies.add(new String[] { "participant", "participants" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "subscriptions/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String SubscriptionController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("subscription", Subscription.findSubscription(id));
        uiModel.addAttribute("itemId", id);
        return "subscriptions/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String SubscriptionController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("subscriptions", Subscription.findSubscriptionEntries(firstResult, sizeNo));
            float nrOfPages = (float) Subscription.countSubscriptions() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("subscriptions", Subscription.findAllSubscriptions());
        }
        addDateTimeFormatPatterns(uiModel);
        return "subscriptions/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String SubscriptionController.update(@Valid Subscription subscription, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, subscription);
            return "subscriptions/update";
        }
        uiModel.asMap().clear();
        subscription.merge();
        return "redirect:/subscriptions/" + encodeUrlPathSegment(subscription.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String SubscriptionController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Subscription.findSubscription(id));
        return "subscriptions/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String SubscriptionController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Subscription subscription = Subscription.findSubscription(id);
        subscription.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/subscriptions";
    }
    
    void SubscriptionController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("subscription_created_date_format", DateTimeFormat.patternForStyle("FF", LocaleContextHolder.getLocale()));
    }
    
    void SubscriptionController.populateEditForm(Model uiModel, Subscription subscription) {
        uiModel.addAttribute("subscription", subscription);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("participants", Participant.findAllParticipants());
        uiModel.addAttribute("questions", questionRepository.findAll());
        uiModel.addAttribute("principals", Principal.findAllPrincipals());
    }
    
    String SubscriptionController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
