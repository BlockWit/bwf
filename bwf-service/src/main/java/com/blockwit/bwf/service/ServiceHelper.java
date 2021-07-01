/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.service;

import com.blockwit.bwf.model.Error;
import com.blockwit.bwf.model.IOwnable;
import com.blockwit.bwf.repository.AccountRepository;
import com.blockwit.bwf.service.utils.WithOptional;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServiceHelper {

  public static <T extends IOwnable> Either<Error, T> findOwnableById(
      AccountRepository accountRepository,
      JpaRepository<T, Long> jpaRepository,
      long targetId) {
    return WithOptional.process(
        jpaRepository.findById(targetId),
        () -> Either.left(new Error(Error.EC_TARGET_NOT_FOUND, Error.EM_TARGET_NOT_FOUND + ": " + targetId)),
        target -> AccountService.withAccount(
            accountRepository,
            target.getOwnerId(), account -> {
              target.setOwner(account);
              return Either.right(target);
            })
    );
  }

}
