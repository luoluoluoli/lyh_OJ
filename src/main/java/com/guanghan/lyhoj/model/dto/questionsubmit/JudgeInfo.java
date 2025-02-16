package com.guanghan.lyhoj.model.dto.questionsubmit;

import lombok.Data;

@Data
public class JudgeInfo {
    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗时间
     */
    private Long time;

    /**
     * 消耗内存
     */
    private Long memery;
}
