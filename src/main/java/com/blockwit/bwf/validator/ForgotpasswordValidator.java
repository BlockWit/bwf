package com.blockwit.bwf.validator;

import com.blockwit.bwf.form.ForgotPassword;
import org.springframework.stereotype.Service;

@Service
public class ForgotpasswordValidator extends CommonValidator {

	public ForgotpasswordValidator(javax.validation.Validator javaxValidator) {
		super(javaxValidator, ForgotPassword.class);
	}

}
