package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.mapping.TxViewMapper;
import com.blockwit.bwf.repository.TxsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/panel/txs")
public class TxsController {

  @Autowired
  TxsRepository txsRepository;

  @Autowired
  TxViewMapper txViewMapper;

  @GetMapping
  public ModelAndView appPanelTxs() {
    return new ModelAndView("redirect:/panel/txs/page/1");
  }

  @GetMapping("/page/{pageNumber}")
  public ModelAndView appPanelTxsPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/txs"),
        pageNumber,
        txsRepository,
        txViewMapper);
  }

}
