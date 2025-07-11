package com.gateway.Filters;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced  // 🔥 Enables Eureka to resolve lb://AUTH-SERVICE
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
