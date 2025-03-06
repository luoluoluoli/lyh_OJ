package com.guanghan.lyhoj.judge.codesandbox.impl;

import com.guanghan.lyhoj.judge.codesandbox.CodesandBox;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeResponse;

public class exampleCodeSandBox implements CodesandBox {
    @Override
    public ExecuteCodeResponse ExecuteCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("是例子");
        return null;
    }
}
