package com.blockwit.bwf.validator;

import com.blockwit.bwf.form.NewAccount;
import org.springframework.stereotype.Service;

@Service
public class NewAccountValidator extends CommonValidator {

    public NewAccountValidator(javax.validation.Validator javaxValidator) {
        super(javaxValidator, NewAccount.class);
    }

}
