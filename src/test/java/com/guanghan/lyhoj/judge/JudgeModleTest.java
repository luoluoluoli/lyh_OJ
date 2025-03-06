package com.guanghan.lyhoj.judge;

import com.guanghan.lyhoj.judge.codesandbox.CodeSandBoxFactory;
import com.guanghan.lyhoj.judge.codesandbox.CodeSandBoxProxy;
import com.guanghan.lyhoj.judge.codesandbox.CodesandBox;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.guanghan.lyhoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.guanghan.lyhoj.model.entity.QuestionSubmit;
import com.guanghan.lyhoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class JudgeModleTest {
    @Value("${codesandbox.type:example}")
    private String type;

    @Test
    void test() {
        CodesandBox codesandBox = CodeSandBoxFactory.newInstance(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codesandBox);

        ExecuteCodeRequest request = ExecuteCodeRequest.builder()
                .language(QuestionSubmitLanguageEnum.JAVA.getValue())
                .code("public class Main {\n" +
                        "\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int a = Integer.parseInt(args[0]);\n" +
                        "        int b = Integer.parseInt(args[1]);\n" +
                        "        System.out.println(\"结果:\" + (a + b));\n" +
                        "    }\n" +
                        "}")
                .inputList(Arrays.asList("1 2","3 4"))
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBoxProxy.ExecuteCode(request);
        System.out.println(executeCodeResponse);


    }

}
