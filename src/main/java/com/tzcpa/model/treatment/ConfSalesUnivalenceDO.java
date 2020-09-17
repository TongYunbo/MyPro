package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
/**
 * 基础销量单价表
 *
 * @Author hanxf
 * @Date 17:33 2019/5/15
**/
@Data
@ToString
public class ConfSalesUnivalenceDO implements Serializable {
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
     * 分组key
     */
    private String groupKey;

    private static final long serialVersionUID = 1L;


}