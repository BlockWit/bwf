package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.mapping.SwapPairViewMapper;
import com.blockwit.bwf.repository.SwapPairRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/panel/swappairs")
public class SwapPairsController {

  @Autowired
  SwapPairRepository swapPairRepository;

  @Autowired
  SwapPairViewMapper swapPairViewMapper;

  @GetMapping
  public ModelAndView appPanelSwapPairs() {
    return new ModelAndView("redirect:/panel/swappairs/page/1");
  }

  @GetMapping("/page/{pageNumber}")
  public ModelAndView appPanelSwapPairsPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/swapPairs"),
        pageNumber,
        swapPairRepository,
        swapPairViewMapper);
  }

}
