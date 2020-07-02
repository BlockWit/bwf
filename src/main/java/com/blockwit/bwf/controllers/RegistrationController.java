package com.blockwit.bwf.controllers;

import com.blockwit.bwf.models.Account;
import com.blockwit.bwf.models.ConfirmationStatus;
import com.blockwit.bwf.models.Role;
import com.blockwit.bwf.repo.AccountRepo;
import com.blockwit.bwf.services.EmailService;
import com.blockwit.bwf.services.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordService passwordService;

    @GetMapping("/app/register/reg-step-1")
    public String regStep1Get() {
        return "front/reg-step-1";
    }

    @PostMapping("/app/register/reg-step-1")
    public String regStep1Post(@RequestParam String login,
                               @RequestParam String email, Map<String, Object> model) {
        LOGGER.info("regStep1Post called");
        if (login == null) {
            model.put("message", "Login can't be empty");
            return "front/reg-step-1";
        }

        if (login.length() <= 5) {
            model.put("message", "Login length must be greater than 5 symbols");
            return "front/reg-step-1";
        }

        if (email == null) {
            model.put("message", "Email can't be empty");
            return "front/reg-step-1";
        }

        /* FIXME: Check email for pattern
        if(email.) {
            model.put("message", "Email can't be empty");
            return "/app/register/reg-step-1";
        }*/

        Account accountFromDB = accountRepo.findByLogin(login);
        if (accountFromDB != null) {
            model.put("message", "Login busy");
            return "front/reg-step-1";
        }

        LOGGER.info("regStep1Post: Sanity checks passed");

        Account account = new Account();
        account.setLogin(login);
        account.setEmail(email);
        account.setRoles(Collections.singleton(Role.USER));
        account.setConfirmationStatus(ConfirmationStatus.WAIT_CONFIRMATION);
        account.setConfirmCode(passwordService.generateRegistrationToken(login));

        LOGGER.info("regStep1Post: Send verification token");
        emailService.sendVerificationToken(email, login, account.getConfirmCode());

        accountRepo.save(account);
        LOGGER.info("regStep1Post: New account saved");

        return "front/reg-step-1-success";
    }

    @GetMapping("/app/login")
    public String login(Model model) {
        return "front/login";
    }

}
