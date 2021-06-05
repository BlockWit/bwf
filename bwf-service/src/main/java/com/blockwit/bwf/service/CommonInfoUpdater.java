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

import com.blockwit.bwf.model.tasks.SystemSchedTasks;
import com.blockwit.bwf.service.utils.PerformSchedTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class CommonInfoUpdater {

  @Autowired
  private SchedTasksService schedTasksService;

  @Autowired
  AppUpdatableInfo appUpdatableInfo;

  @Autowired
  OptionService optionService;

  private AtomicBoolean destroy = new AtomicBoolean(false);
  private AtomicBoolean destroyFinished = new AtomicBoolean(false);

  @PreDestroy
  public void waitDestroy() {
    log.info("Waiting for common updater processor destroy");
    destroy.set(true);
    while (!destroyFinished.get()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    log.info("Common updater processor destroy process finished");
  }

  @Scheduled(fixedDelay = 1000)
  protected void commonInfoUpdater() {
    PerformSchedTask.process(destroy,
        destroyFinished,
        schedTasksService,
        SystemSchedTasks.TASK_ID_COMMON_INFO_UPDATER, () -> {
          appUpdatableInfo.setExampleUpdatableInfo("BWF last info update " + System.currentTimeMillis());
        });
  }

}
