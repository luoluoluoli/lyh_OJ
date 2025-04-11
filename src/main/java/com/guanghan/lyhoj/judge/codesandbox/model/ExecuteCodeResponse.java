package com.guanghan.lyhoj.judge.codesandbox.model;

import com.guanghan.lyhoj.model.dto.questionsubmit.JudgeInfo;
import lombok.Data;

import java.util.List;
@Data
public class ExecuteCodeResponse {
    private List<String> outputList;

    /**
     * 接口信息
     */
    private String message;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;



}
