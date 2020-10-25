package com.blockwit.bwf.controllers.validator;

import com.blockwit.bwf.controllers.model.NewAccount;
import org.springframework.stereotype.Service;

@Service
public class NewAccountValidator extends CommonValidator {

    public NewAccountValidator(javax.validation.Validator javaxValidator) {
        super(javaxValidator, NewAccount.class);
    }

}
