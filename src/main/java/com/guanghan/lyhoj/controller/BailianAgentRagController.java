package com.guanghan.lyhoj.controller;

import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgent;
import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgentOptions;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/ai")
@Slf4j
public class BailianAgentRagController {

    private DashScopeAgent agent;

    @Value("${spring.ai.dashscope.agent.app-id}")
    private String appId;

    public BailianAgentRagController(DashScopeAgentApi dashscopeAgentApi) {
        this.agent = new DashScopeAgent(dashscopeAgentApi);
    }

    @GetMapping("/bailian/agent/call")
    public String call(@RequestParam(value = "message",
            defaultValue = "如何使用SDK快速调用阿里云百炼的应用?") String message) {
        ChatResponse response = agent.call(new Prompt(message, DashScopeAgentOptions.builder().withAppId(appId).build()));
        if (response == null || response.getResult() == null) {
            log.error("chat response is null");
            return "chat response is null";
        }
        AssistantMessage app_output = response.getResult().getOutput();
        return app_output.getContent();
    }

    @GetMapping(value="/bailian/agent/stream")
    public Flux<String> stream(@RequestParam(value = "message",
            defaultValue = "你好，说一句你好") String message) {
        return agent.stream(new Prompt(message, DashScopeAgentOptions.builder().withAppId(appId).build())).map(response -> {
            if (response == null || response.getResult() == null) {
                log.error("chat response is null");
                return "chat response is null";
            }
            AssistantMessage app_output = response.getResult().getOutput();

            String content = app_output.getContent();

            return content;
        });
    }
}