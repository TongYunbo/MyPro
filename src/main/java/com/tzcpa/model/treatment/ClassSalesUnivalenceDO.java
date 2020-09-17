package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 班级基础销量单价表
 *
 * @Author hanxf
 * @Date 11:13 2019/5/17
**/

@Data
@ToString
public class ClassSalesUnivalenceDO implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 车型
     */
    private String vehicleModel;

    /**
     * 基准销量
     */
    private Long benchmarkSales;

    /**
     * 基准单价
     */
    private Long benchmarkUnitPrice;

    /**
     * 基准销售额
     */
    private Long benchmarkSalesPrice;

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