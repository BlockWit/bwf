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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

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
			.cors().configurationSource(request -> {
			CorsConfiguration configuration = new CorsConfiguration();
			List<String> allowOrigins = Arrays.asList("*");
			configuration.setAllowedOrigins(allowOrigins);
			configuration.setAllowedMethods(Arrays.asList("*"));
			configuration.setAllowedHeaders(Arrays.asList("*"));
			//in case authentication is enabled this flag MUST be set, otherwise CORS requests will fail
			configuration.setAllowCredentials(true);
			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			source.registerCorsConfiguration("/**", configuration);
			return configuration;
		})
			.and()
			.authorizeRequests()
			.antMatchers(
				"/",
				"/css/**",
				"/img/**",
				"/js/**",
				"/posts/**",
				"/webjars/**",
				"/api/v1/**"
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
				"/panel/permissions/**"
			).hasAuthority("ADMIN")
			.antMatchers("/logout").authenticated()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/app/login")
			.defaultSuccessUrl("/")
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
