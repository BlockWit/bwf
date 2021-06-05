package com.blockwit.bwf.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class OptionalExecutor<T> {

  public static <T> ModelAndView perform(String targetName,
                                         long targetId,
                                         JpaRepository repository,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes,
                                         Function<T, ModelAndView> nonEmptyExecutor) {
    return perform(targetName,
        targetId,
        repository.findById(targetId),
        request,
        redirectAttributes,
        nonEmptyExecutor);
  }

  public static <T> ModelAndView perform(String targetName,
                                         long targetId,
                                         Optional<T> targetOpt,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes,
                                         Function<T, ModelAndView> nonEmptyExecutor) {
    if (targetOpt.isPresent())
      return nonEmptyExecutor.apply(targetOpt.get());
    return ControllerHelper.returnToReferer(request,
        redirectAttributes,
        targetName + " with Id " + targetId + " not found!");
  }

}
