/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.controller.web;

import com.blockwit.bwf.controller.ControllerHelper;
import com.blockwit.bwf.controller.OptionalExecutor;
import com.blockwit.bwf.form.*;
import com.blockwit.bwf.model.Permission;
import com.blockwit.bwf.model.Role;
import com.blockwit.bwf.model.account.Account;
import com.blockwit.bwf.repository.AccountRepository;
import com.blockwit.bwf.service.AccountService;
import com.blockwit.bwf.service.PermissionService;
import com.blockwit.bwf.service.RoleService;
import com.blockwit.bwf.service.utils.WithOptional;
import com.blockwit.bwf.validator.*;
import com.blockwit.utils.OptionalUtils;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/panel/accounts")
public class AccountController {

	public static final String TP_ADD_ROLE_TO_ACCOUNT = "panel/pages/security/add-role-to-account";

	public static final String TP_ADD_PERMISSION_TO_ACCOUNT = "panel/pages/security/add-permission-to-account";

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;

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

	@Autowired
	private AddRoleToAccountValidator addRoleToAccountValidator;

	@Autowired
	private AddPermissionToAccountValidator addPermissionToAccountValidator;

	@GetMapping
	public ModelAndView appPanelAccounts() {
		return new ModelAndView("redirect:/panel/accounts/page/1");
	}

	@GetMapping("/page/{pageNumber}")
	public ModelAndView appPanelAccountsPage(@PathVariable("pageNumber") int pageNumber) {
		return ControllerHelper.addPageableResult(new ModelAndView("panel/pages/accounts"), pageNumber, accountRepository);
	}

