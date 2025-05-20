package com.fitness.userService.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Singular;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format ")
    private String email;
    private String password;
    private  String keyCloakId;
    @NotBlank(message = "Password is required") 
    @Size(min = 6,message = "Password should be minimum of length 6 ")
    private  String firstName;
    private String lastName;
}
