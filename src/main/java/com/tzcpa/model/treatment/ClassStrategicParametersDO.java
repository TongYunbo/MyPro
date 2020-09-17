package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 班级战略参数
 *
 * @Author hanxf
 * @Date 17:07 2019/5/16
**/
@Data
@ToString
public class ClassStrategicParametersDO implements Serializable {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 年份id
     */
    private Integer year;

    /**
     * 战略选择
     */
    private String strategicSelect;

    /**
     * 车型
     */
    private String vehicleModel;

    /**
     * 销量
     */
    private Float sales;

    /**
     * 单价
     */
    private Float unitPrice;

    /**
     * 成本加成
     */
    private Float costPlus;

    /**
     * 班级id
     */
    private Integer classId;


    private static final long serialVersionUID = 1L;

}