package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.mapping.SwapStartTxLogViewMapper;
import com.blockwit.bwf.repository.SwapStartTxLogsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/panel/startswaplogs")
public class SwapStartLogsController {

  @Autowired
  private SwapStartTxLogsRepository swapStartTxLogsRepository;

  @Autowired
  private SwapStartTxLogViewMapper swapStartTxLogViewMapper;

  @GetMapping
  public ModelAndView appPanelSwapStartLogs() {
    return new ModelAndView("redirect:/panel/startswaplogs/page/1");
  }

  @GetMapping("/page/{pageNumber}")
  public ModelAndView appPanelSwapStartLogsPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/swapStartLogs"),
        pageNumber,
        swapStartTxLogsRepository,
        swapStartTxLogViewMapper);
  }

}
