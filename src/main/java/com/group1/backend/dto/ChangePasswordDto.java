package com.group1.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ChangePasswordDto {
    private String name;
    private String oldPassword;
    private String newPassword;
}
