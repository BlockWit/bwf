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
import lombok.*;

import javax.persistence.Id;
import javax.validation.Constraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateAccountPasswordInternal {

  @Id
  @NotNull
  @Min(0)
  @Max(Long.MAX_VALUE)
  private Long id;

  @NotNull
  @Pattern(regexp = Constants.REGEXP_PASSWORD, message = "{model.newaccount.pwd.regexp.error}")
  private String password;

  @NotNull
  @Pattern(regexp = Constants.REGEXP_PASSWORD, message = "{model.newaccount.repwd.regexp.error}")
  private String repassword;

}
