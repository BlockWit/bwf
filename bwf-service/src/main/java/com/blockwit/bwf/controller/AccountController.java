package com.blockwit.bwf.controller;

import com.blockwit.bwf.form.EditAccountInternal;
import com.blockwit.bwf.form.NewAccountInternal;
import com.blockwit.bwf.form.UpdateAccountPasswordInternal;
import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.repository.AccountRepository;
import com.blockwit.bwf.service.AccountService;
import com.blockwit.bwf.service.utils.WithOptional;
import com.blockwit.bwf.validator.EditAccountInternalValidator;
import com.blockwit.bwf.validator.NewAccountInternalValidator;
import com.blockwit.bwf.validator.UpdateAccountPasswordValidatorInternal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/panel/accounts")
public class AccountController {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountService accountService;

  @Autowired
  private EditAccountInternalValidator editAccountInternalValidator;

  @Autowired
  private UpdateAccountPasswordValidatorInternal updateAccountPasswordValidatorInternal;

  @Autowired
  private NewAccountInternalValidator newAccountInternalValidator;

  @GetMapping
  public ModelAndView appPanelAccounts() {
    return new ModelAndView("redirect:/panel/accounts/page/1");
  }

  @GetMapping("/page/{pageNumber}")
  public ModelAndView appPanelAccountsPage(@PathVariable("pageNumber") int pageNumber) {
    return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/accounts"), pageNumber, accountRepository);
  }

  @GetMapping("/account/{accountId}/roles")
  public ModelAndView accountRoles(HttpServletRequest request,
                                   RedirectAttributes redirectAttributes,
                                   @PathVariable("accountId") long accountId) {
    return new OptionalExecutor<Account>().perform("Account",
        accountId,
        accountRepository,
        request,
        redirectAttributes,
        account -> new ModelAndView("panel/pages/accountRoles", Map.of("targetAccount", account)));
  }

  @GetMapping("/account/{accountId}/permissions")
  public ModelAndView accountPermissions(HttpServletRequest request,
                                         RedirectAttributes redirectAttributes,
                                         @PathVariable("accountId") long accountId) {
    return new OptionalExecutor<Account>().perform("Account",
        accountId,
        accountRepository,
        request,
        redirectAttributes,
        account -> new ModelAndView("panel/pages/accountPermissions", Map.of("targetAccount", account)));
  }

  @GetMapping(value = "/account/{accountId}/delete")
  public ModelAndView removeTaskById(HttpServletRequest request,
                                     RedirectAttributes redirectAttributes,
                                     @PathVariable long accountId) {
    return WithOptional.process(accountService.deleteById(accountId),
        () -> ControllerHelper.returnError400(request, redirectAttributes, "Can't delete account with id " + accountId + "! May be not found?")
        , account -> ControllerHelper.returnToReferer(request, redirectAttributes, "Account with id " + accountId + " successfully deleted!")
    );
  }

  @GetMapping("/create")
  public ModelAndView createAccountGET() {
    return new ModelAndView("panel/pages/account-new-internal", Map.of("newAccountInternal", new NewAccountInternal()));
  }

  @PostMapping("/create")
  public ModelAndView createAccountPOST(
      HttpServletRequest request,
      RedirectAttributes redirectAttributes,
      @ModelAttribute("newAccountInternal") @Valid NewAccountInternal newAccount,
      BindingResult bindingResult
  ) {
    log.info("Perform new account form checks");

    newAccountInternalValidator.validate(newAccount, bindingResult);
    if (bindingResult.hasErrors())
      return new ModelAndView("panel/pages/account-new-internal", bindingResult.getModel(), HttpStatus.BAD_REQUEST);

    log.info("Create account");
    return accountService.createAccountInternal(newAccount.getLogin(), newAccount.getEmail(), newAccount.getPassword()).fold(
        error -> ControllerHelper.returnError400(bindingResult, "panel/pages/account-new-internal", error.getDescr())
        , account -> ControllerHelper.returnSuccess(redirectAttributes, "redirect:/panel/accounts/page/1", "Account " + account.getLogin() + " successfully created!")
    );
  }

