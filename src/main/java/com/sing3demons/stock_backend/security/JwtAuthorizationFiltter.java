package com.sing3demons.stock_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthorizationFiltter extends BasicAuthenticationFilter {
    @Autowired
    private Environment env;
    String JWT_SECRET = "1A09106C-ED16-44CC-957A-07754B09A4A9";

    public JwtAuthorizationFiltter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            UsernamePasswordAuthenticationToken authentication = authenticationToken(authorizationHeader);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken authenticationToken(String s) {
//        System.out.println("JWT_TOKEN : " + env.getProperty("JWT_TOKEN"));
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(s.replace(SecurityConstants.TOKEN_PREFIX, "")).getBody();
        String username = claims.getSubject();
        if (username == null) {
            return null;
        }

        ArrayList<String> roles = (ArrayList<String>) claims.get("role");
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (roles != null) {
            for (String role : roles) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role));
            }
        }
        return new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
    }
}
