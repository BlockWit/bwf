package com.blockwit.bwf.controllers.validator;

import com.blockwit.bwf.controllers.model.NewAccountPassword;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class NewAccountPasswordValidator extends CommonValidator {

    public NewAccountPasswordValidator(javax.validation.Validator javaxValidator) {
        super(javaxValidator, NewAccountPassword.class);
    }

    @Override
    public void performValidate(Object o, Errors errors) {
        NewAccountPassword newAccountPassword = (NewAccountPassword) o;

        if (!newAccountPassword.getPassword().equals(newAccountPassword.getRepassword()))
            errors.rejectValue("repassword",
                    "Passwords not equals");
    }

}
