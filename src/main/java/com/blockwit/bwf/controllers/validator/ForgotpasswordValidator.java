package com.blockwit.bwf.controllers.validator;

import com.blockwit.bwf.controllers.model.ForgotPassword;
import org.springframework.stereotype.Service;

@Service
public class ForgotpasswordValidator extends CommonValidator {

    public ForgotpasswordValidator(javax.validation.Validator javaxValidator) {
        super(javaxValidator, ForgotPassword.class);
    }

}
