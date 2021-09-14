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

package com.blockwit.bwf.security;

import com.blockwit.bwf.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthenticationFailureEventListener implements
    ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private LoginAttemptService loginAttemptService;

  @Override
  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
    final String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader == null) {
      loginAttemptService.loginFailed(request.getRemoteAddr());
    } else {
      loginAttemptService.loginFailed(xfHeader.split(",")[0]);
    }
  }

}
