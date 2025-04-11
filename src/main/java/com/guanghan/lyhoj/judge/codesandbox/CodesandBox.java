package com.guanghan.lyhoj.judge.codesandbox;

import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodesandBox {
    /**
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
