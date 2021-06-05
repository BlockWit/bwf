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

import com.blockwit.bwf.form.EditAccountInternal;
import org.springframework.stereotype.Service;

import javax.validation.Constraint;

@Service
public class EditAccountInternalValidator extends CommonValidator {

  public EditAccountInternalValidator(javax.validation.Validator javaxValidator) {
    super(javaxValidator, EditAccountInternal.class);
  }

}
