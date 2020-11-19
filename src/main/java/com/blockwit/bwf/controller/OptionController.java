package com.blockwit.bwf.controller;

import com.blockwit.bwf.entity.AppContext;
import com.blockwit.bwf.entity.Option;
import com.blockwit.bwf.repository.OptionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@Controller
public class OptionController {

	private final OptionRepository optionRepository;

	public OptionController(OptionRepository optionRepository) {
		this.optionRepository = optionRepository;
	}

	@GetMapping("/panel/options")
	public ModelAndView appPanelOptions() {
		return new ModelAndView("redirect:/panel/options/page/1");
	}

	@GetMapping("/panel/options/page/{pageNumber}")
	public ModelAndView appPanelOptionsPage(@PathVariable("pageNumber") int pageNumber) {
		ModelAndView modelAndView = new ModelAndView("panel/options");
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
