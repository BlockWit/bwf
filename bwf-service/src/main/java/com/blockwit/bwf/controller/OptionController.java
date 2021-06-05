package com.blockwit.bwf.controller;

import com.blockwit.bwf.form.EditOption;
import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.repository.OptionRepository;
import com.blockwit.bwf.service.OptionService;
import com.blockwit.bwf.validator.EditOptionValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/panel/options")
public class OptionController {

  @Autowired
  private OptionRepository optionRepository;

  @Autowired
  private OptionService optionService;

  @Autowired
  private EditOptionValidator editOptionValidator;

  @GetMapping
  public ModelAndView appPanelOptions() {
    return new ModelAndView("redirect:/panel/options/page/1");
  }

  @GetMapping("/option/{optionId}/edit")
  public ModelAndView editOption(HttpServletRequest request,
                                 RedirectAttributes redirectAttributes,
                                 @PathVariable("optionId") long optionId) {
    return OptionalExecutor.<Option>perform("Option",
        optionId,
        optionRepository,
        request,
        redirectAttributes,
        option -> new ModelAndView("panel/pages/editOption", Map.of("option", Option.editOptionForm(option))));
  }

  @PostMapping("/option/{optionId}/update")
  public ModelAndView updateOption(RedirectAttributes redirectAttrs,
                                   @ModelAttribute("newTask")
                                   @Valid EditOption editOption,
                                   BindingResult bindingResult,
                                   @PathVariable("optionId") long optionId) {
    log.debug("Perform edit option");

    editOptionValidator.validate(editOption, bindingResult);
    if (bindingResult.hasErrors())
      return new ModelAndView("panel/pages/editOption", bindingResult.getModel(), HttpStatus.BAD_REQUEST);

    if (optionId != editOption.getId()) {
      redirectAttrs.addFlashAttribute("message_error", "Option Id from URL not equals to option Id " + editOption.getId() + " from form!");
      return new ModelAndView("panel/pages/editOption", bindingResult.getModel(), HttpStatus.BAD_REQUEST);
    }

    Option option = Option.fromEditOptionForm(editOption);

    log.debug("Update option with Id " + editOption.getId());
    if (optionService.update(option).isEmpty()) {
      redirectAttrs.addFlashAttribute("message_error", "Can't update option with Id " + editOption.getId() + "!");
      return new ModelAndView("panel/pages/editOption", bindingResult.getModel(), HttpStatus.BAD_REQUEST);
    }

    log.debug("Option with id " + optionId + " successfully updated!");

    redirectAttrs.addFlashAttribute("message_success", "Option with id " + optionId + " successfully updated!");
    return new ModelAndView("redirect:/panel/options");
  }


  @GetMapping("/page/{pageNumber}")
  public ModelAndView appPanelOptionsPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/options"), pageNumber, optionRepository);
  }

}
