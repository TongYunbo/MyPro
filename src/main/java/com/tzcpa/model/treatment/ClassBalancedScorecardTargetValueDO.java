package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 平衡记分卡目标值
 * @author hanxf
 */

@Data
@ToString
public class ClassBalancedScorecardTargetValueDO implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 目标值
     */
    private String targetValueRight;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 外键
     */
    private Integer foreignId;

    /**
     * 班级id
     */
    private Integer classId;


    private static final long serialVersionUID = 1L;

}