/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.controller;

import com.blockwit.bwf.exception.AttemptTimelimitAccountServiceException;
import com.blockwit.bwf.exception.NotFoundAccountServiceException;
import com.blockwit.bwf.exception.WrongConfirmStatusAccountServiceException;
import com.blockwit.bwf.form.ForgotPassword;
import com.blockwit.bwf.form.SetAccountPassword;
import com.blockwit.bwf.model.ConfirmationStatus;
import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.service.AccountService;
import com.blockwit.bwf.service.EmailService;
import com.blockwit.bwf.validator.ForgotpasswordValidator;
import com.blockwit.bwf.validator.NewAccountPasswordValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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

@Slf4j
@Controller
public class SecurityController {

  @Autowired
  private AccountService accountService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private NewAccountPasswordValidator newAccountPasswordValidator;

  @Autowired
  private ForgotpasswordValidator forgotpasswordValidator;

  @GetMapping("/app/login")
  public String login() {
    return "front/pages/login";
  }

  @PostMapping("/app/forgotpassword")
  public ModelAndView forgotPasswordPost(
      @ModelAttribute("forgotPassword")
      @Valid ForgotPassword forgotPassword,
      BindingResult bindingResult
  ) {
    log.info("Prepare to forgotpassword");

    forgotpasswordValidator.validate(forgotPassword, bindingResult);
    if (bindingResult.hasErrors())
      return new ModelAndView("front/pages/forgotpassword", bindingResult.getModel(), HttpStatus.BAD_REQUEST);

    Optional<Account> accountOpt;
    try {
      accountOpt = accountService._generatePasswordRecoveryCode(forgotPassword.getLogin());
    } catch (AttemptTimelimitAccountServiceException e) {
      bindingResult.rejectValue("login", "model.forgotpassword.timelimit.error");
      //bindingResult.rejectValue("login", "Attempts time limit not exceeded " + e.getLimit() + " millisecond");
      return new ModelAndView("front/pages/forgotpassword", bindingResult.getModel(), HttpStatus.BAD_REQUEST);
    }

    accountOpt.stream().forEach(account -> {
      emailService.sendPasswordRecoveryToken(account.getEmail(), account.getLogin(), account.getPasswordRecoveryCode());
      accountService._recoveryTokenSended(account);
    });

    return new ModelAndView("front/pages/forgotpassword-success");
  }

  @GetMapping("/app/forgotpassword")
  public ModelAndView forgotPassword() {
    return new ModelAndView("front/pages/forgotpassword", Map.of("forgotPassword", new ForgotPassword()));
  }

  @GetMapping("/app/forgotpassword/setpassword/{login}/{code}")
  public ModelAndView forgotPasswordSetNewPassword(
      @PathVariable("login") String login,
      @PathVariable("code") String code
  ) {

    if (!Pattern.matches(Constants.REGEXP_LOGIN, login))
      return new ModelAndView("error/custom-error",
          Map.of("message", "Malformed login " + login),
          HttpStatus.BAD_REQUEST);

    if (!Pattern.matches(Constants.REGEXP_CONFIRM_CODE, code))
      return new ModelAndView("error/custom-error",
          Map.of("message", "Malformed login " + code),
          HttpStatus.BAD_REQUEST);

    Optional<Account> accountOpt = accountService.findByLoginAndAndPasswordRecoveryCode(login.toLowerCase(), code.toLowerCase());
    if (accountOpt.isEmpty())
      return new ModelAndView("custom-error",
          Map.of("message", "Account with login " + login + " and code " + code + " not found"),
          HttpStatus.BAD_REQUEST);

    Account account = accountOpt.get();
    if (account.getConfirmationStatus() != ConfirmationStatus.CONFIRMED)
      return new ModelAndView("custom-error",
          Map.of("message", "Account with login " + login + " and code " + code + " has wrong status"),
          HttpStatus.BAD_REQUEST);

    SetAccountPassword newAccountPassword = new SetAccountPassword();
    newAccountPassword.setLogin(login);
    newAccountPassword.setCode(code);

    return new ModelAndView("front/pages/forgotpassword-setpassword", Map.of("forgotPassword", newAccountPassword));
  }

  @PostMapping("/app/forgotpassword/setpassword")
  public ModelAndView forgotPasswordSetNewPasswordPost(
      @ModelAttribute("newAccountPassword")
      @Valid SetAccountPassword setAccountPassword,
      BindingResult bindingResult
  ) {
    log.info("Perform set forgotten password account checks");

    newAccountPasswordValidator.validate(setAccountPassword, bindingResult);
    if (bindingResult.hasErrors())
      return new ModelAndView("front/pages/forgotpassword-setpassword", bindingResult.getModel(), HttpStatus.BAD_REQUEST);

    Account account;
    try {
      account = accountService._setAccountForgottenNewPassword(setAccountPassword.getLogin().toLowerCase(), setAccountPassword.getPassword());
    } catch (NotFoundAccountServiceException e) {
      return new ModelAndView("error/custom-error",
          Map.of("message", "Account with login " + setAccountPassword.getLogin() + " not found"),
          HttpStatus.BAD_REQUEST);
    } catch (WrongConfirmStatusAccountServiceException e) {
      return new ModelAndView("error/custom-error",
          Map.of("message", "Account with login " + setAccountPassword.getLogin() + " have wrong confirmation status"),
          HttpStatus.BAD_REQUEST);
    }

    return new ModelAndView("front/pages/forgotpassword-setpassword-success", Map.of("account", account));
  }

}
