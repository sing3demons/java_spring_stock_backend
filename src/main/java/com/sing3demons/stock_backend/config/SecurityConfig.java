package com.sing3demons.stock_backend.config;

import com.sing3demons.stock_backend.security.JwtAuthenticationFiltter;
import com.sing3demons.stock_backend.security.JwtAuthorizationFiltter;
import com.sing3demons.stock_backend.security.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailService userDetailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(UserDetailService userDetailService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailService = userDetailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/auth/register").permitAll()
                .antMatchers(HttpMethod.DELETE, "/products/*").hasAnyAuthority("Admin").anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint((req, res, err) -> {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        })
                .and().addFilter(authenticationFilter()).sessionManagement()
                .and().addFilter(new JwtAuthorizationFiltter(authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        final UsernamePasswordAuthenticationFilter filter = new JwtAuthenticationFiltter(authenticationManager());
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }
}
