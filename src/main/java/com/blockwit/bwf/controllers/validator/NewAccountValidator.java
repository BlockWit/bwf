package com.blockwit.bwf.controllers.validator;

import com.blockwit.bwf.controllers.model.NewAccount;
import com.blockwit.bwf.models.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class NewAccountValidator extends CommonValidator {

    private final AccountService accountService;

    public NewAccountValidator(javax.validation.Validator javaxValidator, AccountService accountService) {
        super(javaxValidator, NewAccount.class);
        this.accountService = accountService;
    }

    @Override
    public void performValidate(Object o, Errors errors) {
        NewAccount newAccount = (NewAccount) o;

        if (accountService.isExistsLogin(newAccount.getLogin()))
            errors.rejectValue("login", "Login busy");

        if (accountService.isExistsEmail(newAccount.getEmail()))
            errors.rejectValue("email", "Email busy");

    }

}
