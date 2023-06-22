package com.example.usermicroservice.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestLoginVo {

    @NotNull(message="Email cannot be null")
    @Size(min=2, message = "Eamil not be less than two characters")
    @Email
    private String email;

    @NotNull(message="Password cannot be null")
    @Size(min=8, message = "Password must be equal or grater than 8 characters")
    private String password;
}
