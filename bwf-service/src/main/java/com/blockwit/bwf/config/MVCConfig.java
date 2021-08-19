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

package com.blockwit.bwf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

	private final AppInterceptor appInterceptor;

	public MVCConfig(AppInterceptor appInterceptor) {
		this.appInterceptor = appInterceptor;
	}


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebContentInterceptor interceptor = new WebContentInterceptor();
		interceptor.addCacheMapping(CacheControl.maxAge(365, TimeUnit.DAYS)
			.noTransform()
			.mustRevalidate(), "/css/*", "/js/**", "/img/**", "/webjars/**");
		registry.addInterceptor(interceptor);

		registry.addInterceptor(appInterceptor).addPathPatterns("/app/**", "/panel/**");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setOneIndexedParameters(true);
		argumentResolvers.add(resolver);
	}

}
