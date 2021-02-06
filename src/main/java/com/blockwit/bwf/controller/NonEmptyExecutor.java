package com.blockwit.bwf.controller;

import org.springframework.web.servlet.ModelAndView;

public interface NonEmptyExecutor<T> {

	ModelAndView perform(T target);

}
