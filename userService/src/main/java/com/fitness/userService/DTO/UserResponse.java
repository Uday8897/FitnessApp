package com.fitness.userService.DTO;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private String id;
    private String emailId;
    private String passWord;
    private String keyCloakId;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;
}
