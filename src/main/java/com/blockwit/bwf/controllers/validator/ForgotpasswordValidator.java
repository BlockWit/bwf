package com.blockwit.bwf.controllers.validator;

import com.blockwit.bwf.controllers.model.Forgotpassword;
import org.springframework.stereotype.Service;

@Service
public class ForgotpasswordValidator extends CommonValidator {

    public ForgotpasswordValidator(javax.validation.Validator javaxValidator) {
        super(javaxValidator, Forgotpassword.class);
    }

}
