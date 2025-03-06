package com.guanghan.lyhoj.judge.codesandbox.model;

import com.guanghan.lyhoj.model.dto.questionsubmit.JudgeInfo;
import lombok.Data;

import java.util.List;
@Data
public class ExecuteCodeResponse {
    private List<String> outputList;

    private String message;

    private JudgeInfo judgeInfo;


}
