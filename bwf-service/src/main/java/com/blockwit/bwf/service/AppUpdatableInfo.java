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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AppUpdatableInfo {

  public static final String APP_EXAMPLE_INFO = "APP_EXAMPLE_INFO";

  private String exampleUpdatableInfo = "Need for update! You should start update task!";

  public synchronized String getExampleUpdatableInfo() {
    return exampleUpdatableInfo;
  }

  public synchronized void setExampleUpdatableInfo(String exampleUpdatableInfo) {
    this.exampleUpdatableInfo = exampleUpdatableInfo;
  }

  public synchronized Map<String, String> getInfoMap() {
    return Map.of(APP_EXAMPLE_INFO, exampleUpdatableInfo);
  }

}
