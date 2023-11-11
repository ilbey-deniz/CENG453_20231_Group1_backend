package com.group1.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class ChangePasswordDto {
    private String name;
    private String oldPassword;
    private String newPassword;
}
