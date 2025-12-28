package com.example.dify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DifyWebClientConfig {

    @Bean
    public WebClient difyWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer app-oU1Bw2vxS72DLgqP6ITIEnqx")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
