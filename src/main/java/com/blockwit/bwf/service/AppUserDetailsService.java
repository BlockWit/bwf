package com.blockwit.bwf.service;

import com.blockwit.bwf.model.Account;
import com.blockwit.bwf.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

	private final AccountService accountService;

	public AppUserDetailsService(AccountService accountService) {
		this.accountService = accountService;
	}
	@Override
	public UserDetails loadUserByUsername(String userNameOrLogin) throws UsernameNotFoundException {
		Account account = accountService
			.findByEmailOrLogin(userNameOrLogin)
			.orElseThrow(() -> new UsernameNotFoundException("Email or login " + userNameOrLogin + " not found"));
		System.out.println(account.getId());
		return new User(account);
	}

}