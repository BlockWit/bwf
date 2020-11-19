package com.blockwit.bwf.controller;

import com.blockwit.bwf.entity.Account;
import com.blockwit.bwf.entity.ConfirmationStatus;
import com.blockwit.bwf.exception.*;
import com.blockwit.bwf.form.ForgotPassword;
import com.blockwit.bwf.form.NewAccount;
import com.blockwit.bwf.form.SetAccountPassword;
import com.blockwit.bwf.service.AccountService;
import com.blockwit.bwf.service.EmailService;
import com.blockwit.bwf.service.RoleService;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Controller
public class SecurityController {

	private final AccountService accountService;
	private final EmailService emailService;
	private final RoleService roleService;

	public SecurityController(
		EmailService emailService,
		RoleService roleService,
		AccountService accountService
	) {
		this.emailService = emailService;
		this.roleService = roleService;
		this.accountService = accountService;
	}

	@GetMapping("/app/registration/setpassword/{login}/{code}")
	public ModelAndView registrationSetPassword(
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

		SetAccountPassword newAccountPassword = new SetAccountPassword();
		newAccountPassword.setLogin(login);
		newAccountPassword.setCode(code);

		return new ModelAndView("front/reg-setpassword", Map.of("newAccountPassword", newAccountPassword));
	}

	@PostMapping("/app/registration/setpassword")
	public ModelAndView registrationSetPasswordPost(
		@ModelAttribute("newAccountPassword")
		@Valid SetAccountPassword setAccountPassword,
		BindingResult bindingResult
	) {
		log.info("Perform set password account checks");

		if (bindingResult.hasErrors())
			return new ModelAndView("front/setpassword", bindingResult.getModel(), HttpStatus.BAD_REQUEST);

		Account account;
		try {
			account = accountService._setAccountConfirmedWithPassword(setAccountPassword.getLogin().toLowerCase(), setAccountPassword.getPassword());
		} catch (NotFoundAccountServiceException e) {
			return new ModelAndView("error/custom-error",
				Map.of("message", "Account with login " + setAccountPassword.getLogin() + " not found"),
				HttpStatus.BAD_REQUEST);
		} catch (WrongConfirmStatusAccountServiceException e) {
			return new ModelAndView("error/custom-error",
				Map.of("message", "Account with login " + setAccountPassword.getLogin() + " have wrong confirmation status"),
				HttpStatus.BAD_REQUEST);
		}

		return new ModelAndView("front/reg-setpassword-success", Map.of("account", account));
	}

	@GetMapping("/app/registration/new")
	public ModelAndView registration() {
		return new ModelAndView("front/reg-new", Map.of("newAccount", new NewAccount()));
	}

	@PostMapping("/app/registration/new")
	public ModelAndView registrationPost(
		@ModelAttribute("newAccount")
		@Valid NewAccount newAccount,
		BindingResult bindingResult
	) {
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

	@PostMapping("/app/forgotpassword")
	public ModelAndView forgotPasswordPost(
		@ModelAttribute("forgotPassword")
		@Valid ForgotPassword forgotPassword,
		BindingResult bindingResult
	) {
		log.info("Prepare to forgotpassword");

		if (bindingResult.hasErrors())
			return new ModelAndView("front/forgotpassword", bindingResult.getModel(), HttpStatus.BAD_REQUEST);

		Optional<Account> accountOpt;
		try {
			accountOpt = accountService._generatePasswordRecoveryCode(forgotPassword.getLogin());
		} catch (AttemptTimelimitAccountServiceException e) {
			bindingResult.rejectValue("login", "model.forgotpassword.timelimit.error");
			//bindingResult.rejectValue("login", "Attempts time limit not exceeded " + e.getLimit() + " millisecond");
			return new ModelAndView("front/forgotpassword", bindingResult.getModel(), HttpStatus.BAD_REQUEST);
		}

		accountOpt.stream().forEach(account -> {
			emailService.sendPasswordRecoveryToken(account.getEmail(), account.getLogin(), account.getPasswordRecoveryCode());
			accountService._recoveryTokenSended(account);
		});

		return new ModelAndView("front/forgotpassword-success");
	}

	@GetMapping("/app/forgotpassword")
	public ModelAndView forgotPassword() {
		return new ModelAndView("front/forgotpassword", Map.of("forgotPassword", new ForgotPassword()));
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

		return new ModelAndView("front/forgotpassword-setpassword", Map.of("forgotPassword", newAccountPassword));
	}

	@PostMapping("/app/forgotpassword/setpassword")
	public ModelAndView forgotPasswordSetNewPasswordPost(
		@ModelAttribute("newAccountPassword")
		@Valid SetAccountPassword setAccountPassword,
		BindingResult bindingResult
	) {
		log.info("Perform set forgotten password account checks");

		if (bindingResult.hasErrors())
			return new ModelAndView("front/forgotpassword-setpassword", bindingResult.getModel(), HttpStatus.BAD_REQUEST);

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

		return new ModelAndView("front/forgotpassword-setpassword-success", Map.of("account", account));
	}

}
