package com.sing3demons.stock_backend.service;

import com.sing3demons.stock_backend.models.User;
import com.sing3demons.stock_backend.request.UserRequest;

public interface UserService {
    User register(UserRequest userRequest);
    User findUserByEmail(String email);
}
