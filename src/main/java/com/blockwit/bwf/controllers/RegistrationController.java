package com.blockwit.bwf.controllers;

import com.blockwit.bwf.controllers.model.Login;
import com.blockwit.bwf.controllers.model.NewAccount;
import com.blockwit.bwf.controllers.model.NewAccountPassword;
import com.blockwit.bwf.models.entity.Account;
import com.blockwit.bwf.models.entity.ConfirmationStatus;
import com.blockwit.bwf.models.repository.AccountRepository;
import com.blockwit.bwf.models.service.AccountService;
import com.blockwit.bwf.models.service.RoleService;
import com.blockwit.bwf.models.service.exceptions.EmailBusyAccountServiceException;
import com.blockwit.bwf.models.service.exceptions.LoginBusyAccountServiceException;
import com.blockwit.bwf.models.service.exceptions.NotFoundAccountServiceException;
import com.blockwit.bwf.models.service.exceptions.WrongConfirmStatusAccountServiceException;
import com.blockwit.bwf.services.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Log4j2
@Controller
public class RegistrationController {

    private final AccountRepository accountRepository;

    private final AccountService accountService;

    private final EmailService emailService;

    private final RoleService roleService;

    public RegistrationController(EmailService emailService,
                                  RoleService roleService,
                                  AccountService accountService,
                                  AccountRepository accountRepository) {
        this.emailService = emailService;
        this.roleService = roleService;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/app/registration/setpassword/{login}/{code}")
    public ModelAndView registrationSetPassword(@PathVariable("login") String login,
                                                @PathVariable("code") String code) {

        if (!Pattern.matches(Constants.REGEXP_LOGIN, login))
            return new ModelAndView("error/custom-error",
                    Map.of("message", "Malformed login " + login),
                    HttpStatus.BAD_REQUEST);

        if (!Pattern.matches(Constants.REGEXP_CONFIRM_CODE, code))
            return new ModelAndView("error/custom-error",
                    Map.of("message", "Malformed login " + code),
                    HttpStatus.BAD_REQUEST);

        Optional<Account> accountOpt = accountService.findByLoginAndConfirmCode(login.toLowerCase(), code.toLowerCase());
        if (accountOpt.isEmpty())
            return new ModelAndView("custom-error",
                    Map.of("message", "Account with login " + login + " and code " + code + " not found"),
                    HttpStatus.BAD_REQUEST);

        Account account = accountOpt.get();
        if (account.getConfirmationStatus() == ConfirmationStatus.CONFIRMED)
            return new ModelAndView("custom-error",
                    Map.of("message", "Account with login " + login + " and code " + code + " already confirmed"),
                    HttpStatus.BAD_REQUEST);

        NewAccountPassword newAccountPassword = new NewAccountPassword();
        newAccountPassword.setLogin(login);
        newAccountPassword.setCode(code);

        return new ModelAndView("front/reg-setpassword", Map.of("newAccountPassword", newAccountPassword));
    }

    @PostMapping("/app/registration/setpassword")
    public ModelAndView registrationSetPasswordPost(@ModelAttribute("newAccountPassword") @Valid NewAccountPassword newAccountPassword,
                                                    BindingResult bindingResult) {
        log.info("Perform set password account checks");

        if (bindingResult.hasErrors())
            return new ModelAndView("front/setpassword", bindingResult.getModel(), HttpStatus.BAD_REQUEST);

        Account account;
        try {
            account = accountService._setAccountConfirmedWithPassword(newAccountPassword.getLogin().toLowerCase(), newAccountPassword.getPassword());
        } catch (NotFoundAccountServiceException e) {
            return new ModelAndView("error/custom-error",
                    Map.of("message", "Account with login " + newAccountPassword.getLogin() + " not found"),
                    HttpStatus.BAD_REQUEST);
        } catch (WrongConfirmStatusAccountServiceException e) {
            return new ModelAndView("error/custom-error",
                    Map.of("message", "Account with login " + newAccountPassword.getLogin() + " have wrong confirmation status"),
                    HttpStatus.BAD_REQUEST);
        }

        return new ModelAndView("front/reg-setpassword-success", Map.of("account", account));
    }

    @GetMapping("/app/registration/new")
    public ModelAndView registration() {
        return new ModelAndView("front/reg-new", Map.of("newAccount", new NewAccount()));
    }

    @PostMapping("/app/registration/new")
    public ModelAndView registrationPost(@ModelAttribute("newAccount") @Valid NewAccount newAccount, BindingResult bindingResult) {
        log.info("Perform new account form checks");

        if (bindingResult.hasErrors())
            return new ModelAndView("front/reg-new", bindingResult.getModel(), HttpStatus.BAD_REQUEST);

        log.info("Create account");
        Account account;
        try {
            account = accountService._registerUnconfirmedAccount(newAccount.getLogin(), newAccount.getEmail());
        } catch (LoginBusyAccountServiceException e) {
            bindingResult.rejectValue("login", "model.newaccount.login.exists.error");
            return new ModelAndView("front/reg-new", bindingResult.getModel(), HttpStatus.BAD_REQUEST);
        } catch (EmailBusyAccountServiceException e) {
            bindingResult.rejectValue("email", "model.newaccount.email.exists.error");
            return new ModelAndView("front/reg-new", bindingResult.getModel(), HttpStatus.BAD_REQUEST);
        }

        emailService.sendVerificationToken(account.getEmail(), account.getLogin(), account.getConfirmCode());
        log.info("Verification token sends");

        accountService._setConfirmationStatusTokenSended(account);
        log.info("New account created");

        return new ModelAndView("front/reg-new-success");
    }

    @GetMapping("/app/login")
    public ModelAndView login(Model model) {
        return new ModelAndView("front/login", Map.of("login", new Login()));
    }

    @PostMapping("/app/login")
    public ModelAndView loginPost(@ModelAttribute("login") @Valid Login login, BindingResult bindingResult) {
        log.info("Perform login checks");

        if (bindingResult.hasErrors())
            return new ModelAndView("front/login", bindingResult.getModel(), HttpStatus.BAD_REQUEST);

        Account account;
        try {
            account = accountService._tryLogin(login.getLogin().trim().toLowerCase(), login.getPassword().trim());
        } catch (NotFoundAccountServiceException e) {
            return new ModelAndView("error/custom-error",
                    Map.of("message", "Wrong login or password"),
                    HttpStatus.UNAUTHORIZED);
        }

        return new ModelAndView("front/home", Map.of("account", account));
    }

}
