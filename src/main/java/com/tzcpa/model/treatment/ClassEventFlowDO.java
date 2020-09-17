package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/***
 * @ClassName ClassEventFlowDO
 * @Description 班级事件流程Bean
 * @Author hanxf
 * @Description
 * @Date 15:01 2019/5/6
 **/
@Data
@ToString
public class ClassEventFlowDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 事件编号
     */
    private String eventCode;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件类型 题目:2 地图:3
     */
    private Integer eventType;

    /**
     * 事件显示顺序
     */
    private Integer eventOrder;

    /**
     * 是否显示1：是0：否
     */
    private Integer isShow;

    /**
     * 时间线
     */
    private Date timeLine;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 持续时长
     */
    private Integer duration;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 角色编码
     */
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleValue;

    /**
     * 积分满分值
     */
    private Integer fullMark;

    /**
     * 角色权重（基数为10）
     */
    private Integer weight;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 事件id
     */
    private Integer eventId;
    
    /**
     * 是否受战略影响0:不受影响，1:受影响
     */
    private Integer isStrategic;
    
    /**
     * 父级id
     */
    private Integer createTime;
}