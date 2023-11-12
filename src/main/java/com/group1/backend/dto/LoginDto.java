package com.group1.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class LoginDto {
    private String name;
    private String password;
}

