/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.config;

import com.blockwit.bwf.service.OptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class AppInterceptor extends HandlerInterceptorAdapter {

	OptionService optionService;

	public AppInterceptor(OptionService optionService) {
		this.optionService = optionService;
	}

	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler) {
		log.info(prepareCommonLogString("request", request));
		return true;
	}

	@Override
	public void afterCompletion(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler,
		Exception ex) {
		log.info(prepareCommonLogString("response", request));
	}

	public final static String prepareCommonLogString(String target, HttpServletRequest request) {
		// TODO: Create option to hide or show this logs.
		// Add ELK filebeat module folder to project.

		return "target:" + target +
			" to_ip:" + request.getLocalAddr() +
			" to_host:" + request.getLocalName() +
			" from_ip:" + request.getRemoteAddr() +
			" method:" + request.getMethod() +
			" uri:" + request.getRequestURI() +
			" registered:" + ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
	}

}
