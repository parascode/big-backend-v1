package com.mac.big_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder){
        return builder
                .baseUrl("http://api.weatherapi.com/v1")
                .build();
    }
}
