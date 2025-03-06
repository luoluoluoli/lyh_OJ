package com.guanghan.lyhoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guanghan.lyhoj.common.BaseResponse;
import com.guanghan.lyhoj.common.ErrorCode;
import com.guanghan.lyhoj.common.ResultUtils;
import com.guanghan.lyhoj.exception.BusinessException;
import com.guanghan.lyhoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.guanghan.lyhoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.guanghan.lyhoj.model.entity.QuestionSubmit;
import com.guanghan.lyhoj.model.entity.User;
import com.guanghan.lyhoj.model.vo.QuestionSubmitVO;
import com.guanghan.lyhoj.service.QuestionSubmitService;
import com.guanghan.lyhoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 * @author luoluoluo
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 提交记录id
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
            HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        final User loginUser = userService.getLoginUser(request);
        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非代码，提及代码等公开信息）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listPostByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                               HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getPageNum();
        long size = questionSubmitQueryRequest.getPageSize();
        //从数据库中查询题目原始的提及记录
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        //根据当前登录用户身份的不同脱敏返回
        final User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage,loginUser));
    }



}
