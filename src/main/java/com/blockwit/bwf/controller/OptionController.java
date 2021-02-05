package com.blockwit.bwf.controller;

import com.blockwit.bwf.form.EditOption;
import com.blockwit.bwf.model.AppContext;
import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.repository.OptionRepository;
import com.blockwit.bwf.service.OptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/panel/options")
public class OptionController {

	private final OptionRepository optionRepository;

	private final OptionService optionService;

	public OptionController(OptionService optionService, OptionRepository optionRepository) {
		this.optionService = optionService;
		this.optionRepository = optionRepository;
	}

	@GetMapping
	public ModelAndView appPanelOptions() {
		return new ModelAndView("redirect:/panel/options/page/1");
	}

	@GetMapping("/option/{optionId}/edit")
	public ModelAndView appPanelOptionsPage(RedirectAttributes redirectAttrs, @PathVariable("optionId") long optionId) {
		Optional<Option> optionOpt = optionRepository.findById(optionId);
		if (optionOpt.isPresent()) {
			return new ModelAndView("panel/pages/editOption", Map.of("option", Option.editOptionForm(optionOpt.get())));
		} else {
			log.error("Option with id " + optionId + " not found!");
			redirectAttrs.addFlashAttribute("message_success", "Option with id " + optionId + " not found!");
			return new ModelAndView("redirect:/panel/options");
		}
	}

	@PostMapping("/option/{optionId}/update")
	public ModelAndView appPanelOptionsPage(RedirectAttributes redirectAttrs,
																					@ModelAttribute("newTask")
																					@Valid EditOption editOption,
																					BindingResult bindingResult,
																					@PathVariable("optionId") long optionId) {
		log.debug("Perform edit option");

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
		ModelAndView modelAndView = new ModelAndView("panel/pages/options");
		// TODO move prev and next page url calculation logic to views
		Pageable pageRequest =
			PageRequest.of(pageNumber - 1, AppContext.DEFAULT_PAGE_SIZE, Sort.by("id").descending());
		Page<Option> page = optionRepository.findAll(pageRequest);
		int totalPages = page.getTotalPages();

		if (pageNumber > 1)
			modelAndView.addObject("prevPageUrl", pageNumber - 1);
		if (pageNumber < totalPages)
			modelAndView.addObject("nextPageUrl", pageNumber + 1);

		modelAndView.addObject("pageContent", page.getContent());
		modelAndView.addObject("title", "Main page");

		return modelAndView;
	}

}
