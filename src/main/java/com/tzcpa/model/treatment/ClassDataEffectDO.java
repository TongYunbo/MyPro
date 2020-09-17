package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author hanxf
 * @Description  影响指标项配置
 * @Date 14:28 2019/5/14
**/
@Data
@ToString
public class ClassDataEffectDO implements Serializable {
    /**
     *
     * 主键id
     */
    private Integer id;

    /**
     * 任务id 对应answer_impact的id
     */
    private Integer taskId;

    /**
     * 影响项描述
     */
    private String impactStudyDesc;

    /**
     * 操作影响
     */
    private String operation;

    /**
     * 是否按照年度影响，0:不是，1:是
     */
    private Integer isYear;

    /**
     * 影响数值，1是年度的为年数，0为月数
     */
    private Integer impactNum;

    /**
     * 需要修改的表
     */
    private String tableName;

    /**
     * 表中的列
     */
    private String tableColumn;

    /**
     * 死条件用来放到where后
     */
    private String condition;

    /**
     * 排序值，越小越先执行
     */
    private Integer sortNum;

    /**
     * 此影响的开始时间（包含本时间）
     */
    private String beginDate;

    private static final long serialVersionUID = 1L;


}