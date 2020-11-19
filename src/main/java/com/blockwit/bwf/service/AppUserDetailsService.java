package com.blockwit.bwf.service;


import com.blockwit.bwf.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppUserDetailsService implements UserDetailsService {

	private final AccountService accountService;

	public AppUserDetailsService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public UserDetails loadUserByUsername(String userNameOrLogin) throws UsernameNotFoundException {
		Account account = accountService.findByEmailOrLogin(userNameOrLogin)
			.orElseThrow(() -> new UsernameNotFoundException("Email or login " + userNameOrLogin + " not found"));
		return new org.springframework.security.core.userdetails.User(account.getLogin(), account.getHash(),
			getAuthorities(account));
	}

	private static Collection<? extends GrantedAuthority> getAuthorities(Account account) {
		Set<String> accountPermissions = account.getPermissions().stream().map((p) -> p.getName()).collect(Collectors.toSet());
		account.getRoles().stream().map(r -> r.getPermissions()).flatMap(Collection::stream).map(t -> t.getName()).forEach(accountPermissions::add);
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(accountPermissions.stream().toArray(String[]::new));
		return authorities;
	}
}
