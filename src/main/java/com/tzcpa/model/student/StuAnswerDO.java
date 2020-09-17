package com.tzcpa.model.student;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 *
 * @author wangbj
 * <p>
 * 2019年4月28日
 */
@Data
@ToString
public class StuAnswerDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 答案id
     */
    private Integer id;

    /**
     * 答案
     */
    private String answer;

    /**
     * 题目id
     */
    private Integer questionId;

    /**
     * 团队id
     */
    private Integer teamId;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 答题年份
     */
    private Integer year;

    /**
     * 答题月份
     */
    private Integer month;

    /**
     * 事件编号
     */
    private String eventCode;

    /**
     * 主题id
     */
    private Integer rootId;

    /**
     * 本题分数所占权重
     */
    private Double weight;

    private String roleId;

    /**
     * 时间线
     */
    private String timeLine;

    /**
     * 是否受战略影响
     */
    private Integer isStrategic;
}