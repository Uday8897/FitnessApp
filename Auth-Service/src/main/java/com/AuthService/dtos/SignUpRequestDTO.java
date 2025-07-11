package com.AuthService.dtos;

import lombok.Data;

@Data
public class SignUpRequestDTO {
    private String userName;
    private String passWord;
    private String email;
}
