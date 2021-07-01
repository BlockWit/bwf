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

package com.blockwit.bwf.validator;

import com.blockwit.bwf.form.EditPost;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class EditPostValidator extends CommonValidator {

  public EditPostValidator(javax.validation.Validator javaxValidator) {
    super(javaxValidator, EditPost.class);
  }

  @Override
  public void performValidate(Object o, Errors errors) {
    EditPost editPost = (EditPost) o;
  }

}
