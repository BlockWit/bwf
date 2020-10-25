package com.blockwit.bwf.controllers.validator;

import com.blockwit.bwf.controllers.model.Login;
import org.springframework.stereotype.Service;

@Service
public class LoginValidator extends CommonValidator {

    public LoginValidator(javax.validation.Validator javaxValidator) {
        super(javaxValidator, Login.class);
    }

}
