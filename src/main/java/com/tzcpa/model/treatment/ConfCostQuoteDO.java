package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName ConfCostQuoteDO
 * @Description 成本引用
 * @Author hanxf
 * @Date 2019/5/15
 **/
@Data
@ToString
public class ConfCostQuoteDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *自增主键
     */
    private Integer id;

    /**
     *年份
     */
    private Integer year;

    /**
     *级别
     */
    private Integer level;

    /**
     *车型
     */
    private String vehicleModel;

    /**
     *销量区间
     */
    private Long salesRange;

    /**
     *单位成本
     */
    private Long unitCost;

    /**
     * 分组key
     */
    private String groupKey;

}