package com.guanghan.lyhoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.guanghan.lyhoj.model.dto.question.JudgeCase;
import com.guanghan.lyhoj.model.dto.question.JudgeConfig;
import com.guanghan.lyhoj.model.dto.questionsubmit.JudgeInfo;
import com.guanghan.lyhoj.model.entity.Question;
import com.guanghan.lyhoj.model.entity.QuestionSubmit;
import com.guanghan.lyhoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;

public class DefaultJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memery = judgeInfo.getMemery();
        Long time = judgeInfo.getTime();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();

        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACEEPTED;

        JudgeInfo judgeinfoResponse = new JudgeInfo();
        judgeinfoResponse.setMemery(memery);
        judgeinfoResponse.setTime(time);

        //输入输出数量
        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSER;
            judgeinfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeinfoResponse;
        }

        //依次判断每一次输出和预期输出是否相等
        for(int i = 0 ; i < outputList.size() ; i++){
            //拿正确输出和输出列表顺序比对
            JudgeCase judgeCase = judgeCaseList.get(i);
            if(!judgeCase.getOutput().equals(outputList.get(i))){}
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSER;
            judgeinfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeinfoResponse;
        }

        //判断题目条件限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long memoryLimit = judgeConfig.getMemoryLimit();
        Long timeLimit = judgeConfig.getTimeLimit();
        if(memery > memoryLimit){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMERY_LIMIT_EXCEEDED;
            judgeinfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeinfoResponse;
        }
        if(time > timeLimit){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeinfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeinfoResponse;
        }

        return judgeinfoResponse;
    }
}
