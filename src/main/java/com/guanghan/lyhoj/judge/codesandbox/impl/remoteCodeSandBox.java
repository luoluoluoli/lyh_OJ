package com.guanghan.lyhoj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.guanghan.lyhoj.common.ErrorCode;
import com.guanghan.lyhoj.exception.BusinessException;
import com.guanghan.lyhoj.judge.codesandbox.CodesandBox;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

public class remoteCodeSandBox implements CodesandBox {
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";
    @Override
    public ExecuteCodeResponse ExecuteCode(ExecuteCodeRequest executeCodeRequest) {
        String url = "http://117.72.70.134:8090/executeCode";
        String jsonStr = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER,AUTH_REQUEST_SECRET)
                .body(jsonStr)
                .execute()
                .body();
        if(StringUtils.isBlank(responseStr)){
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"execute remote code error");
        }
        return JSONUtil.toBean(responseStr,ExecuteCodeResponse.class);
    }
}
