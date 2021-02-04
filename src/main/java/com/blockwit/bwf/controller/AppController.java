package com.blockwit.bwf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class AppController {

	@GetMapping("/")
	public String appHome(Model model) {
		model.addAttribute("title", "Main page");
		return "front/pages/home";
	}

	@GetMapping("/panel")
	public String panelHome(Model model) {
		model.addAttribute("title", "Main page");
		return "panel/pages/home";
	}

	@GetMapping("/panel/profile")
	public String panelProfile(Model model) {
		model.addAttribute("title", "Main page");
		return "panel/pages/profile";
	}

}
