package com.guanghan.lyhoj.judge;

import cn.hutool.json.JSONUtil;
import com.guanghan.lyhoj.common.ErrorCode;
import com.guanghan.lyhoj.exception.BusinessException;
import com.guanghan.lyhoj.judge.codesandbox.CodeSandBoxFactory;
import com.guanghan.lyhoj.judge.codesandbox.CodeSandBoxProxy;
import com.guanghan.lyhoj.judge.codesandbox.CodesandBox;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.guanghan.lyhoj.judge.strategy.DefaultJudgeStrategy;
import com.guanghan.lyhoj.judge.strategy.JudgeContext;
import com.guanghan.lyhoj.judge.strategy.JudgeStrategy;
import com.guanghan.lyhoj.model.dto.question.JudgeCase;
import com.guanghan.lyhoj.model.dto.questionsubmit.JudgeInfo;
import com.guanghan.lyhoj.model.entity.Question;
import com.guanghan.lyhoj.model.entity.QuestionSubmit;
import com.guanghan.lyhoj.model.enums.QuestionSubmitLanguageEnum;
import com.guanghan.lyhoj.model.enums.QuestionSubmitStatusEnum;
import com.guanghan.lyhoj.model.vo.QuestionSubmitVO;
import com.guanghan.lyhoj.service.QuestionService;
import com.guanghan.lyhoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Value("${codesandbox.type:example}")
    private String type;

    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private JudgeManeger judgeManeger;

    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
//      判题服务业务流程
//      传入题目的提交d,获取到对应的题目、提交信悬（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
//      如果题目提交状态不为等待中，就不用重复执行了
        if(!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"题目已经在判题中");
        }
        //更改判题（题目提交）的状态为“判题中”，防止重复执行，也能让用户即时看到状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if(!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"判题状态更新失败");
        }
        //获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaselist =JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaselist.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest request = ExecuteCodeRequest.builder()
                .language(questionSubmit.getLanguage())
                .code(questionSubmit.getCode())
                .inputList(inputList)
                .build();
//        调用沙箱，获取到执行结果
        CodesandBox codesandBox = CodeSandBoxFactory.newInstance(questionSubmit.getLanguage());
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codesandBox);
        ExecuteCodeResponse executeCodeResponse = codeSandBoxProxy.executeCode(request);
        List<String> outputList = executeCodeResponse.getOutputList();
        System.out.println(executeCodeResponse);
//        根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        judgeContext.setJudgeCaseList(judgeCaselist);
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());

        JudgeInfo judgeInfo = judgeManeger.doJudge(judgeContext);


        //修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if(!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"判题状态更新失败");
        }

        QuestionSubmit questionSubmitResponse = questionSubmitService.getById(questionSubmitId);
        return questionSubmitResponse;


    }
}