	@GetMapping("/account/{accountId}/rolesandpermissions")
	public ModelAndView accountRolesAndPermissions(HttpServletRequest request,
												   RedirectAttributes redirectAttributes,
												   @PathVariable("accountId") long accountId) {
		return new OptionalExecutor<Account>().perform("Account",
			accountId,
			accountRepository,
			request,
			redirectAttributes,
			account -> new ModelAndView("panel/pages/security/account-roles-and-permissions", Map.of("targetAccount", account)));
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

	@GetMapping("/account/{accountId}/roles/add")
	public ModelAndView addRoleToAccountGET(HttpServletRequest request,
											RedirectAttributes redirectAttributes,
											@PathVariable long accountId) {
		return OptionalUtils.process(accountService.findById(accountId),
			() -> ControllerHelper.returnError400(request, redirectAttributes, "Account with id " + accountId + " not found!")
			, account -> {
				Set<Long> existsRolesIds = account.getRoles().stream().map(t -> t.getId()).collect(Collectors.toSet());
				return new ModelAndView(TP_ADD_ROLE_TO_ACCOUNT,
					Map.of("account", account,
						"addRoleToAccount", new AddRoleToAccount(null),
						"roles", roleService.findAll()
							.stream()
							.filter(t -> !existsRolesIds.contains(t.getId()))
							.collect(Collectors.toList())));
			}
		);
	}

	@PostMapping("/account/{accountId}/roles/add")
	public ModelAndView addRoleToAccountPOST(
		HttpServletRequest request,
		RedirectAttributes redirectAttributes,
		@PathVariable long accountId,
		@ModelAttribute("addRoleToAccount") @Valid AddRoleToAccount addRoleToAccount,
		BindingResult bindingResult
	) {
		return OptionalUtils.process(accountService.findById(accountId),
			() -> ControllerHelper.returnError400(request, redirectAttributes, "Account with id " + accountId + " not found!")
			, account -> {

				addRoleToAccountValidator.validate(addRoleToAccount, bindingResult);

				if (bindingResult.hasErrors()) {
					Set<Long> existsRolesIds = account.getRoles().stream().map(t -> t.getId()).collect(Collectors.toSet());
					return new ModelAndView(TP_ADD_ROLE_TO_ACCOUNT, bindingResult.getModel(), HttpStatus.BAD_REQUEST)
						.addObject(Map.of("addRoleToAccount", new AddRoleToAccount(null),
							"account", account,
							"roles", roleService.findAll()
								.stream()
								.filter(t -> !existsRolesIds.contains(t.getId()))
								.collect(Collectors.toList())));
				}

				return OptionalUtils.process(roleService.findByName(addRoleToAccount.getRoleName()),
					() -> ControllerHelper.returnError400(request, redirectAttributes, "Role with name " + addRoleToAccount.getRoleName() + " not found!")
					, role -> {
						Set<Role> newRoles = new HashSet<>();
						newRoles.addAll(account.getRoles());
						newRoles.add(role);
						accountService.save(account.toBuilder().roles(newRoles).build());
						return ControllerHelper.returnSuccess(redirectAttributes, "redirect:/panel/accounts/account/" + accountId + "/rolesandpermissions",
							"Role " + role.getName() + " has been added to account " + account.getLogin());
					}
				);
			}
		);
	}

	@GetMapping("/account/{accountId}/permissions/add")
	public ModelAndView addPermissionToAccountGET(HttpServletRequest request,
												  RedirectAttributes redirectAttributes,
												  @PathVariable long accountId) {
		return OptionalUtils.process(accountService.findById(accountId),
			() -> ControllerHelper.returnError400(request, redirectAttributes, "Account with id " + accountId + " not found!")
			, account -> {
				Set<Long> existsPermissionsIds = account.getPermissions().stream().map(t -> t.getId()).collect(Collectors.toSet());
				return new ModelAndView(TP_ADD_PERMISSION_TO_ACCOUNT,
					Map.of("account", account,
						"addPermissionToAccount", new AddPermissionToAccount(null),
						"permissions", permissionService.findAll()
							.stream()
							.filter(t -> !existsPermissionsIds.contains(t.getId()))
							.collect(Collectors.toList())));
			}
		);
	}

	@PostMapping("/account/{accountId}/permissions/add")
	public ModelAndView addPermissionToAccountPOST(
		HttpServletRequest request,
		RedirectAttributes redirectAttributes,
		@PathVariable long accountId,
		@ModelAttribute("addPermissionToAccount") @Valid AddPermissionToRole addPermissionToAccount,
		BindingResult bindingResult
	) {
		return OptionalUtils.process(accountService.findById(accountId),
			() -> ControllerHelper.returnError400(request, redirectAttributes, "Account with id " + accountId + " not found!")
			, account -> {

				addPermissionToAccountValidator.validate(addPermissionToAccount, bindingResult);

				if (bindingResult.hasErrors()) {
					Set<Long> existsPermissionsIds = account.getPermissions().stream().map(t -> t.getId()).collect(Collectors.toSet());
					return new ModelAndView(TP_ADD_PERMISSION_TO_ACCOUNT, bindingResult.getModel(), HttpStatus.BAD_REQUEST)
						.addObject(Map.of("addPermissionToAccount", new AddPermissionToAccount(null),
							"account", account,
							"roles", roleService.findAll()
								.stream()
								.filter(t -> !existsPermissionsIds.contains(t.getId()))
								.collect(Collectors.toList())));
				}

				return OptionalUtils.process(permissionService.findByName(addPermissionToAccount.getPermissionName()),
					() -> ControllerHelper.returnError400(request, redirectAttributes, "Permission with name " + addPermissionToAccount.getPermissionName() + " not found!")
					, permission -> {
						Set<Permission> newPermission = new HashSet<>();
						newPermission.addAll(account.getPermissions());
						newPermission.add(permission);
						accountService.save(account.toBuilder().permissions(newPermission).build());
						return ControllerHelper.returnSuccess(redirectAttributes, "redirect:/panel/accounts/account/" + accountId + "/rolesandpermissions",
							"Permission " + permission.getName() + " has been added to account " + account.getLogin());
					}
				);
			}
		);
	}


	@GetMapping("/account/{accountId}/roles/role/{roleId}/remove")
	public ModelAndView deleteRoleFromAccount(
		HttpServletRequest request,
		RedirectAttributes redirectAttributes,
		@PathVariable long accountId,
		@PathVariable long roleId
	) {
		return OptionalUtils.process(accountService.findById(accountId),
			() -> ControllerHelper.returnError400(request, redirectAttributes, "Account with id " + accountId + " not found!")
			, account -> {
				if (account.getRoles().stream()
					.map(t -> t.getId())
					.filter(t -> t.equals(roleId))
					.findFirst().isEmpty()) {
					ControllerHelper.returnError400(request, redirectAttributes,
						"Account " + account.getLogin() + " does not contain role with id " + roleId + " !");
				}
				Set<Role> newRoles = account.getRoles().stream()
					.filter(t -> !t.getId().equals(roleId))
					.collect(Collectors.toSet());
				accountService.save(account.toBuilder().roles(newRoles).build());
				return ControllerHelper.returnToReferer(
					request,
					redirectAttributes,
					"Role with id " + roleId + " has been removed from account " + account.getLogin());
			}
		);
	}

	@GetMapping("/account/{accountId}/permissions/permission/{permissionId}/remove")
	public ModelAndView deletePermissionFromAccount(
		HttpServletRequest request,
		RedirectAttributes redirectAttributes,
		@PathVariable long accountId,
		@PathVariable long permissionId
	) {
		return OptionalUtils.process(accountService.findById(accountId),
			() -> ControllerHelper.returnError400(request, redirectAttributes, "Account with id " + accountId + " not found!")
			, account -> {
				if (account.getPermissions().stream()
					.map(t -> t.getId())
					.filter(t -> t.equals(permissionId))
					.findFirst().isEmpty()) {
					ControllerHelper.returnError400(request, redirectAttributes,
						"Account " + account.getLogin() + " does not contain permission with id " + permissionId + " !");
				}
				Set<Permission> newPermissions = account.getPermissions().stream()
					.filter(t -> !t.getId().equals(permissionId))
					.collect(Collectors.toSet());
				accountService.save(account.toBuilder().permissions(newPermissions).build());
				return ControllerHelper.returnToReferer(
					request,
					redirectAttributes,
					"Permission with id " + permissionId + " has been removed from account " + account.getLogin());
			}
		);
	}


}
