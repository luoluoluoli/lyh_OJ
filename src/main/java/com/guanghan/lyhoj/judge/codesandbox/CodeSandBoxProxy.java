package com.guanghan.lyhoj.judge.codesandbox;

import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeSandBoxProxy implements CodesandBox{
    private final CodesandBox codesandBox;

    public CodeSandBoxProxy(CodesandBox codesandBox) {
        this.codesandBox = codesandBox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("ExecuteCodeRequest:{}", executeCodeRequest);
        ExecuteCodeResponse executeCodeResponse = codesandBox.executeCode(executeCodeRequest);
        log.info("ExecuteCodeResponse:{}", executeCodeResponse);
        return executeCodeResponse;
    }
}
