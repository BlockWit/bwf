package com.blockwit.bwf.validator;

import com.blockwit.bwf.form.EditOption;
import org.springframework.stereotype.Service;

@Service
public class EditOptionValidator extends CommonValidator {

	public EditOptionValidator(javax.validation.Validator javaxValidator) {
		super(javaxValidator, EditOption.class);
	}

}
