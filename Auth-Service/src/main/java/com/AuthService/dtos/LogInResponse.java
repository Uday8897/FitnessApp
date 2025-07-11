package com.AuthService.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogInResponse {
    private String token;
}
