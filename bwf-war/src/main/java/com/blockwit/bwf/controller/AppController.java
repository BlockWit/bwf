package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.mapping.SwapViewMapper;
import com.blockwit.bwf.service.SwapService;
import com.blockwit.bwf.service.chains.ChainsInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class AppController {

  @Autowired
  private ChainsInfo chainsInfo;

  @Autowired
  private SwapService swapService;

  @Autowired
  private SwapViewMapper swapViewMapper;

  @GetMapping("/")
  public String appHome(Model model) {
    model.addAllAttributes(chainsInfo.getInfoMap());
    model.addAttribute("pageContent", swapViewMapper.map(swapService.findLastSwaps(5), this));
    return "front/pages/home";
  }

  @GetMapping("/panel")
  public String panelHome(Model model) {
    model.addAllAttributes(chainsInfo.getInfoMap());
    return "panel/pages/home";
  }

  @GetMapping("/panel/profile")
  public String panelProfile(Model model) {
    return "panel/pages/profile";
  }

}
