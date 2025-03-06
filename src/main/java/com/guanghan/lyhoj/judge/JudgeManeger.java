package com.guanghan.lyhoj.judge;

import com.guanghan.lyhoj.judge.strategy.DefaultJudgeStrategy;
import com.guanghan.lyhoj.judge.strategy.JavaJudgeStrategy;
import com.guanghan.lyhoj.judge.strategy.JudgeContext;
import com.guanghan.lyhoj.judge.strategy.JudgeStrategy;
import com.guanghan.lyhoj.model.dto.questionsubmit.JudgeInfo;
import com.guanghan.lyhoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

@Service
public class JudgeManeger {

    JudgeInfo doJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if(language.equals("java")){
            judgeStrategy = new JavaJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
