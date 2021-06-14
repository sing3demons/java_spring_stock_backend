package com.sing3demons.stock_backend.service;

import com.sing3demons.stock_backend.exception.UserException;
import com.sing3demons.stock_backend.models.User;
import com.sing3demons.stock_backend.repository.UserRepository;
import com.sing3demons.stock_backend.request.UserRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository _userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this._userRepository = userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
    }

    @Override
    public User register(UserRequest userRequest) {
        User result = _userRepository.findByEmail(userRequest.getEmail());
        if (result == null) {
            result = new User();
            result.setEmail(userRequest.getEmail());
            result.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
            result.setRole("Admin");
            return _userRepository.save(result);
        }
        throw new UserException(userRequest.getEmail());
    }
}
