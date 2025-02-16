package com.guanghan.lyhoj.service;

import com.guanghan.lyhoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.guanghan.lyhoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guanghan.lyhoj.model.entity.User;

/**
 * @author luoluoluo
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    long doQuestionSubmit(QuestionSubmitAddRequest questionId, User loginUser);
}
