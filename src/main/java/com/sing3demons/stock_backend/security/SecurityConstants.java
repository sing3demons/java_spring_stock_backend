package com.sing3demons.stock_backend.security;

import org.springframework.beans.factory.annotation.Value;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 1_296_000_000; // 15 day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SIGN_in_URL = "/api/v1/auth/login";
}
