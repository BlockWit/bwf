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

package com.blockwit.bwf.form;

import com.blockwit.bwf.controller.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewAccountInternal {

  @NotNull
  @Size(min = 3, max = 50)
  @Pattern(regexp = Constants.REGEXP_LOGIN, message = "{model.newaccount.login.regexp.error}")
  private String login;

  @NotNull
  @Email(message = "{model.newaccount.email.regexp.error}")
  private String email;

  @NotNull
  @Pattern(regexp = Constants.REGEXP_PASSWORD, message = "{model.newaccount.pwd.regexp.error}")
  private String password;

  @NotNull
  @Pattern(regexp = Constants.REGEXP_PASSWORD, message = "{model.newaccount.repwd.regexp.error}")
  private String repassword;

}
