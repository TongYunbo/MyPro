package com.tzcpa.model.treatment;

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
public class ClassQuestionDescDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 事件名称
     */
    private String questionTitle;

    /**
     * 问题描述
     */
    private String questionDesc;

    /**
     * 问题出现的年份
     */
    private Integer questionYear;

    /**
     * 问题类型 填空题：1单选题：2
     */
    private Integer questionType;

    /**
     * 本题的做题时间单位分
     */
    private Integer duration;

    /**
     * 背景描述
     */
    private String backgroundDesc;

    /**
     * 事件编码
     */
    private Integer eventId;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 是否展示  显示：0 背景禁用：1
     */
    private Integer isShow;

    /**
     * 父级id
     */
    private Integer parentId;
    
    /**
     * 问题id
     */
    private Integer questionId;
    
    /**
     * 是否随机
     */
    private Integer is_random;
    
    /**
     * 几个选项
     */
    private Integer severalOptions;
    
    /**
     * 选几个
     */
    private Integer chooseAFew;
    
    /**
     * 是否开启随机 0：开启 1：关闭   默认开启状态
     */
    private Integer turnOnRandomization;

}