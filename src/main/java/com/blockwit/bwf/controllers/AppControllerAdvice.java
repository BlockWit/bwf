package com.blockwit.bwf.controllers;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AppControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;

        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("status", "500");
        mav.addObject("error", "Internal server error");
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        mav.setViewName("error");
        return mav;
    }

/*
    @ExceptionHandler(value = DefaultAdminRoleNotExistsServiceException.class)
    public ModelAndView exception(DefaultAdminRoleNotExistsServiceException e) {
        return new ModelAndView("error/customError", Map.of("description", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = DefaultUserRoleNotExistsServiceException.class)
    public ModelAndView exception(DefaultUserRoleNotExistsServiceException e) {
        return new ModelAndView("error/customError", Map.of("description", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
*/
}
