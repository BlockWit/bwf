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

package com.blockwit.bwf.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolation;
import java.util.Set;

public abstract class CommonValidator implements Validator {

	private final javax.validation.Validator javaxValidator;

	private final Class clazz;

	public CommonValidator(javax.validation.Validator javaxValidator, Class clazz) {
		this.javaxValidator = javaxValidator;
		this.clazz = clazz;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return clazz.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		Set<ConstraintViolation<Object>> validates = javaxValidator.validate(o);
		validates.stream().forEach(cv -> errors.rejectValue(cv.getPropertyPath().toString(), "", cv.getMessage()));
		performValidate(o, errors);
	}

	public void performValidate(Object o, Errors errors) {
	}

}
