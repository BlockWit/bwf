package com.blockwit.bwf.controllers.validator;

import com.blockwit.bwf.controllers.model.NewAccount;
import com.blockwit.bwf.models.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Service
public class NewAccountValidator implements Validator {

    private final javax.validation.Validator javaxValidator;

    private final AccountService accountService;

    public NewAccountValidator(javax.validation.Validator javaxValidator, AccountService accountService) {
        this.javaxValidator = javaxValidator;
        this.accountService = accountService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return NewAccount.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        NewAccount newAccount = (NewAccount) o;

        Set<ConstraintViolation<Object>> validates = javaxValidator.validate(newAccount);

        for (ConstraintViolation<Object> constraintViolation : validates) {
            String propertyPath = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            errors.rejectValue(propertyPath, "", message);
        }

        if (accountService.isExistsLogin(newAccount.getLogin())) {
            errors.rejectValue("login", "Login busy");
        }

        if (accountService.isExistsEmail(newAccount.getEmail())) {
            errors.rejectValue("email", "Email busy");
        }


//        if (newAccount.getLogin() == null) {
//            errors.rejectValue("login", "Login can't be empty");
//        } else {
//            if (!Pattern.matches("(\\w|\\.){2,50}", newAccount.getLogin())) {
//                errors.rejectValue("login", "Login must contains from latin chars and digits, underscore and dots. Size from 8 to 50!");
//            }
//        }
//
//        if (newAccount.getEmail() == null) {
//            errors.rejectValue("login", "Login can't be empty");
//        } else {
//            if (!Pattern.matches("(\\w|\\.){2,50}", newAccount.getEmail())) {
//                errors.rejectValue("login", "Login must contains from latin chars and digits, underscore and dots. Size from 8 to 50!");
//            }
//        }
//
        // check login exists
        // check email exists
    }

}
