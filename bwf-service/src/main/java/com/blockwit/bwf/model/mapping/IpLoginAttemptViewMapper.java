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

package com.blockwit.bwf.model.mapping;

import com.blockwit.bwf.model.LoginAttempts;
import com.blockwit.bwf.service.OptionService;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class IpLoginAttemptViewMapper implements IMapper<LoginAttempts, IpLoginAttemptView> {

  private final OptionService optionService;

  public IpLoginAttemptViewMapper(OptionService optionService) {
    this.optionService = optionService;
  }

  @Override
  public List<IpLoginAttemptView> map(List<LoginAttempts> loginAttempts, Object context) {
    return StreamEx.of(loginAttempts)
        .map(t -> map(t, context))
        .filter(Optional::isPresent)
        .map(Optional::get).toList();
  }

  @Override
  public Optional<IpLoginAttemptView> map(LoginAttempts loginAttempt, Object context) {
    return fromModelToView(loginAttempt);
  }

  private static Optional<IpLoginAttemptView> fromModelToView(LoginAttempts model) {
    return Optional.of(IpLoginAttemptView.builder()
        .id(model.getId())
        .addr(model.getAddr())
        .badAttemptsCount(model.getBadAttemptsCount())
        .lastBadAttempt(model.getLastBadAttempt() == null ? "" : dateTimeFormatter.print(model.getLastBadAttempt()))
        .build());
  }

}
