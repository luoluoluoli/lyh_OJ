package com.guanghan.lyhoj.judge.codesandbox;

import com.guanghan.lyhoj.judge.codesandbox.impl.exampleCodeSandBox;
import com.guanghan.lyhoj.judge.codesandbox.impl.remoteCodeSandBox;
import com.guanghan.lyhoj.judge.codesandbox.impl.thirdCodeSandBox;

public class CodeSandBoxFactory {
    public static CodesandBox newInstance(String type) {
        switch (type) {
            case "example":
                return new exampleCodeSandBox();
            case "remote":
                return new remoteCodeSandBox();
            case "third":
                return new thirdCodeSandBox();
            default:
                return new exampleCodeSandBox();
        }

    }

}
