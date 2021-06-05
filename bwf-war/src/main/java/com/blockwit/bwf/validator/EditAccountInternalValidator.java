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
