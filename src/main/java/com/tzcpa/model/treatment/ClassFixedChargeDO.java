package com.tzcpa.model.treatment;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
/**
 * 班级固定费用信息
 * @author WangYao
 * 2019年5月21日
 */
@Data
@ToString
public class ClassFixedChargeDO implements Serializable {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 年份 
     */
    private Integer year;

    /**
     * 折旧费用
     */
    private Long depreciationCharge;

    /**
     * 生产折旧费用
     */
    private Long productionDepreciationCharge;

    /**
     * 生产工人工资
     */
    private Long productionWorkersSalary;

    /**
     * 销售费用-宣传推广费
     */
    private Long salesExpensesPromotionalExpenses;

    /**
     * 销售费用-其他
     */
    private Long salesExpensesOther;

    /**
     * 管理费用-折旧费
     */
    private Long administrativeExpensesDepreciationCharge;

    /**
     * 资产减值损失-坏账
     */
    private Long impairmentLossAssetsBadDebts;

    /**
     * 资产减值损失-存货
     */
    private Long impairmentLossAssetsInventory;

    /**
     * 资产减值损失-固定资产
     */
    private Long impairmentLossAssetsFixedAssets;

    /**
     * 营业外收入
     */
    private Long nonbusinessIncome;

    /**
     * 营业外支出
     */
    private Long nonbusinessExpenditure;

    /**
     * 班级id
     */
    private Integer classId;

    private static final long serialVersionUID = 1L;
}