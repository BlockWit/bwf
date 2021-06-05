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

package com.blockwit.bwf.service.utils;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.service.OptionService;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class WithOption {

	public static void process(OptionService optionService, String optionName, Consumer<Option> f) {
		optionService.findByName(optionName).ifPresentOrElse(f::accept, () -> log.error("Option " + optionName + " not found!"));
	}

}
