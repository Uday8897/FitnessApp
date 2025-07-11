package com.AuthService.config;

import com.AuthService.entity.User;
import com.AuthService.repository.UserRepository;
import com.AuthService.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
@Autowired
    private  UserRepository userRepository;
    @Autowired

    private  JwtUtil jwtUtil;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");
            String provider = getProviderFromRequest(request);

            if (email == null) {
                throw new RuntimeException("OAuth2 email not found");
            }

            String userName = (name != null) ? name : email.split("@")[0];

            Optional<User> existingUser = userRepository.findByEmail(email);
            User user = existingUser.orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setUserName(userName);
                newUser.setPassWord(""); // Not used for OAuth users
                return userRepository.save(newUser);
            });
            userRepository.save(user);


            String token = jwtUtil.generateToken(user);
            log.info("OAuth2 login success for user: {}, provider: {}", email, provider);

            // Redirect to frontend with JWT
            String redirectUrl = "http:/localhost:5173/oauth2/redirect?token=" +
                    URLEncoder.encode(token, StandardCharsets.UTF_8);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("OAuth2 Authentication Error", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "OAuth2 Login Failed");
        }
    }

    private String getProviderFromRequest(HttpServletRequest request) {
        // Extract 'registrationId' from request URI, e.g., /oauth2/callback/google
        String uri = request.getRequestURI(); // e.g., /login/oauth2/code/google
        if (uri.contains("/code/")) {
            return uri.substring(uri.lastIndexOf("/code/") + 6).toUpperCase(); // GOOGLE
        }
        return "UNKNOWN";
    }
}
