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

import com.blockwit.bwf.model.LoginAttempts;
import com.blockwit.bwf.repository.IpLoginAttemptsRepository;
import com.blockwit.bwf.service.utils.WithOptional;
import com.blockwit.bwf.service.utils.WithOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginAttemptService {

  @Autowired
  IpLoginAttemptService ipLoginAttemptService;

  @Autowired
  IpLoginAttemptsRepository ipLoginAttemptsRepository;

  @Autowired
  OptionService optionService;

  public void loginSucceeded(String key) {
    ipLoginAttemptService.resetAttempts(key);
  }

  public void loginFailed(String key) {
    ipLoginAttemptsRepository.findFirstByAddr(key).ifPresentOrElse(logAt -> {
      WithOptions.process(optionService,
          List.of(OptionService.OPTION_LOGIN_TRY_PERIOD), options -> {
            Long loginTryPeriod = (Long) options.get(OptionService.OPTION_LOGIN_TRY_PERIOD).getPerformedValue();

            if (logAt.getLastBadAttempt() + loginTryPeriod > System.currentTimeMillis())
              ipLoginAttemptService.save(logAt.toBuilder()
                  .lastBadAttempt(System.currentTimeMillis())
                  .badAttemptsCount(logAt.getBadAttemptsCount() + 1)
                  .build());
            else
              ipLoginAttemptService.save(logAt.toBuilder()
                  .lastBadAttempt(System.currentTimeMillis())
                  .badAttemptsCount(1)
                  .build());

          });
    }, () ->
        ipLoginAttemptService.save(LoginAttempts.builder()
            .badAttemptsCount(1)
            .lastBadAttempt(System.currentTimeMillis())
            .addr(key)
            .build()));
  }

  public boolean isBlocked(String key) {
    return WithOptional.process(ipLoginAttemptsRepository.findFirstByAddr(key),
        () -> false,
        logAt -> WithOptions.processF(optionService,
            List.of(OptionService.OPTION_LOGIN_ATTEMPTS_LIMIT,
                OptionService.OPTION_LOGIN_LOCK_PERIOD), options -> {
              Integer loginAttemptsLimit = (Integer) options.get(OptionService.OPTION_LOGIN_ATTEMPTS_LIMIT).getPerformedValue();
              Long loginLockPeriod = (Long) options.get(OptionService.OPTION_LOGIN_LOCK_PERIOD).getPerformedValue();
              return logAt.getLastBadAttempt() != null &&
                  logAt.getBadAttemptsCount() != null &&
                  logAt.getBadAttemptsCount() >= loginAttemptsLimit &&
                  (logAt.getLastBadAttempt() + loginLockPeriod) >= System.currentTimeMillis();
            }, () -> true));
  }

}
