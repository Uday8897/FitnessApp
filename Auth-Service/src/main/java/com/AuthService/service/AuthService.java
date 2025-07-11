package com.AuthService.service;

import com.AuthService.dtos.LogInRequestDTO;
import com.AuthService.dtos.LogInResponse;
import com.AuthService.entity.User;
import com.AuthService.repository.UserRepository;
import com.AuthService.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialNotFoundException;

import com.AuthService.dtos.SignUpRequestDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    private static Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

    public LogInResponse authenticate(LogInRequestDTO logInRequestDTO) throws Exception {
        log.info("Request Received: {}", logInRequestDTO.getUserName());

        User user = userRepository.findByUserName(logInRequestDTO.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                logInRequestDTO.getUserName(),
                logInRequestDTO.getPassWord()
        );

        Authentication authenticate = authenticationManager.authenticate(authentication);

        if (authenticate.isAuthenticated()) {
            String token = jwtUtil.generateToken(user);
            return LogInResponse.builder().token(token).build();
        } else {
            throw new CredentialNotFoundException("Credential Not found");
        }
    }

    public String signUp(SignUpRequestDTO signUpRequestDTO) {
        if (userRepository.findByUserName(signUpRequestDTO.getUserName()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setUserName(signUpRequestDTO.getUserName());
        user.setPassWord(passwordEncoder.encode(signUpRequestDTO.getPassWord()));

        userRepository.save(user);

        return "User Registered Successfully";
    }

    public boolean isValid(String token) {
    return jwtUtil.verifyToken(token);
    }
}
