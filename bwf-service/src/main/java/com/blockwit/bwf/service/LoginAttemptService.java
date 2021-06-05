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
