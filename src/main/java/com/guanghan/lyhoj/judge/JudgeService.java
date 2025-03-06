package com.guanghan.lyhoj.judge;

import com.guanghan.lyhoj.model.entity.QuestionSubmit;
import com.guanghan.lyhoj.model.vo.QuestionSubmitVO;

public interface JudgeService {

    QuestionSubmit doJudge(Long questionSubmitId);
}
