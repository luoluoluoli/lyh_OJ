package com.guanghan.lyhoj.judge.codesandbox.impl;

import com.guanghan.lyhoj.judge.codesandbox.CodeSandboxTemplate;
import com.guanghan.lyhoj.judge.codesandbox.CodesandBox;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeResponse;

public class CppCodeSandBox extends CodeSandboxTemplate  {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("cpp");
        return null;
    }
}
