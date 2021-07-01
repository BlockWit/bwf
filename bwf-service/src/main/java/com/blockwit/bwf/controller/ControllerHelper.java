/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.AppContext;
import com.blockwit.bwf.model.IOwnable;
import com.blockwit.bwf.model.mapping.IMapper;
import com.blockwit.bwf.model.media.Media;
import com.blockwit.bwf.repository.AccountRepository;
import com.blockwit.bwf.repository.MediaRepository;
import com.blockwit.bwf.service.ServiceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class ControllerHelper {

  public static final String REFERER = "Referer";

  public static ModelAndView returnToReferer(HttpServletRequest request) {
    return new ModelAndView("redirect:" + getRefererURL(request));
  }

  public static ResponseEntity unauthorizedResponseEntity(String errorMsg) {
    log.error(errorMsg);
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .contentType(MediaType.TEXT_PLAIN)
        .contentLength(errorMsg.length())
        .body(errorMsg);
  }

  public static ResponseEntity internalErrorResponseEntity(String errorMsg) {
    log.error(errorMsg);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.TEXT_PLAIN)
        .contentLength(errorMsg.length())
        .body(errorMsg);
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

  public static <T extends IOwnable, R> ResponseEntity<R> responseEntityWithOwnable(AccountRepository accountRepository,
                                                                                    JpaRepository<T, Long> jpaRepository,
                                                                                    Long targetId,
                                                                                    HttpServletRequest request,
                                                                                    RedirectAttributes redirectAttributes,
                                                                                    Function<T, ResponseEntity<R>> f) {
    return ServiceHelper.findOwnableById(
        accountRepository,
        jpaRepository,
        targetId).fold(
        error -> returnResponseEntityError404(request, redirectAttributes, error.getDescr()),
        target -> f.apply(target)
    );
  }

  public static <T extends IOwnable> ModelAndView withOwnable(AccountRepository accountRepository,
                                                              JpaRepository<T, Long> jpaRepository,
                                                              Long targetId,
                                                              HttpServletRequest request,
                                                              RedirectAttributes redirectAttributes,
                                                              Function<T, ModelAndView> f) {
    return ServiceHelper.findOwnableById(
        accountRepository,
        jpaRepository,
        targetId).fold(
        error -> returnError404(request, redirectAttributes, error.getDescr()),
        target -> f.apply(target)
    );
  }

  public static <R> ResponseEntity<R> responseEntityMediaWithOwnableSec(AccountRepository accountRepository,
                                                                        MediaRepository mediaRepository,
                                                                        Long targetId,
                                                                        HttpServletRequest request,
                                                                        RedirectAttributes redirectAttributes,
                                                                        Function<Media, ResponseEntity<R>> f) {
    return responseEntityWithOwnable(
        accountRepository,
        mediaRepository,
        targetId,
        request,
        redirectAttributes,
        target ->
            AccessContextHelper.access(
                () -> f.apply(target),
                account -> Optional.ofNullable(target.getOwnerId().equals(account.getId()) ? f.apply(target) : null),
                () -> target.getPub() ? f.apply(target) : unauthorizedResponseEntity("Unauthorized")
            )
    );
  }

  /**
   *
   * TODO: Should check other cases -> because it calles when previous cases not allow access to resource
   *
   * @param accountRepository
   * @param jpaRepository
   * @param targetId
   * @param request
   * @param redirectAttributes
   * @param f
   * @param <T>
   * @param <R>
   * @return
   */
  public static <T extends IOwnable, R> ResponseEntity<R> responseEntityWithOwnableSec(AccountRepository accountRepository,
                                                                                       JpaRepository<T, Long> jpaRepository,
                                                                                       Long targetId,
                                                                                       HttpServletRequest request,
                                                                                       RedirectAttributes redirectAttributes,
                                                                                       Function<T, ResponseEntity<R>> f) {
    return responseEntityWithOwnable(
        accountRepository,
        jpaRepository,
        targetId,
        request,
        redirectAttributes,
        target ->
            AccessContextHelper.access(
                () -> f.apply(target),
                account -> Optional.ofNullable(target.getOwnerId().equals(account.getId()) ? f.apply(target) : null),
                () -> f.apply(target)
            )
    );
  }

  public static <T extends IOwnable> ModelAndView withOwnableSec(AccountRepository accountRepository,
                                                                 JpaRepository<T, Long> jpaRepository,
                                                                 Long targetId,
                                                                 HttpServletRequest request,
                                                                 RedirectAttributes redirectAttributes,
                                                                 Function<T, ModelAndView> f) {
    return withOwnable(
        accountRepository,
        jpaRepository,
        targetId,
        request,
        redirectAttributes,
        target ->
            AccessContextHelper.access(
                () -> f.apply(target),
                account -> Optional.ofNullable(target.getOwnerId().equals(account.getId()) ? f.apply(target) : null),
                () -> f.apply(target)
            )
    );
  }

  public static ResponseEntity returnResponseEntityError404(HttpServletRequest request,
                                                            RedirectAttributes redirectAttributes,
                                                            String msg) {
    return internalErrorResponseEntity(msg);
  }

  public static ModelAndView returnError404(HttpServletRequest request,
                                            RedirectAttributes redirectAttributes,
                                            String msg) {
    log.error(msg);
    redirectAttributes.addFlashAttribute("message_error", msg);
    return new ModelAndView("redirect:" + getRefererURL(request), HttpStatus.NOT_FOUND);
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

  public static <FROM, TO> ModelAndView addPageableResult(ModelAndView modelAndView,
                                                          int pageNumber,
                                                          Function<PageRequest, Page<FROM>> pageProvider,
                                                          IMapper<FROM, TO> mapper) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, AppContext.DEFAULT_PAGE_SIZE, Sort.by("id").descending());
    Page<FROM> page = pageProvider.apply(pageRequest);
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
