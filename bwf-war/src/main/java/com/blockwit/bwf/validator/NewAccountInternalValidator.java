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
