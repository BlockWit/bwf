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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
public class WithOptions {

  public static <R> R processF(OptionService optionService,
                               List<String> optionNames,
                               Function<Map<String, Option>, R> f,
                               Supplier<R> errF) {
    Set<Option> options = optionService.findByNameIn(optionNames);
    Map<String, Option> optionMap = options.stream()
        .collect(Collectors.toMap(Option::getName, option -> option));
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

  public static void process(OptionService optionService,
                             List<String> optionNames,
                             Consumer<Map<String, Option>> f) {
    Set<Option> options = optionService.findByNameIn(optionNames);
    Map<String, Option> optionMap = options.stream()
        .collect(Collectors.toMap(Option::getName, option -> option));
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
      return;
    f.accept(optionMap);
  }

}
