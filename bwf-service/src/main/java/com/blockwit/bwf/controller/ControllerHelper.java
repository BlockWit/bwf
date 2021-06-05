package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.AppContext;
import com.blockwit.bwf.model.mapping.IMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
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
    redirectAttributes.addFlashAttribute("message_success", msg);
    return new ModelAndView("redirect:" + getRefererURL(request));
  }

  public static String getRefererURL(HttpServletRequest request) {
    String referer = request.getHeader(REFERER);
    if (referer == null || referer.isEmpty())
      return "/";
    return referer;
  }

  public static ModelAndView returnError(HttpServletRequest request,
                                         RedirectAttributes redirectAttributes,
                                         HttpStatus status,
                                         String msg,
                                         Exception e) {
    e.printStackTrace();
    log.error(msg, e);
    redirectAttributes.addFlashAttribute("message_error", msg);
    return new ModelAndView("redirect:" + getRefererURL(request), status);
  }

  public static ModelAndView returnError(HttpServletRequest request,
                                         RedirectAttributes redirectAttributes,
                                         HttpStatus status,
                                         String msg) {
    log.error(msg);
    redirectAttributes.addFlashAttribute("message_error", msg);
    return new ModelAndView("redirect:" + getRefererURL(request), status);
  }

  public static ModelAndView returnError400(HttpServletRequest request,
                                            RedirectAttributes redirectAttributes,
                                            String msg) {
    log.error(msg);
    redirectAttributes.addFlashAttribute("message_error", msg);
    return new ModelAndView("redirect:" + getRefererURL(request), HttpStatus.BAD_REQUEST);
  }

  public static ModelAndView returnSuccess(RedirectAttributes redirectAttributes,
                                           String url,
                                           String msg) {
    redirectAttributes.addFlashAttribute("message_success", msg);
    return new ModelAndView(url);
  }

  public static ModelAndView returnSuccess(String url,
                                           String msg) {
    ModelAndView modelAndView = new ModelAndView(url);
    modelAndView.addObject("message_success", msg);
    return modelAndView;
  }

  public static ModelAndView returnError400(BindingResult bindingResult,
                                            String url,
                                            String msg) {
    ModelAndView modelAndView = new ModelAndView(url, HttpStatus.BAD_REQUEST);
    modelAndView.addObject(bindingResult.getModel());
    modelAndView.addObject("message_error", msg);
    return modelAndView;
  }

  public static ModelAndView returnError500(HttpServletRequest request,
                                            RedirectAttributes redirectAttributes,
                                            String msg) {
    log.error(msg);
    redirectAttributes.addFlashAttribute("message_error", msg);
    return new ModelAndView("redirect:" + getRefererURL(request), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public static ModelAndView addPageableResult(ModelAndView modelAndView,
                                               int pageNumber,
                                               JpaRepository repository) {
    return addPageableResult(modelAndView, pageNumber, repository, null);
  }

  public static <FROM, TO> ModelAndView addPageableResult(ModelAndView modelAndView,
                                                          int pageNumber,
                                                          JpaRepository repository,
                                                          IMapper<FROM, TO> mapper) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, AppContext.DEFAULT_PAGE_SIZE, Sort.by("id").descending());
    Page page = repository.findAll(pageRequest);
    // TODO move prev and next page url calculation logic to views
    int totalPages = page.getTotalPages();

    if (pageNumber > 1)
      modelAndView.addObject("prevPageUrl", pageNumber - 1);
    if (pageNumber < totalPages)
      modelAndView.addObject("nextPageUrl", pageNumber + 1);

    modelAndView.addObject("pageContent", mapper == null ? page.getContent() : mapper.map(page.getContent(), null));
    return modelAndView;
  }

}
