package com.blockwit.bwf.controllers.validator;

import com.blockwit.bwf.controllers.model.Login;
import com.blockwit.bwf.controllers.model.NewAccount;
import com.blockwit.bwf.models.entity.Account;
import com.blockwit.bwf.models.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
public class LoginValidator extends CommonValidator {

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;

    public LoginValidator(javax.validation.Validator javaxValidator, AccountService accountService) {
        super(javaxValidator, Login.class);
        this.accountService = accountService;
        this.passwordEncoder =
    }

    @Override
    public void performValidate(Object o, Errors errors) {
        Login login = (Login) o;

        String preparedLogin = login.getLogin().trim().toLowerCase();

        Optional<Account> account = accountService.findByEmailOrLogin(preparedLogin);

        if (account.isEmpty())
            errors.rejectValue("login", "Wrong login or password");



    }

}
