package com.blockwit.bwf.model.account;

import com.blockwit.bwf.model.User;
import org.springframework.security.core.Authentication;

import java.util.stream.Collectors;

public class AccountHelper {

	public static final AnonymousAccountInfoProvider anonymousAccountInfoProvider = new AnonymousAccountInfoProvider();

	public static final IAccountInfoProvider getAccountInfoProvider(Authentication authentication) {
		if (authentication == null)
			return anonymousAccountInfoProvider;

		Object principal = authentication.getPrincipal();
		if (principal instanceof User) {
			Account account = ((User) principal).getAccount();
			return getAccountInfoProvider(account);
		}
		return anonymousAccountInfoProvider;
	}

	public static final IAccountInfoProvider getAccountInfoProvider(Account account) {
		return SimpleAccountInfoProvider.builder()
			.id(account.getId())
			.username(account.getLogin())
			.allPermissions(account.getPermissions().stream().map(t -> t.getName()).collect(Collectors.toSet()))
			.roles(account.getRoles().stream().map(t -> t.getName()).collect(Collectors.toSet()))
			.email(account.getEmail())
			.build();
	}

}
