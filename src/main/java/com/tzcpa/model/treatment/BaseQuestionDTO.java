package com.tzcpa.model.treatment;

import com.tzcpa.model.teacher.Mark;
import com.tzcpa.utils.JsonUtil;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BaseQuestionDTO
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/10 15:36
 * @Version 6.0
 **/
@Data
@ToString
public class BaseQuestionDTO implements Serializable {

    /**
     * 是否初始化 1-是 0-答题 2-查询记录
     */
    private int initOrNot;
    /**
     * 当前题目idquestionId
     */
    private int thisItemId;

    /**
     * 当前题目内容
     */
    private String thisItemContent;

    /**
     * 当前题目选项
     */
    private List<Map<String,Object>> thisItemOptions;

    /**
     * 当前题目答案
     */
    private List<String> thisItemAnswer = new ArrayList<>();

    public void setThisItemAnswer(List<String> thisItemAnswer) {
        if(null != thisItemAnswer && !thisItemAnswer.isEmpty()) {
            this.thisItemAnswer = thisItemAnswer;
        }
    }

    private String answer;

    public void setAnswer(String answer) {
        this.answer = answer;
        if(answer!=null&&!answer.isEmpty()){
            this.setThisItemAnswer(JsonUtil.jsonToList(answer,String.class));
        }
    }

    /**
     * 下一题目id
     */
    private int nextItemId;

    /**
     *  题目类型
     */
    private int questionType;

    /**
     * 事件问题类型 2-题目 3-地图
     */
    private int eventType;

    /**
     * 事件问题编号
     */
    private String eventCode;

    /**
     * 事件问题名字
     */
    private String eventName;

    /**
     * 时间线
     */
    private String timeLine;

    /**
     * 加分对象id 例如：1,2,3等(传入的时候为单个，在数据中计算的时候有可能会变成多个以逗号分割)
     */
    private String roleId;

    /**
     * 权重
     */
    private Double weight;

    /**
     * 是否受战略影响
     */
    private String isStrategic;

    /**
     * 背景资料
     */
    private String backgroundDesc;

    /**
     * 题目对应的年份
     */
    private Integer questionYear;

    /**
     * 题目对应的月份
     */
    private Integer questionMonth;
    /**
     * 标准分
     */
    private Double score;
    /**
     * 得过的分
     */
    private List<Mark> scoreList;

    /**
     * 背景是否显示
     */
    private Integer isShow;

}
