package com.group1.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;




@AllArgsConstructor
@Getter
public class RegisterCredentialDto {
    private String name;
    private String email;
    private String password;
}
