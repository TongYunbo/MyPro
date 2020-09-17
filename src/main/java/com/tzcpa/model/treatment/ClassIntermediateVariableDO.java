package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 班级中间变量表
 *
 * @Author hanxf
 * @Date 11:50 2019/5/16
**/
@Data
@ToString
public class ClassIntermediateVariableDO implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 变量名
     */
    private String variableName;

    /**
     * 选项值
     */
    private String optionVal;

    /**
     * 变量值
     */
    private String variableVal;

    /**
     * 差异化战略:A 成本优先战略:B
     */
    private String strategicChoice;

    /**
     * 问题ID
     */
    private Integer questionId;

    /**
     * 变量的单位，为空的话说明不需要改变（如果运算的话需要根据单位进行值的改变）
     */
    private String unit;

    /**
     * 比较时候调用的方法，为空说明不需要计算（有些变量需要比较来确定值）
     */
    private String compareMethod;

    /**
     * 班级id
     */
    private int classId;


    private static final long serialVersionUID = 1L;


}