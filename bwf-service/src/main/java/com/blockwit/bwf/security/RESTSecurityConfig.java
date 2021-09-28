package com.blockwit.bwf.security;

import com.blockwit.bwf.controller.rest.RestUrls;
import com.blockwit.bwf.security.jwt.config.SecretProvider;
import com.blockwit.bwf.security.jwt.filter.JwtTokenAuthenticationFilter;
import com.blockwit.bwf.security.jwt.filter.RestAccessDeniedHandler;
import com.blockwit.bwf.security.jwt.filter.SecurityAuthenticationEntryPoint;
import com.blockwit.bwf.service.PermissionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Order(2)
@Configuration
@EnableWebSecurity
public class RESTSecurityConfig extends WebSecurityConfigurerAdapter {


	private final UserDetailsService appUserDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final SecretProvider secretProvider;

	public RESTSecurityConfig(UserDetailsService appUserDetailsService,
							  PasswordEncoder passwordEncoder,
							  SecretProvider secretProvider) {
		this.appUserDetailsService = appUserDetailsService;
		this.passwordEncoder = passwordEncoder;
		this.secretProvider = secretProvider;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
        /*
          Overloaded to expose Authenticationmanager's bean created by configure(AuthenticationManagerBuilder).
           This bean is used by the AuthenticationController.
         */
		return super.authenticationManagerBean();
	}

	private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter(String path, String secret) {
		return new JwtTokenAuthenticationFilter(path, secret, appUserDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println(RestUrls.REST_URL_API_V_1_FRONT_POSTS_PATTERN);
		http
			.requestMatchers().antMatchers(RestUrls.REST_URL_API_V_1_PATTERN)
			.and()
			.csrf().disable()
			.httpBasic().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.antMatcher(RestUrls.REST_URL_API_V_1_PATTERN)
			.addFilterAfter(jwtTokenAuthenticationFilter(RestUrls.REST_URL_API_V_1_PATTERN, secretProvider.getSecret()), ExceptionTranslationFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
			.accessDeniedHandler(new RestAccessDeniedHandler())
			.and()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, RestUrls.REST_URL_API_V_1_AUTH).permitAll()
			.antMatchers(HttpMethod.GET,
				RestUrls.REST_URL_API_V_1_ADMIN_OPTIONS,
				RestUrls.REST_URL_API_V_1_ADMIN_ACCOUNTS,
				RestUrls.REST_URL_API_V_1_ADMIN_ROLES,
				RestUrls.REST_URL_API_V_1_ADMIN_PERMISSIONS,
				RestUrls.REST_URL_API_V_1_ADMIN_POSTS,
				RestUrls.REST_URL_API_V_1_ADMIN_MEDIA,
				RestUrls.REST_URL_API_V_1_ADMIN_MENU,
				RestUrls.REST_URL_API_V_1_ADMIN_NOTIFICATIONS,
				RestUrls.REST_URL_API_V_1_ADMIN_NOTIFICATIONS_EXEC_STATES,
				RestUrls.REST_URL_API_V_1_ADMIN_NOTIFICATIONS_NOTIFY_TYPES,
				RestUrls.REST_URL_API_V_1_ADMIN_NOTIFICATIONS_EXECUTORS,
				RestUrls.REST_URL_API_V_1_ADMIN_NOTIFICATIONS_ASSIGNS
			).hasAuthority(PermissionService.PERMISSION_ADMIN)
			.antMatchers(HttpMethod.GET,
				RestUrls.REST_URL_API_V_1_FRONT_CONFIG,
				RestUrls.REST_URL_API_V_1_FRONT_MENU_PATTERN,
				RestUrls.REST_URL_API_V_1_FRONT_POSTS_PATTERN)
			.permitAll()
			.anyRequest().authenticated();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(appUserDetailsService)
			.passwordEncoder(passwordEncoder);
	}

}


