package com.guanghan.lyhoj.controller;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;



@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    @Value("${api.key}")
    private String API_KEY;


    /**
     * 创建线程池
    **/
    ThreadPoolExecutor executor = new ThreadPoolExecutor(2,4,60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * 实现 chat 接口，支持流式返回数据
     *
     * @param query
     * @return
     */
    @RequestMapping(value = "/chat", method = RequestMethod.POST)
    public ResponseBodyEmitter streamData(@RequestBody String query) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(180000L);
        executor.execute(() -> {
          try {
            JsonObject jsonObject = new JsonParser().parse(query).getAsJsonObject();
            streamCall(emitter, jsonObject.get("prompt").getAsString());
          } catch (NoApiKeyException | InputRequiredException e) {
              e.printStackTrace();
              emitter.completeWithError(e);
          }
        });
        return emitter;
    }

    /**
     * 调用百炼应用，封装流式返回数据
     * 返回数据格式
     * id:1
     * event:result
     * :HTTP_STATUS/200
     * data:{"output":{"session_id":"xxx","finish_reason":"null","text":"相关的问题"}}
     *
     * @param emitter
     * @param query
     * @throws NoApiKeyException
     * @throws InputRequiredException
     */
    public void streamCall(ResponseBodyEmitter emitter, String query) throws NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message userMsg = Message.builder().role(Role.USER.getValue()).content("你是谁？").build();
        Flowable<GenerationResult> result = streamCallWithMessage(gen, userMsg);;
        AtomicInteger counter = new AtomicInteger(0);
        result.blockingForEach(data -> {
            int newValue = counter.incrementAndGet();
            String resData = "id:" + newValue + "\nevent:result\n:HTTP_STATUS/200\ndata:" + new Gson().toJson(data) + "\n\n";
            emitter.send(resData.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            if ("stop".equals(data.getOutput().getFinishReason())) {
                emitter.complete();
            }
        });
    }

    public static Flowable<GenerationResult> streamCallWithMessage(Generation gen, Message userMsg) throws NoApiKeyException, ApiException, InputRequiredException {
        GenerationParam param = buildGenerationParam(userMsg);
        Flowable<GenerationResult> result = gen.streamCall(param);
        return result;
    }

    private static GenerationParam buildGenerationParam(Message userMsg) {
        return GenerationParam.builder()
                .apiKey("sk-018b0fbd0fef42bd99a3ebfd017b9d4f")
                // 此处以qwen-plus为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-plus")
                .messages(Arrays.asList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .incrementalOutput(true)
                .build();

    }
}