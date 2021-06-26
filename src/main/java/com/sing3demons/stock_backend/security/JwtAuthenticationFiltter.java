package com.sing3demons.stock_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sing3demons.stock_backend.request.UserRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


public class JwtAuthenticationFiltter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Value("${app.key.token}")
    private String JWT_SECRET;

    public JwtAuthenticationFiltter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(SecurityConstants.SIGN_in_URL, HttpMethod.POST.name()));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserRequest userRequest = new ObjectMapper().readValue(request.getInputStream(), UserRequest.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword(), new ArrayList<>()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if (authResult.getPrincipal() != null) {
            org.springframework.security.core.userdetails.User user = (User) authResult.getPrincipal();
            String username = user.getUsername();
            if (username != null && username.length() > 0) {
                Claims claims = Jwts.claims().setSubject(username);

                List<String> roles = new ArrayList<>();
                user.getAuthorities().forEach(authority -> roles.add(authority.getAuthority()));
                claims.put("role", roles);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                Map<String, Object> respJson = new HashMap<>();
                respJson.put("token", createToken(claims));

                OutputStream outputStream = response.getOutputStream();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, respJson);
                outputStream.flush();
            }
        }
    }

    private String createToken(Claims claims) {
        return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME)).signWith(SignatureAlgorithm.HS256, JWT_SECRET).compact();
    }
}
