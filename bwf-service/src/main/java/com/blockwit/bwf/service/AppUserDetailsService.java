package com.blockwit.bwf.service;

import com.blockwit.bwf.model.User;
import com.blockwit.bwf.model.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service("userDetailsService")
@Transactional
public class AppUserDetailsService implements UserDetailsService {

  @Autowired
  private AccountService accountService;

  @Autowired
  private LoginAttemptService loginAttemptService;

  @Autowired
  private HttpServletRequest request;

  @Override
  public UserDetails loadUserByUsername(String userNameOrLogin) throws UsernameNotFoundException {
    String ip = getClientIP();
    if (loginAttemptService.isBlocked(ip)) {
      throw new UsernameNotFoundException("Limit login attempts expired. Please try again later");
    }

    Account account = accountService
        .findByEmailOrLogin(userNameOrLogin)
        .orElseThrow(() -> new UsernameNotFoundException("Email or login " + userNameOrLogin + " not found"));
    return new User(account);
  }

  private String getClientIP() {
    String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader == null) {
      return request.getRemoteAddr();
    }
    return xfHeader.split(",")[0];
  }

}
