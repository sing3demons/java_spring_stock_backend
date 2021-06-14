package com.sing3demons.stock_backend.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {
    @NotEmpty
    @Email
    private String Email;
    @NotEmpty
    private String password;
}
