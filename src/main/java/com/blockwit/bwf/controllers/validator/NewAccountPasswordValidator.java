package com.blockwit.bwf.controllers.validator;

import com.blockwit.bwf.controllers.model.SetAccountPassword;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class NewAccountPasswordValidator extends CommonValidator {

    public NewAccountPasswordValidator(javax.validation.Validator javaxValidator) {
        super(javaxValidator, SetAccountPassword.class);
    }

    @Override
    public void performValidate(Object o, Errors errors) {
        SetAccountPassword newAccountPassword = (SetAccountPassword) o;

        if (!newAccountPassword.getPassword().equals(newAccountPassword.getRepassword()))
            errors.rejectValue("repassword",
                    "Passwords not equals");
    }

}
