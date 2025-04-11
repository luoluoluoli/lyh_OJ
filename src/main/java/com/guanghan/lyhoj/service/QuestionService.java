package com.guanghan.lyhoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guanghan.lyhoj.model.dto.question.QuestionQueryRequest;
import com.guanghan.lyhoj.model.entity.Question;
import com.guanghan.lyhoj.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guanghan.lyhoj.model.vo.QuestionVO;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author luoluoluo
*/
public interface QuestionService extends IService<Question> {

    /**
     * 校验
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);


    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    List<String> getQuestionTags();
}
