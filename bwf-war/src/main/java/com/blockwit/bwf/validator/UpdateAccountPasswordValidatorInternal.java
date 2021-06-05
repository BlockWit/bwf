package com.blockwit.bwf.validator;

import com.blockwit.bwf.form.UpdateAccountPasswordInternal;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class UpdateAccountPasswordValidatorInternal extends CommonValidator {

  public UpdateAccountPasswordValidatorInternal(javax.validation.Validator javaxValidator) {
    super(javaxValidator, UpdateAccountPasswordInternal.class);
  }

  @Override
  public void performValidate(Object o, Errors errors) {
    UpdateAccountPasswordInternal updateAccountPasswordInternal = (UpdateAccountPasswordInternal) o;

    if (!updateAccountPasswordInternal.getPassword().equals(updateAccountPasswordInternal.getRepassword()))
      errors.rejectValue("repassword", "Passwords not equals", "Passwords not equals");
  }

}
