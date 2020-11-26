package com.blockwit.bwf.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class AppController {

	@GetMapping("/")
	public String appHome(Model model) {
		model.addAttribute("title", "Main page");
		return "pages/index";
	}

	@GetMapping("/panel")
	public String panelHome(Model model) {
		model.addAttribute("title", "Main page");
		return "panel/home";
	}

	@GetMapping("/panel/profile")
	public String panelProfile(Model model) {
		model.addAttribute("title", "Main page");
		return "panel/profile";
	}

}
