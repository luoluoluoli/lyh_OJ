package com.guanghan.lyhoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guanghan.lyhoj.common.ErrorCode;
import com.guanghan.lyhoj.exception.BusinessException;
import com.guanghan.lyhoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.guanghan.lyhoj.model.entity.Question;
import com.guanghan.lyhoj.model.entity.QuestionSubmit;
import com.guanghan.lyhoj.model.entity.User;
import com.guanghan.lyhoj.model.enums.QuestionSubmitLanguageEnum;
import com.guanghan.lyhoj.model.enums.QuestionSubmitStatusEnum;
import com.guanghan.lyhoj.service.QuestionService;
import com.guanghan.lyhoj.service.QuestionSubmitService;
import com.guanghan.lyhoj.mapper.QuestionSubmitMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author luoluoluo
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{
    @Resource
    private QuestionService questionService;

    /**
     *  提交题目
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return 创建的提交记录的id
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        //检验编程语言
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum enumByValue = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if(enumByValue == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言错误");
        }
        // 判断实体是否存在，根据类别获取实体
        Long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        Long userId = loginUser.getId();
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 提交对象
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        questionSubmit.setCode(questionSubmitAddRequest.getCode());

        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if(!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据插入异常");
        }
        return questionSubmit.getId();

    }
}




