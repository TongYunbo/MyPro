package com.tzcpa.model.teacher;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName BaseAutoAnswer
 * @Description
 * @Author wangzhangju
 * @Date 2019/6/18 19:55
 * @Version 6.0
 **/
@Data
public class BaseAutoAnswer {
    private Integer classId;
    private Integer teamId;
    /**
     * 一级问题id
     */
    private Integer questionId;
    private Integer rootId;

    private int isRandom;

    /**
     * 选项个数
     */
    private int severalOptions;

    /**
     * 从选项中选择个数
     */
    private int chooseFew;


    /**
     * 事件编号
     */
    private String eventCode;

    /**
     * 本题分数所占权重
     */
    private Double weight;

    private Integer year;

    private Integer month;

    /**
     * 问题类型
     */
    private Integer questionType;

    private String eventName;

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
