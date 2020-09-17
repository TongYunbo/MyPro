package com.tzcpa.model.treatment;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 班级固定费用中间表
 * @author WangYao
 * 2019年5月22日
 */
@Data
@ToString
public class ClassFixedChargeAmortizationDO implements Serializable {

	/**
     * 自增主键
     */
    private Integer id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 车型
     */
    private String vehicleModel;

    /**
     * 折旧费用
     */
    private Float depreciationCharge;
    
    /**
     * 生产折旧费用
     */
    private Float productionDepreciationCharge;

    /**
     * 生产工人工资分摊比例
     */
    private Float productionWorkersSalary;

    /**
     * 销售费用-宣传推广费分摊比例
     */
    private Float salesExpensesPromotionalExpenses;

    /**
     * 销售费用-售后服务费基准（收入）
     */
    private Float salesExpensesAfterSalesServiceFeeBenchmarkIncome;
    
    /**
     * 销售费用-其他
     */
    private Float salesExpensesOther;

    /**
     * 财务费用
     */
    private Float financialCost;

    /**
     * 填报的研发费分摊比例
     */
    private Float administrativeExpensesDevelopmentExpense;

    /**
     * 管理费用-折旧费
     */
    private Float administrativeExpensesDepreciationCharge;
    
    /**
     * 管理费用-其他基准（收入）
     */
    private Float administrativeExpensesOtherIncome;
    
    /**
     * 资产减值损失-坏账
     */
    private Float impairmentLossAssetsBadDebts;

    /**
     * 资产减值损失-存货
     */
    private Float impairmentLossAssetsInventory;

    /**
     * 资产减值损失-固定资产
     */
    private Float impairmentLossAssetsFixedAssets;

    /**
     * 营业外收入
     */
    private Float nonbusinessIncome;

    /**
     * 营业外支出
     */
    private Float nonbusinessExpenditure;

    /**
     * 班级id
     */
    private Integer classId;

    private static final long serialVersionUID = 1L;
}
