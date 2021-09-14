package com.blockwit.bwf.security;

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

	public static final String REST_URL_API_V_1 = "/api/v1";

	public static final String REST_URL_API_V_1_PATTERN = REST_URL_API_V_1 + "/**";

	public static final String REST_URL_REL_AUTH = "/auth";

	public static final String REST_URL_API_V_1_AUTH = RESTSecurityConfig.REST_URL_API_V_1 + REST_URL_REL_AUTH;

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
		return new JwtTokenAuthenticationFilter(path, secret);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.requestMatchers().antMatchers(REST_URL_API_V_1_PATTERN)
			.and()
			.csrf().disable()
			.httpBasic().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.antMatcher(REST_URL_API_V_1_PATTERN)
			.addFilterAfter(jwtTokenAuthenticationFilter(RESTSecurityConfig.REST_URL_API_V_1_PATTERN, secretProvider.getSecret()), ExceptionTranslationFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
			.accessDeniedHandler(new RestAccessDeniedHandler())
			.and()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, RESTSecurityConfig.REST_URL_API_V_1_AUTH).permitAll()
			.antMatchers(REST_URL_API_V_1 + "/options").hasAuthority(PermissionService.PERMISSION_ADMIN)
			.anyRequest().authenticated();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(appUserDetailsService)
			.passwordEncoder(passwordEncoder);
	}

}


