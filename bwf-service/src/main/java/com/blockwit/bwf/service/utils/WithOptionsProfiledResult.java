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
import one.util.streamex.StreamEx;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class WithOptionsProfiledResult {

	public static <R> Optional<R> process(
		OptionService optionService,
		List<String> optionNames,
		Function<Map<String, Option>, Optional<R>> f) {
		String profile = optionService.getActiveProfile();
		Set<Option> options = optionService.findProfiledByNameIn(optionNames);
		Map<String, Option> optionMap = StreamEx.of(options)
			.map(option -> option.toBuilder().name(option.getName().substring(profile.length() + OptionService.SEPARATOR.length())).build())
			.toMap(Option::getName, option -> option);
		boolean error = false;
		for (String optionName : optionNames) {
			Option curOption = optionMap.get(optionName);
			if (curOption == null) {
				log.error("Option " + optionName + " not found!");
				error = true;
			} else if (curOption.getPerformedValue() == null) {
				error = true;
			}
		}
		if (error)
			return Optional.empty();
		return f.apply(optionMap);
	}


	public static <R> R process(
			OptionService optionService,
			List<String> optionNames,
			Function<Map<String, Option>, R> f, Supplier<R> errF) {
		String profile = optionService.getActiveProfile();
		Set<Option> options = optionService.findProfiledByNameIn(optionNames);
		Map<String, Option> optionMap = StreamEx.of(options)
				.map(option -> option.toBuilder().name(option.getName().substring(profile.length() + OptionService.SEPARATOR.length())).build())
				.toMap(Option::getName, option -> option);
		boolean error = false;
		for (String optionName : optionNames) {
			Option curOption = optionMap.get(optionName);
			if (curOption == null) {
				log.error("Option " + optionName + " not found!");
				error = true;
			} else if (curOption.getPerformedValue() == null) {
				error = true;
			}
		}
		if (error)
			return errF.get();
		return f.apply(optionMap);
	}


}
