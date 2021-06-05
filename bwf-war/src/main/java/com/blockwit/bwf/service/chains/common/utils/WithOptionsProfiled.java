package com.blockwit.bwf.service.chains.common.utils;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.service.OptionService;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class WithOptionsProfiled {

  public static Boolean processIsRepeat(OptionService optionService,
                                        String optionName,
                                        Function<Option, Boolean> f) {
    Optional<Option> optionOpt = optionService.findProfiledByName(optionName);
    if (optionOpt.isEmpty()) {
      log.error("Option " + optionName + " not found!");
      return false;
    }
    return f.apply(optionOpt.get());
  }

  // TODO: Fixme to Either
  public static <R> R processF(OptionService optionService,
                               List<String> optionNames,
                               Function<Map<String, Option>, R> f) {
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
      return null;
    return f.apply(optionMap);
  }

  public static void processWithValues(OptionService optionService,
                                       List<String> optionNames,
                                       Consumer<List<Object>> f) {
    String profile = optionService.getActiveProfile();
    Set<Option> options = optionService.findProfiledByNameIn(optionNames);
    Map<String, Option> optionMap = StreamEx.of(options)
        .map(option -> option.toBuilder().name(option.getName().substring(profile.length() + OptionService.SEPARATOR.length())).build())
        .toMap(Option::getName, option -> option);
    boolean error = false;
    List<Object> optionsValues = new ArrayList<>();
    for (String optionName : optionNames) {
      Option curOption = optionMap.get(optionName);
      if (curOption == null) {
        log.error("Option " + optionName + " not found!");
        error = true;
      } else {
        Object performedValue = curOption.getPerformedValue();
        if (performedValue == null) {
          error = true;
        } else {
          optionsValues.add(performedValue);
        }
      }
    }
    if (error)
      return;
    f.accept(optionsValues);
  }

  public static void process(OptionService optionService,
                             List<String> optionNames,
                             Consumer<Map<String, Option>> f) {
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
      return;
    f.accept(optionMap);
  }

}
