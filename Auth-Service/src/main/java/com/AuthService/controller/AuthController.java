package com.AuthService.controller;
import com.AuthService.dtos.LogInRequestDTO;
import com.AuthService.dtos.LogInResponse;
import com.AuthService.dtos.SignUpRequestDTO;
import com.AuthService.dtos.TokenRequest;
import com.AuthService.exceptions.UserNotValidException;
import com.AuthService.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LogInResponse> login(@RequestBody LogInRequestDTO logInRequestDTO) throws Exception {
        System.out.println(logInRequestDTO.toString());
        return ResponseEntity.ok(authService.authenticate(logInRequestDTO));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        String message = authService.signUp(signUpRequestDTO);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
    @GetMapping("/validate")
    public ResponseEntity<Boolean> isValid(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) throws Exception {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UserNotValidException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        if (authService.isValid(token)) {
            return ResponseEntity.ok(true);
        }

        throw new UserNotValidException("Token is not valid");
    }

}
