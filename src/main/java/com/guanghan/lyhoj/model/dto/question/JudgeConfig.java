package com.guanghan.lyhoj.model.dto.question;

import lombok.Data;


@Data
public class JudgeConfig {
    /**
     * 时间限制 ms
     */
    private Long timeLimit;

    /**
     * 空间限制 kB
     */
    private Long memoryLimit;

    /**
     * 堆栈限制
     */
    private Long stackLimit;
}
