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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IpLoginAttemptService {

  @Autowired
  IpLoginAttemptsRepository ipLoginAttemptsRepository;

  @Transactional
  public Optional<LoginAttempts> deleteById(long accountId) {
    return WithOptional.process(ipLoginAttemptsRepository.findById(accountId), () -> Optional.empty(), account -> {
      ipLoginAttemptsRepository.deleteById(accountId);
      return Optional.of(account);
    });
  }

  @Transactional
  public void resetAttempts(String key) {
    ipLoginAttemptsRepository.findFirstByAddr(key).ifPresent(ipLoginAttemptsRepository::delete);
  }

  @Transactional
  public LoginAttempts save(LoginAttempts l) {
    return ipLoginAttemptsRepository.save(l);
  }
}

