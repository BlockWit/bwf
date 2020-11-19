package com.blockwit.bwf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService appUserDetailsService;
	private final PasswordEncoder passwordEncoder;

	public WebSecurityConfig(UserDetailsService appUserDetailsService, PasswordEncoder passwordEncoder) {
		this.appUserDetailsService = appUserDetailsService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/",
				"/img/**",
				"/js/**",
				"/css/**",
				"/webjars/**").permitAll()
			.antMatchers("/app/login",
				"/app/registration/**",
				"/app/forgotpassword",
				"/app/forgotpassword/**").anonymous()
			.antMatchers("/panel/options/**",
				"/panel/roles/**",
				"/panel/accounts/**",
				"/panel/permissions/**").hasAuthority("ADMIN")
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/app/login")
			.defaultSuccessUrl("/")
			.and()
			.logout()
			.logoutUrl("/app/logout");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(appUserDetailsService)
			.passwordEncoder(passwordEncoder);
	}

}
