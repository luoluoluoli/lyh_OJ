package com.guanghan.lyhoj.judge.strategy;

import com.guanghan.lyhoj.model.dto.questionsubmit.JudgeInfo;

public interface JudgeStrategy {

    JudgeInfo doJudge(JudgeContext judgeContext);
}
