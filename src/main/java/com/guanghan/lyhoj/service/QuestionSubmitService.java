package com.guanghan.lyhoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guanghan.lyhoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.guanghan.lyhoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.guanghan.lyhoj.model.entity.Post;
import com.guanghan.lyhoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guanghan.lyhoj.model.entity.User;
import com.guanghan.lyhoj.model.vo.PostVO;
import com.guanghan.lyhoj.model.vo.QuestionSubmitVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author luoluoluo
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    long doQuestionSubmit(QuestionSubmitAddRequest questionId, User loginUser);

    QueryWrapper<QuestionSubmit>  getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取提交记录封装VO
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User
                                         loginUser);

    /**
     * 分页获取提及记录封装VO
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */

    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
