package com.group1.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
@Getter
public class RegisterCredentialDto {
    private String name;
    private String email;
    private String password;
}
