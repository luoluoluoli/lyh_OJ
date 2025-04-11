package com.guanghan.lyhoj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.guanghan.lyhoj.common.ErrorCode;
import com.guanghan.lyhoj.exception.BusinessException;
import com.guanghan.lyhoj.judge.codesandbox.CodeSandboxTemplate;
import com.guanghan.lyhoj.judge.codesandbox.CodesandBox;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

public class PythonCodeSandBox extends CodeSandboxTemplate {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("python code");
        return new ExecuteCodeResponse();
    }
}
