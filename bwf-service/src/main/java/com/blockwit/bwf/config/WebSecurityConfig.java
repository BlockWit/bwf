/**
 * Copyright (c) 2017-present BlockWit, LLC. or BlockWit Team All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.blockwit.bwf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
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
        .antMatchers(
            "/",
            "/css/**",
            "/img/**",
            "/js/**",
            "/posts/**",
            "/media/**",
            "/migration",
            "/migration2",
            "/webjars/**"
        ).permitAll()
        .antMatchers(
            "/app/login",
            "/app/registration/**",
            "/app/forgotpassword",
            "/app/forgotpassword/**"
        ).anonymous()
        .antMatchers(
            "/panel/options/**",
            "/panel/roles/**",
            "/panel/accounts/**",
            "/panel/permissions/**",
            "/panel/tasks/**"
        ).hasAuthority("ADMIN")
        .antMatchers("/logout").authenticated()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/app/login")
        .defaultSuccessUrl("/panel")
        .and()
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/app/logout", "GET"));
    //.logoutUrl("/logout"); TODO: logout should be POST with CSRF token
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(appUserDetailsService)
        .passwordEncoder(passwordEncoder);
  }

}
