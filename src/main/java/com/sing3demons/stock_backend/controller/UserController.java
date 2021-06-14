package com.sing3demons.stock_backend.controller;

import com.sing3demons.stock_backend.exception.ValidationException;
import com.sing3demons.stock_backend.models.User;
import com.sing3demons.stock_backend.request.UserRequest;
import com.sing3demons.stock_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<Object> register(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(fieldError -> {
                throw new ValidationException(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            });
        }
        User user = userService.register(userRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
