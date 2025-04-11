package com.guanghan.lyhoj.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guanghan.lyhoj.common.ErrorCode;
import com.guanghan.lyhoj.constant.CommonConstant;
import com.guanghan.lyhoj.exception.BusinessException;
import com.guanghan.lyhoj.exception.ThrowUtils;
import com.guanghan.lyhoj.mapper.QuestionSubmitMapper;
import com.guanghan.lyhoj.model.dto.question.QuestionQueryRequest;
import com.guanghan.lyhoj.model.entity.*;
import com.guanghan.lyhoj.model.entity.Question;
import com.guanghan.lyhoj.model.enums.QuestionSubmitStatusEnum;
import com.guanghan.lyhoj.model.vo.QuestionVO;
import com.guanghan.lyhoj.model.vo.UserVO;
import com.guanghan.lyhoj.service.QuestionService;
import com.guanghan.lyhoj.mapper.QuestionMapper;
import com.guanghan.lyhoj.service.QuestionSubmitService;
import com.guanghan.lyhoj.service.UserService;
import com.guanghan.lyhoj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luoluoluo
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService{


    @Resource
    private UserService userService;
    
    @Resource
    private QuestionSubmitMapper questionSubmitMapper;


    @Override
    public void validQuestion(Question question, boolean add) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String answer = question.getAnswer();
        String judgeConfig = question.getJudgeConfig();
        String judgeCase = question.getJudgeCase();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(tags) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签过长");
        }
        if (StringUtils.isNotBlank(answer) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "配置过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "测试用例过长");
        }
    }

    /**
     * 获取查询包装类
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {

        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();
        String keyword = questionQueryRequest.getKeyword();
        String status = questionQueryRequest.getStatus();
        String difficulty = questionQueryRequest.getDifficulty();
        List<String> tags = questionQueryRequest.getTags();
        

        //不查询content和answer，因为很多时候不显示
        queryWrapper.select(Question.class, item -> !item.getColumn().equals("content") && !item.getColumn().equals("answer"));
        if(StrUtil.isNotBlank(status) && !status.equals("全部")){
            Long userId = questionQueryRequest.getUserId();
            Set<Long> passedIds;
            Set<Long> triedIds;
            switch (status){
                case "已通过":
                    passedIds = questionSubmitMapper.selectList(new LambdaQueryWrapper<QuestionSubmit>()
                                    .select(QuestionSubmit::getQuestionId).eq(QuestionSubmit::getUserId, userId)
                                    .eq(QuestionSubmit::getStatus, QuestionSubmitStatusEnum.SUCCEED.getValue()))
                            .stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toSet());
                    if(passedIds.isEmpty()){
                        return null;
                    }
                    queryWrapper.in("id", passedIds);
                    break;
                case "尝试过":
                    passedIds = questionSubmitMapper.selectList(new LambdaQueryWrapper<QuestionSubmit>()
                                    .select(QuestionSubmit::getQuestionId).eq(QuestionSubmit::getUserId, userId)
                                    .eq(QuestionSubmit::getStatus, QuestionSubmitStatusEnum.SUCCEED.getValue()))
                            .stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toSet());
                    triedIds = questionSubmitMapper.selectList(new LambdaQueryWrapper<QuestionSubmit>()
                                    .select(QuestionSubmit::getQuestionId).eq(QuestionSubmit::getUserId, userId)
                                    .ne(QuestionSubmit::getStatus, QuestionSubmitStatusEnum.SUCCEED.getValue()))
                            .stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toSet());
                    triedIds = (Set<Long>) CollUtil.subtract(triedIds, passedIds);
                    if(triedIds.isEmpty()){
                        return null;
                    }
                    queryWrapper.in("id", triedIds);
                    break;
                case "未开始":
                    triedIds = questionSubmitMapper.selectList(new LambdaQueryWrapper<QuestionSubmit>()
                                    .select(QuestionSubmit::getQuestionId).eq(QuestionSubmit::getUserId, userId))
                            .stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toSet());
                    if(!triedIds.isEmpty()){
                        queryWrapper.notIn("id", triedIds);
                    }
                    break;
            }
        }

        // 拼接查询条件
        boolean likeQuery = StringUtils.isNotBlank(keyword);
        queryWrapper.like(likeQuery, "title", keyword);
        queryWrapper.like(likeQuery, "content", keyword);
        queryWrapper.like(likeQuery, "answer", keyword);

        if (CollUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(StringUtils.isNotBlank(difficulty), "difficulty", difficulty);
//        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        QuestionVO questionVO = QuestionVO.objToVO(question);
        long questionId = question.getId();
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUserVO(userVO);
        return questionVO;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollUtil.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));


        // 填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = QuestionVO.objToVO(question);
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUserVO(userService.getUserVO(user));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    @Override
    public List<String> getQuestionTags() {
        return lambdaQuery().select(Question::getTags).list().stream()
                .flatMap(problem -> JSONUtil.toList(problem.getTags(), String.class).stream())
                .distinct().collect(Collectors.toList());

    }
}







