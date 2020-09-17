package com.tzcpa.model.student;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName MonthlySales
 * @Description
 * @Author hanxf
 * @Date 2019/5/30 19:01
 * @Version 1.0
 **/
@Data
@ToString
public class MonthlySales implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 车型
	 */
	private String vehicleModel;

	/**
	 * 月份
	 */
    private Integer month;
    
    /**
     * 车型
     */
    private Integer year;

    /**
     * 调整后销量
     */
    private Long adjustedSales;

    /**
     * 战略基准销量 单位 辆
     */
    private Long strategyBenchmarkSales;
}
