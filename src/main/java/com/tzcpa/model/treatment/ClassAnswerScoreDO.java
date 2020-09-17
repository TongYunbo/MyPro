package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
/**
 * @Author hanxf
 * @Description  标准答案得分
 * @Date 14:39 2019/5/14
**/
@Data
@ToString
public class ClassAnswerScoreDO implements Serializable {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 标准答案
     */
    private String answer;

    /**
     * 问题ID
     */
    private Integer questionId;

    /**
     * 每个选项分值
     */
    private Integer score;

    /**
     * 差异化战略:A 成本优先战略:B
     */
    private String strategicChoice;

    /**
     * 班级id
     */
    private Integer classId;

    private static final long serialVersionUID = 1L;

}