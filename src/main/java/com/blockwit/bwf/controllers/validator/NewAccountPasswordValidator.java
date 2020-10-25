package com.blockwit.bwf.controllers.validator;

import com.blockwit.bwf.controllers.model.NewAccountPassword;
import com.blockwit.bwf.models.entity.Account;
import com.blockwit.bwf.models.entity.ConfirmationStatus;
import com.blockwit.bwf.models.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
public class NewAccountPasswordValidator extends CommonValidator {

    private final AccountService accountService;

    public NewAccountPasswordValidator(javax.validation.Validator javaxValidator,
                                       AccountService accountService) {
        super(javaxValidator, NewAccountPassword.class);
        this.accountService = accountService;
    }

    @Override
    public void performValidate(Object o, Errors errors) {
        NewAccountPassword newAccountPassword = (NewAccountPassword) o;

//        Optional<Account> accountOpt = accountService.findByLoginAndConfirmCode(
//                newAccountPassword.getLogin().toLowerCase(),
//                newAccountPassword.getCode().toLowerCase());
//
//        if (accountOpt.isEmpty())
//            errors.rejectValue("login",
//                    "Account with login " + newAccountPassword.getLogin() + " and code " + newAccountPassword.getCode() + " not found");
//
//        Account account = accountOpt.get();
//        if (account.getConfirmationStatus() == ConfirmationStatus.CONFIRMED)
//            errors.rejectValue("login",
//                    "Account with login " + newAccountPassword.getLogin() + " and code " + newAccountPassword.getCode() + " already confirmed");

        if (!newAccountPassword.getPassword().equals(newAccountPassword.getRepassword()))
            errors.rejectValue("repassword",
                    "Passwords not equals");

    }

}
