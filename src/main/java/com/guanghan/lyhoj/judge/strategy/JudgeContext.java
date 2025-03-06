package com.guanghan.lyhoj.judge.strategy;

import com.guanghan.lyhoj.model.dto.question.JudgeCase;
import com.guanghan.lyhoj.model.dto.questionsubmit.JudgeInfo;
import com.guanghan.lyhoj.model.entity.Question;
import com.guanghan.lyhoj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文，用于在策略中传递的参数
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
