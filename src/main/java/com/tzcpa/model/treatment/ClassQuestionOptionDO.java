package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目选项DTO
 *
 * @Author hanxf
 * @Date 16:17 2019/5/14
 * @param
 * @return
**/
@Data
@ToString
public class ClassQuestionOptionDO implements Serializable {

    private Integer id;

    private Integer lastId;

    /**
     * 问题id
     */
    private Integer questionId;

    /**
     * 问题选项
     */
    private String quesionOption;

    /**
     * 问题选项描述
     */
    private String quesionOptionDesc;

    /**
     * 目标值
     */
    private String targetValue;

    /**
     * 班级id
     */
    private Integer classId;

    private Integer teamId;

    private String unit;

    private String balanceScoreFunc;

    private Date createTime;

    private static final long serialVersionUID = 1L;

}