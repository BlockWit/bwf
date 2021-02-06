package com.blockwit.bwf.controller;

import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.repository.AccountRepository;
import com.blockwit.bwf.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/panel/accounts")
public class AccountController {

	private final AccountRepository accountRepository;

	private final AccountService accountService;

	public AccountController(AccountRepository accountRepository, AccountService accountService) {
		this.accountRepository = accountRepository;
		this.accountService = accountService;
	}

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

}
