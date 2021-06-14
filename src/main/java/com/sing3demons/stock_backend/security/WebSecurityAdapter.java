package com.sing3demons.stock_backend.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class WebSecurityAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/**").permitAll().and().exceptionHandling().authenticationEntryPoint((req, res, err) -> {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }).and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
