package com.tzcpa.model.student;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 单车材料成本列表信息
 * @author WangYao
 * 2019年6月3日
 */
@Data
@ToString
public class SingleMaterialCost implements Serializable{

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
     * 基准单位材料成本
     */
    private Long benchmarkUnitMaterialCost;
    
    /**
     * 调整后单位材料成本
     */
    private Long adjustedUnitMaterialCost;
}
