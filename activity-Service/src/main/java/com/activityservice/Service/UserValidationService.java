package com.activityservice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class UserValidationService {

    @Autowired
    private WebClient userWebClient;

    public boolean validateUser(String userId) {
        try {
            Boolean result = userWebClient.get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            return Boolean.TRUE.equals(result);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("User not found with userId " + userId);
            } else {
                throw new RuntimeException("Error from user service: " + e.getStatusText(), e);
            }
        } catch (WebClientRequestException e) {
            throw new RuntimeException("Unable to reach user service", e);
        }
    }
}