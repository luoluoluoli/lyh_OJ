package com.guanghan.lyhoj.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/steamChat")
    public Flux<String> steamChat(@RequestParam String input) {
        return this.chatClient.prompt().user(input).stream().content();
    }

    @GetMapping("/codeanalies")
    public Flux<String> codeanalies(@RequestParam String input) {
        return this.chatClient.prompt("给这段代码一个分析，从可读性，代码规范，语法建议等角度分析，并给出一个分数").user(input).stream().content();
    }
}