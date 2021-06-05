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
