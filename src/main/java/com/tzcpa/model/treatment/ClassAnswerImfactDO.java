package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 *班级答案影响
 *
 * @Author hanxf
 * @Date 17:06 2019/5/14
**/
@Data
@ToString
public class ClassAnswerImfactDO implements Serializable {
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
     * 差异化战略:A 成本优先战略:B
     */
    private String strategicChoice;

    /**
     * 是否选中的时候影响，选中：0 未选中：1
     */
    private Integer isSelect;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 任务id
     */
    private Integer taskId;


    private static final long serialVersionUID = 1L;


}