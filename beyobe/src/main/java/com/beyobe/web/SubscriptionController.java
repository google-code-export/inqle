package com.beyobe.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.beyobe.domain.Subscription;

@RequestMapping("/admin/subscriptions")
@Controller
@RooWebScaffold(path = "admin/subscriptions", formBackingObject = Subscription.class)
public class SubscriptionController {
}
