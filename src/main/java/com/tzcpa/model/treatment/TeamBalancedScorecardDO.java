package com.tzcpa.model.treatment;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
/**
 * 团队平衡记分卡信息
 * @author WangYao
 * 2019年5月21日
 */
@Data
@ToString
public class TeamBalancedScorecardDO implements Serializable {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 角度
     */
    private String angle;

    /**
     * 选项
     */
    private Integer option;

    /**
     * 问题选项  即平衡记分卡指标
     */
    private String quesionOption;

    /**
     * 问题选项的文字描述  即平衡记分卡指标
     */
    private String questionOptionValue;

    /**
     * 目标值
     */
    private String targetValue;

    /**
     * 标准分
     */
    private Integer standardScore;

    /**
     * 权重系数
     */
    private Float weightCoefficient;

    /**
     * 实际值（系统根据选择指标取值）
     */
    private String actualValue;

    /**
     * 最终得分
     */
    private Integer finalScore;

    /**
     * 得分标准
     */
    private String scoreStandard;

    /**
     * 问题id
     */
    private Integer questionId;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 团队id
     */
    private Integer teamId;

    /**
     * 题号
     */
    private Integer options;

    private static final long serialVersionUID = 1L;
}