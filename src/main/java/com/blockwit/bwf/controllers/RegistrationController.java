package com.blockwit.bwf.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @GetMapping("/app/register/reg-step-1")
    public String regStep1Get() {
        return "front/reg-step-1";
    }

    @PostMapping("/app/register/reg-step-1")
    public String regStep1Post() {
        return "front/reg-step-1";
    }

    @GetMapping("/app/login")
    public String home(Model model) {
        return "front/login";
    }

}
