package com.guanghan.lyhoj.model.dto.questionsubmit;

import com.guanghan.lyhoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 * @author luoluoluo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {


    /**
     * 编程语言
     */
    private String language;


    /**
     * 判题状态 (0-待判题、1-判题中、2-成功、3-失败)
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;



    private static final long serialVersionUID = 1L;
}