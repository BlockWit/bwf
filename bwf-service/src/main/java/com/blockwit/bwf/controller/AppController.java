package com.blockwit.bwf.controller;

import com.blockwit.bwf.service.AppUpdatableInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class AppController {

  @Autowired
  private AppUpdatableInfo appUpdatableInfo;

  @GetMapping("/")
  public String appHome(Model model) {
    model.addAllAttributes(appUpdatableInfo.getInfoMap());
    return "front/pages/home";
  }

  @GetMapping("/panel")
  public String panelHome(Model model) {
    model.addAllAttributes(appUpdatableInfo.getInfoMap());
    return "panel/pages/home";
  }

  @GetMapping("/panel/profile")
  public String panelProfile(Model model) {
    return "panel/pages/profile";
  }

}
