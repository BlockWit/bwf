package com.blockwit.bwf.config;

import com.blockwit.bwf.service.PermissionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Order(2)
@Configuration
@EnableWebSecurity
public class RESTSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String REST_URL_API_V_1 = "/api/v1";

	public static final String REST_URL_API_V_1_PATTERN = REST_URL_API_V_1 + "/**";

	private final UserDetailsService appUserDetailsService;
	private final PasswordEncoder passwordEncoder;

	public RESTSecurityConfig(UserDetailsService appUserDetailsService, PasswordEncoder passwordEncoder) {
		this.appUserDetailsService = appUserDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.requestMatchers().antMatchers(REST_URL_API_V_1_PATTERN)
			.and()
			.csrf().disable()
			.authorizeRequests().antMatchers(REST_URL_API_V_1 + "/options").hasAuthority(PermissionService.PERMISSION_ADMIN)
			.and()
			.authorizeRequests().anyRequest().authenticated()
			.and().httpBasic()
			.and().sessionManagement().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(appUserDetailsService)
			.passwordEncoder(passwordEncoder);
	}

}


