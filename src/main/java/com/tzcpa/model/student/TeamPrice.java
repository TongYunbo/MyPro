package com.tzcpa.model.student;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 团队价格列表信息
 * @author WangYao
 * 2019年6月3日
 */
@Data
@ToString
public class TeamPrice implements Serializable{

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
     * 基准单价 单位分
     */
    private Long benchmarkUnitPrice;
    
    /**
     * 调整后单价
     */
    private Long adjustedUnitPrice;
}
