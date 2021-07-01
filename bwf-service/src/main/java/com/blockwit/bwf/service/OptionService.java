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

package com.blockwit.bwf.service;

import com.blockwit.bwf.model.Option;
import com.blockwit.bwf.repository.OptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OptionService {

  public final static String OPTION_APP_NAME = "APP_NAME";
  public final static String OPTION_APP_SHORT_NAME = "APP_SHORT_NAME";
  public final static String OPTION_APP_VERSION = "APP_VERSION";
  public final static String OPTION_APP_YEAR = "APP_YEAR";

  public final static String OPTION_MEDIA_PATH = "MEDIA_PATH";

  public static final String OPTION_LOGIN_ATTEMPTS_LIMIT = "OPTION_LOGIN_ATTEMPTS_LIMIT";
  public static final String OPTION_LOGIN_LOCK_PERIOD = "OPTION_LOGIN_LOCK_PERIOD";
  public static final String OPTION_LOGIN_TRY_PERIOD = "OPTION_LOGIN_TRY_PERIOD";

  public final static String OPTION_PROFILE = "OPTION_PROFILE";

  public final static String OPTION_TYPE_STRING = "string";
  public final static String OPTION_TYPE_LONG = "long";
  public final static String OPTION_TYPE_INT = "int";
  public final static String OPTION_TYPE_ETH_AMOUNT = "eth_amount";

  public final static String OPTION_PROFILE_VALUE_MAIN = "OP_MAIN";


  private final OptionRepository optionRepository;
  private final Map<String, Option> defaultOptions;
  private final List<String> defaultOptionNames;

  public static final String SEPARATOR = "_";

  public OptionService(OptionRepository optionRepository) {
    this.optionRepository = optionRepository;

    defaultOptions = new HashMap<>();

    defaultOptions.putAll(Map.of(
        OPTION_APP_NAME, new Option(0L, OPTION_APP_NAME, OPTION_TYPE_STRING, "TenSet", "Application full name"),
        OPTION_APP_VERSION, new Option(0L, OPTION_APP_VERSION, OPTION_TYPE_STRING, "next", "Application version"),
        OPTION_APP_SHORT_NAME, new Option(0L, OPTION_APP_SHORT_NAME, OPTION_TYPE_STRING, "TenSet", "Application short name"),
        OPTION_APP_YEAR, new Option(0L, OPTION_APP_YEAR, OPTION_TYPE_STRING, "2021", "Application year"),
        OPTION_LOGIN_ATTEMPTS_LIMIT, new Option(0L, OPTION_LOGIN_ATTEMPTS_LIMIT, OPTION_TYPE_INT, "3", "Count of bad login attempts before account locking"),
        OPTION_LOGIN_LOCK_PERIOD, new Option(0L, OPTION_LOGIN_LOCK_PERIOD, OPTION_TYPE_LONG, "86400000", "Account lock period in case of login attempts limit exceeded"),
        OPTION_LOGIN_TRY_PERIOD, new Option(0L, OPTION_LOGIN_TRY_PERIOD, OPTION_TYPE_LONG, "3600000", "Period between two bad login attempts to increase bad attempts counter"),
        OPTION_MEDIA_PATH, new Option(0L, OPTION_MEDIA_PATH, OPTION_TYPE_STRING, "/opt/bwf/media", "Media path")
    ));

    defaultOptions.putAll(Map.of(
        OPTION_PROFILE, new Option(0L, OPTION_PROFILE, OPTION_TYPE_STRING, OPTION_PROFILE_VALUE_MAIN, "Profile prefix for properties")
    ));

    defaultOptionNames = defaultOptions.keySet().stream().collect(Collectors.toList());

    getAllDefaultValues();
  }

  @Transactional
  public Map<String, String> getAllDefaultValues() {
    Map<String, String> namesToValues = optionRepository.findByNameIn(defaultOptionNames)
        .stream().collect(Collectors.toMap(Option::getName, Option::getValue));

    if (!namesToValues.keySet().containsAll(defaultOptionNames)) {
      for (String name : defaultOptionNames) {
        if (!namesToValues.containsKey(name)) {
          optionRepository.save(defaultOptions.get(name));
        }
      }

      namesToValues = optionRepository.findByNameIn(defaultOptionNames)
          .stream().collect(Collectors.toMap(Option::getName, Option::getValue));
    }

    return namesToValues;
  }

  @Transactional
  public Optional<Option> update(Option option) {
    return _update(option);
  }

  public Optional<Option> _update(Option option) {
    return optionRepository.findById(option.getId()).flatMap(t -> {
      Option newOption = optionRepository.save(option);
      return Optional.of(newOption);
    });
  }

  private String _getActiveProfile() {
    return findByName(OPTION_PROFILE).get().getValue();
  }

  public String getActiveProfile() {
    return _getActiveProfile();
  }

  public Optional<Option> findProfiledByName(String name) {
    String profile = _getActiveProfile();
    return optionRepository.findByName(profile + SEPARATOR + name);
  }

  public Set<Option> findProfiledByNameIn(List<String> optionNames) {
    String profile = _getActiveProfile();
    return optionRepository.findByNameIn(optionNames.stream()
        .map(t -> profile + "_" + t).collect(Collectors.toList()));
  }

  public Optional<Option> findByName(String name) {
    return optionRepository.findByName(name);
  }

  public Set<Option> findByNameIn(List<String> optionNames) {
    return optionRepository.findByNameIn(optionNames);
  }

  public static Object getPerformedValue(Option curOption) {
    Object result = null;
    if (curOption.getType().equals(OptionService.OPTION_TYPE_ETH_AMOUNT)) {
      result = new BigInteger(curOption.getValue());
    } else if (curOption.getType().equals(OptionService.OPTION_TYPE_LONG)) {
      try {
        result = Long.parseLong(curOption.getValue());
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    } else if (curOption.getType().equals(OptionService.OPTION_TYPE_INT)) {
      try {
        result = Integer.parseInt(curOption.getValue());
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    } else if (curOption.getType().equals(OptionService.OPTION_TYPE_STRING)) {
      result = curOption.getValue();
    }

    if (result == null)
      log.error("Option " + curOption.getName() + " with type - " + curOption.getType() + " have wrong value: \"" + curOption.getValue() + "\"");

    return result;
  }

  @Transactional
  public Optional<Option> updateProfiled(Option option) {
    String profile = _getActiveProfile();
    return optionRepository.findById(option.getId()).flatMap(t -> {
      Option newOption = optionRepository.save(option.toBuilder().name(profile + SEPARATOR + option.getName()).build());
      return Optional.of(newOption.toBuilder().name(option.getName().substring(profile.length() + SEPARATOR.length())).build());
    });
  }

}
