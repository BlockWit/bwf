package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.mapping.SwapFinalizeTxLogViewMapper;
import com.blockwit.bwf.repository.SwapFinalizeTxLogsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/panel/finswaplogs")
public class SwapFinalizeLogsController {

  @Autowired
  private SwapFinalizeTxLogsRepository swapFinalizeTxLogsRepository;

  @Autowired
  private SwapFinalizeTxLogViewMapper swapFinalizeTxLogViewMapper;

  @GetMapping
  public ModelAndView appPanelSwapFinalizeLogs() {
    return new ModelAndView("redirect:/panel/finswaplogs/page/1");
  }

  @GetMapping("/page/{pageNumber}")
  public ModelAndView appPanelSwapFinalizeLogsPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/swapFinalizeLogs"),
        pageNumber,
        swapFinalizeTxLogsRepository,
        swapFinalizeTxLogViewMapper);
  }

}
