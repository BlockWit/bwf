package com.blockwit.bwf.controllers;

import com.blockwit.bwf.controllers.model.NewAccount;
import com.blockwit.bwf.models.entity.Account;
import com.blockwit.bwf.models.service.AccountService;
import com.blockwit.bwf.models.service.RoleService;
import com.blockwit.bwf.models.service.exceptions.DefaultAdminRoleNotExistsServiceException;
import com.blockwit.bwf.models.service.exceptions.DefaultUserRoleNotExistsServiceException;
import com.blockwit.bwf.models.service.exceptions.EmailBusyAccountServiceException;
import com.blockwit.bwf.models.service.exceptions.LoginBusyAccountServiceException;
import com.blockwit.bwf.services.EmailService;
import com.blockwit.bwf.services.PasswordService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;

@Log4j2
@Controller
public class RegistrationController {

    private final AccountService accountService;

    private final EmailService emailService;

    private final PasswordService passwordService;

    private final RoleService roleService;

    public RegistrationController(EmailService emailService,
                                  PasswordService passwordService,
                                  RoleService roleService,
                                  AccountService accountService) {
        this.emailService = emailService;
        this.passwordService = passwordService;
        this.roleService = roleService;
        this.accountService = accountService;
    }

    @GetMapping("/app/register/reg-step-2")
    public String regStep2Get() {
        //return "front/reg-step-2";
        throw new NotImplementedException("Coming soon");
    }

    @GetMapping("/app/register/reg-step-1")
    public ModelAndView regStep1Get() {
        return new ModelAndView("front/reg-step-1", Map.of("newAccount", new NewAccount()));
    }

    @PostMapping("/app/register/reg-step-1")
    public ModelAndView regStep1Post(@ModelAttribute("newAccount") @Valid NewAccount newAccount, BindingResult bindingResult) throws
            DefaultAdminRoleNotExistsServiceException,
            DefaultUserRoleNotExistsServiceException {
        log.info("Perform new account form checks");

        if (bindingResult.hasErrors()) {
            return new ModelAndView("front/reg-step-1", bindingResult.getModel(), HttpStatus.BAD_REQUEST);
        }

        log.info("Create account");
        Account account = null;
        try {
            accountService._registerUnconfirmedAccount(newAccount.getLogin(), newAccount.getEmail());
        } catch (LoginBusyAccountServiceException e) {
            bindingResult.rejectValue("login", "model.newaccount.login.exists.error");
            return new ModelAndView("front/reg-step-1", bindingResult.getModel(), HttpStatus.BAD_REQUEST);
        } catch (EmailBusyAccountServiceException e) {
            bindingResult.rejectValue("email", "model.newaccount.email.exists.error");
            return new ModelAndView("front/reg-step-1", bindingResult.getModel(), HttpStatus.BAD_REQUEST);
        }

        emailService.sendVerificationToken(account.getEmail(), account.getLogin(), account.getConfirmCode());
        log.info("Verification token sends");

        accountService._setConfirmationStatusTokenSended(account);
        log.info("New account created");

        return new ModelAndView("front/reg-step-1-success");
    }

    @GetMapping("/app/login")
    public String login(Model model) {
        return "front/login";
    }

}
