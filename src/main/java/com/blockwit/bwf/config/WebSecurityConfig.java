package com.blockwit.bwf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMethod;

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
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/",
                        "/img/**",
                        "/js/**",
                        "/css/**",
                        "/webjars/**").permitAll()
                //.antMatchers(HttpMethod.GET,"/app/login").anonymous()
                //.antMatchers(HttpMethod.POST, "/app/perform_login").anonymous()
                .antMatchers(HttpMethod.POST, "/app/login").anonymous()
                .antMatchers("/app/registration/**").anonymous()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/app/login")
                //.loginProcessingUrl("/app/perform_login")
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutUrl("/app/logout");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

}
