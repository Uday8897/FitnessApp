package com.gateway.UserSync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {

    @Autowired
    private WebClient userWebClient;

    public Mono<Boolean> validateUser(String userId) {
        return userWebClient.get()
                .uri("/api/users/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.just(false);
                    } else {
                        return Mono.error(new RuntimeException("Server error while validating user", e));
                    }
                })
                .onErrorResume(WebClientRequestException.class, e -> {
                    return Mono.error(new RuntimeException("Connection error while validating user", e));
                })
                .onErrorResume(WebClientException.class, e -> {
                    return Mono.error(new RuntimeException("Unexpected error while validating user", e));
                });
    }

    public Mono<UserResponse> registerUser(RegisterRequest request) {
log.info("Reigster user************************************* with email {}",request.getEmail()) ;
        return userWebClient.post()
                .uri("/api/users/register").bodyValue(request)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new RuntimeException("Bad Request", e));

                    } else {
                        return Mono.error(new RuntimeException("Internal Server Error ", e));
                    }
                });
    }
}
