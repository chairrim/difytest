package com.example.dify.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class DifyStreamService {

    private final ObjectMapper objectMapper;


    private final WebClient difyWebClient;

    public DifyStreamService(ObjectMapper objectMapper, WebClient difyWebClient) {
        this.objectMapper = objectMapper;
        this.difyWebClient = difyWebClient;
    }

    public Flux<String> streamChat(String query) {
        return difyWebClient.post()
                .uri("/chat-messages")
                .bodyValue(Map.of(
                        "inputs", Map.of(),
                        "query", query,
                        "response_mode", "streaming",
                        "user", "frontend-user"
                ))
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(this::parseDifyLine)
                .filter(StringUtils::hasText);
    }
    /**
     * 从 Dify streaming 数据中提取 answer
     */
    private String extractAnswer(String raw) {
        if (raw.contains("\"answer\"")) {
            int start = raw.indexOf("\"answer\":\"") + 10;
            int end = raw.indexOf("\"", start);
            return raw.substring(start, end);
        }
        return "";
    }

    private Flux<String> parseDifyLine(String line) {

        // 1. 忽略空行
        if (line == null || line.isEmpty()) {
            return Flux.empty();
        }

        String data = line.trim();


        try {
            JsonNode json = objectMapper.readTree(data);

            // 3. 只取真正给用户看的内容
            if ("message".equals(json.path("event").asText())) {
                String answer = json.path("answer").asText();
                return Flux.just(answer);
            }

        } catch (Exception ignored) {}

        return Flux.empty();
    }

}
