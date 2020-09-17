package com.tzcpa.model.treatment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName ClassIntermediateDO
 * @Description 班级中间表
 * @Author wangbj
 * @Description
 * @Date 2019/5/10
 **/
@Data
@ToString
@NoArgsConstructor
public class ClassIntermediateDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *自增主键
     */
    private Integer id;

    /**
     *班级id
     */
    private Integer classId;

    /**
     *年份
     */
    private Integer year;

    /**
     *月份
     */
    private Integer month;

    /**
     * 按照车型分组
     */
    private String vehicleModelGroup;

    /**
     *车型
     */
    private String vehicleModel;

    /**
     *基准销量 单位 辆
     */
    private Long benchmarkSales;

    /**
     *基准单价
     */
    private Long benchmarkUnitPrice;

    /**
     *基准销售额
     */
    private Long benchmarkSalesPrice;

    /**
     *基准单位材料成本
     */
    private Long benchmarkUnitMaterialCost;

    /**
     *基准材料成本合计
     */
    private Long benchmarkMaterialsCostTotal;

    public ClassIntermediateDO(Integer classId, Integer year, Integer month, String vehicleModel, Long benchmarkSales, Long benchmarkUnitPrice, Long benchmarkSalesPrice, Long benchmarkUnitMaterialCost, Long benchmarkMaterialsCostTotal) {
        this.classId = classId;
        this.year = year;
        this.month = month;
        this.vehicleModel = vehicleModel;
        this.benchmarkSales = benchmarkSales;
        this.benchmarkUnitPrice = benchmarkUnitPrice;
        this.benchmarkSalesPrice = benchmarkSalesPrice;
        this.benchmarkUnitMaterialCost = benchmarkUnitMaterialCost;
        this.benchmarkMaterialsCostTotal = benchmarkMaterialsCostTotal;
    }
}