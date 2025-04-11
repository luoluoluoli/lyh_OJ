package com.guanghan.lyhoj.judge.codesandbox;

import com.guanghan.lyhoj.judge.codesandbox.impl.CppCodeSandBox;
import com.guanghan.lyhoj.judge.codesandbox.impl.JavaCodeSandBox;
import com.guanghan.lyhoj.judge.codesandbox.impl.PythonCodeSandBox;

public class CodeSandBoxFactory {
    public static CodesandBox newInstance(String type) {
        switch (type) {
            case "java":
                return new JavaCodeSandBox();
            case "python":
                return new PythonCodeSandBox();
            case "cpp":
                return new CppCodeSandBox();
            default:
                return new JavaCodeSandBox();
        }

    }

}