  @PostMapping("/account/{accountId}/edit")
  public ModelAndView editAccountPOST(
      HttpServletRequest request,
      RedirectAttributes redirectAttributes,
      @PathVariable long accountId,
      @ModelAttribute("editAccountInternal") @Valid EditAccountInternal editAccount,
      BindingResult bindingResult
  ) {
    log.info("Perform edit account form checks");

    editAccountInternalValidator.validate(editAccount, bindingResult);
    if (bindingResult.hasErrors())
      return new ModelAndView("panel/pages/account-edit-internal", bindingResult.getModel(), HttpStatus.BAD_REQUEST);


    if (accountId != editAccount.getId().longValue())
      return ControllerHelper.returnError400(bindingResult,
          "panel/pages/account-edit-internal", "Sanity checks not passed - id from url and from form must be equals");

    log.info("Update account");
    return accountService.updateAccountInternal(editAccount.getId(), editAccount.getLogin(), editAccount.getEmail()).fold(
        error -> ControllerHelper.returnError400(bindingResult, "panel/pages/account-edit-internal", error.getDescr())
        , account -> ControllerHelper.returnSuccess(redirectAttributes, "redirect:/panel/accounts/page/1", "Account " + account.getLogin() + " successfully updated!")
    );
  }

  @GetMapping(value = "/account/{accountId}/edit")
  public ModelAndView editAccountGET(HttpServletRequest request,
                                     RedirectAttributes redirectAttributes,
                                     @PathVariable long accountId) {
    return WithOptional.process(accountService.findById(accountId),
        () -> ControllerHelper.returnError400(request, redirectAttributes, "Can't edit account with id " + accountId + "! May be not found?")
        , account ->
            new ModelAndView("panel/pages/account-edit-internal", Map.of("editAccountInternal",
                EditAccountInternal.builder()
                    .id(account.getId())
                    .login(account.getLogin())
                    .email(account.getEmail())
                    .build()))
    );
  }

  @PostMapping("/account/{accountId}/setpass")
  public ModelAndView updatePassAccountPOST(
      HttpServletRequest request,
      RedirectAttributes redirectAttributes,
      @PathVariable long accountId,
      @ModelAttribute("editAccountInternal") @Valid UpdateAccountPasswordInternal editAccount,
      BindingResult bindingResult
  ) {
    log.info("Perform set password account form checks");

    updateAccountPasswordValidatorInternal.validate(editAccount, bindingResult);
    if (bindingResult.hasErrors())
      return new ModelAndView("panel/pages/account-setpass-internal", bindingResult.getModel(), HttpStatus.BAD_REQUEST);

    if (accountId != editAccount.getId().longValue())
      return ControllerHelper.returnError400(bindingResult,
          "panel/pages/account-setpass-internal", "Sanity checks not passed - id from url and from form must be equals");

    log.info("Update account");
    return accountService.updateAccountPasswordInternal(editAccount.getId(), editAccount.getPassword()).fold(
        error -> ControllerHelper.returnError400(bindingResult, "panel/pages/account-setpass-internal", error.getDescr())
        , account -> ControllerHelper.returnSuccess(redirectAttributes, "redirect:/panel/accounts/page/1", "Account " + account.getLogin() + " password successfully updated!")
    );
  }

  @GetMapping(value = "/account/{accountId}/setpass")
  public ModelAndView updatePassAccountGET(HttpServletRequest request,
                                           RedirectAttributes redirectAttributes,
                                           @PathVariable long accountId) {
    return WithOptional.process(accountService.findById(accountId),
        () -> ControllerHelper.returnError400(request, redirectAttributes, "Can't edit account with id " + accountId + "! May be not found?")
        , account ->
            new ModelAndView("panel/pages/account-setpass-internal", Map.of("editAccountInternal",
                UpdateAccountPasswordInternal.builder()
                    .id(account.getId())
                    .build()))
    );
  }


}
