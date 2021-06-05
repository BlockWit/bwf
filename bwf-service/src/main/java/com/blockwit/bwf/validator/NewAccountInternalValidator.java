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

package com.blockwit.bwf.validator;

import com.blockwit.bwf.form.NewAccountInternal;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class NewAccountInternalValidator extends CommonValidator {

  public NewAccountInternalValidator(javax.validation.Validator javaxValidator) {
    super(javaxValidator, NewAccountInternal.class);
  }

  @Override
  public void performValidate(Object o, Errors errors) {
    NewAccountInternal newAccountInternal = (NewAccountInternal) o;

    if (!newAccountInternal.getPassword().equals(newAccountInternal.getRepassword()))
      errors.rejectValue("repassword", "Passwords not equals", "Passwords not equals");
  }

}
