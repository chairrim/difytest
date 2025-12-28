package com.example.dify.controller;

import com.example.dify.service.DifyStreamService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/dify")
public class DifyStreamController {

    private final DifyStreamService difyStreamService;

    public DifyStreamController(DifyStreamService difyStreamService) {
        this.difyStreamService = difyStreamService;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String query) {
        return difyStreamService.streamChat(query);
    }
}
