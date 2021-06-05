package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.chain.SwapStatus;
import com.blockwit.bwf.model.mapping.SwapViewMapper;
import com.blockwit.bwf.repository.SwapRepository;
import com.blockwit.bwf.service.SwapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/panel/swaps")
public class SwapController {

  @Autowired
  SwapRepository swapRepository;

  @Autowired
  SwapService swapService;

  @Autowired
  SwapViewMapper swapViewMapper;

  @GetMapping
  public ModelAndView appPanelSwaps() {
    return new ModelAndView("redirect:/panel/swaps/page/1");
  }

  @GetMapping("/page/{pageNumber}")
  public ModelAndView appPanelSwapsPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/swaps"),
        pageNumber,
        swapRepository,
        swapViewMapper);
  }

  @GetMapping("/swap/{swapId}/{action}")
  public ModelAndView changeTaskStatus(HttpServletRequest request,
                                       RedirectAttributes redirectAttributes,
                                       @PathVariable("swapId") long taskId,
                                       @PathVariable("action") String action) {
    SwapStatus newSwapStatus;
    SwapStatus oldSwapStatus;
    if (action.equals("retry-send")) {
      oldSwapStatus = SwapStatus.SWAP_OPPOSITE_CHAIN_TX_SENDING_FAILED;
      newSwapStatus = SwapStatus.SWAP_FINALIZATION_RETRY;
    } else {
      redirectAttributes.addFlashAttribute("message_error", "Can't perform action " + action + "!");
      return new ModelAndView("redirect:/panel/swaps");
    }

    swapService.tryChangeStatus(taskId, oldSwapStatus, newSwapStatus).fold(
        error -> redirectAttributes.addFlashAttribute("message_error", error.getDescr())
        , task -> redirectAttributes.addFlashAttribute("message_success", "Task " + taskId + " successfully updated")
    );

    return new ModelAndView("redirect:/panel/swaps");
  }

}

