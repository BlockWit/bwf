package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.AppContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class ControllerHelper {

	public static final String REFERER = "Referer";

	public static ModelAndView returnToReferer(HttpServletRequest request) {
		return new ModelAndView("redirect:" + getRefererURL(request));
	}

	public static ModelAndView returnToReferer(HttpServletRequest request, RedirectAttributes redirectAttributes, String msg) {
		log.error(msg);
		redirectAttributes.addFlashAttribute("message_error", msg);
		return new ModelAndView("redirect:" + getRefererURL(request));
	}

	public static String getRefererURL(HttpServletRequest request) {
		String referer = request.getHeader(REFERER);
		if (referer == null || referer.isEmpty())
			return "/";
		return referer;
	}

	public static ModelAndView addPageableResult(ModelAndView modelAndView, int pageNumber, JpaRepository repository) {
		PageRequest pageRequest = PageRequest.of(pageNumber - 1, AppContext.DEFAULT_PAGE_SIZE, Sort.by("id").descending());
		Page page = repository.findAll(pageRequest);
		// TODO move prev and next page url calculation logic to views
		int totalPages = page.getTotalPages();

		if (pageNumber > 1)
			modelAndView.addObject("prevPageUrl", pageNumber - 1);
		if (pageNumber < totalPages)
			modelAndView.addObject("nextPageUrl", pageNumber + 1);

		modelAndView.addObject("pageContent", page.getContent());
		return modelAndView;
	}

}
