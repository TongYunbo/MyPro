package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 团队中间变量表
 *
 * @Author hanxf
 * @Date 11:51 2019/5/16
**/
@Data
@ToString
public class TeamIntermediateVariableDO implements Serializable {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 团队ID
     */
    private Integer teamId;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 变量名
     */
    private String variableName;

    /**
     * 变量值
     */
    private String variableVal;

    /**
     * 变量的单位，为空的话说明不需要改变（如果运算的话需要根据单位进行值的改变）
     */
    private String unit;


    private static final long serialVersionUID = 1L;

}