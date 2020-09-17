package com.tzcpa.model.student;

import lombok.Data;
import lombok.ToString;

/**
 * 团队平衡记分卡题目
 *
 * @author WangYao
 * 2019年5月17日
 */
@Data
@ToString
public class BalancedScorecard {

    /**
     * 主键id
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
     * 单位
     */
    private String unit;

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
    private Float finalScore;

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
     * 对应Id
     */
    private Integer infactId;

    /**
     * 团队id
     */
    private Integer teamId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 计算最终值方法名
     */
    private String balanceScoreFunc;

    /**
     * 排序
     */
    private Integer order;
    
    /**
     * 正确问题选项  即平衡记分卡指标
     */
    private String quesionOptionRight;
    
    /**
     * 正确选项对应目标值
     */
    private String targetValueRight;
    
    /**
     * 正确选项对应目标值单位
     */
    private String unitRight;
    
    /**
     * 正确选项对应权重系数
     */
    private Float weightCoefficientRight;
    
    /**
     * 正确选项对应实际值（系统根据选择指标取值）
     */
    private String actualValueRight;
    
    /**
     * 正确选项对应实际值计算方法名
     */
    private String balanceScoreFuncRight;
    
    /**
     * 匹配度
     */
    private Float matchingDegree;
    
    /**
     * 系统考核模块得分
     */
    private Float checkScore;
    
    /**
     * 战略化
     */
    private String strategy;

    /**
     * 题号
     */
    private Integer options;
}