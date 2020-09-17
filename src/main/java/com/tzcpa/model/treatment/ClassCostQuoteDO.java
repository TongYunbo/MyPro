package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName ClassCostQuoteDO
 * @Description 班级成本引用
 * @Author hanxf
 * @Date 11:05 2019/5/17
**/
@Data
@ToString
public class ClassCostQuoteDO implements Serializable {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 车型
     */
    private String vehicleModel;

    /**
     * 销量区间 最小值
     */
    private Long salesRangeMin;

    /**
     * 销量区间 最大值
     */
    private Long salesRangeMax;

    /**
     * 单位成本
     */
    private Long unitCost;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 分组key
     */
    private String groupKey;

    private static final long serialVersionUID = 1L;

}