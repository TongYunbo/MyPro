package com.tzcpa.model.treatment;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
/**
 * 初始化固定值比例
 * @author WangYao
 * 2019年5月22日
 */
@Data
@ToString
public class ClassFixedParamDO implements Serializable {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 生产折旧费用
     */
    private Float productionDepreciationCharge;

    /**
     * 销售费用-售后服务费基准（收入）
     */
    private Float salesExpensesAfterSalesServiceFeeBenchmarkIncome;

    /**
     * 管理费用-折旧费
     */
    private Float administrativeExpensesDepreciationCharge;

    /**
     * 管理费用-其他基准（收入）
     */
    private Float administrativeExpensesOtherIncome;

    /**
     * 货币资金
     */
    private Float financialCostMonetaryCapital;

    /**
     * 短期借款
     */
    private Float financialCostShorttermBorrowing;

    /**
     * 基准能耗成本
     */
    private Float benchmarkEnergyCost;

    /**
     * 战略基准能耗成本
     */
    private Float strategyBenchmarkEnergyCost;

    /**
     * 销量调后-能耗成本
     */
    private Float salesAdjustedUnitEnergyCost;

    /**
     * 营业税金及附加（基准收入）
     */
    private Float benchmarkFinalOperatingTaxSurcharge;

    /**
     * 营业税金及附加（战略基准收入）
     */
    private Float strategyBenchmarkFinalOperatingTaxSurcharge;

    /**
     * 最终营业税金及附加
     */
    private Float finalOperatingTaxSurcharge;

    /**
     * 月度利润表 减：所得税费用
     */
    private Float incomeTaxExpense;
    
    /**
     * 未分配利润-提取盈余公积
     */
    private Float undistributedProfitsWithdrawalSurplusReserves;
    
    /**
     * 未分配利润-对股东的分配
     */
    private Float undistributedProfitsDistributionShareholders;
    
    /**
     * 未分配利润--其他
     */
    private Float undistributedProfitsOther;

    /**
     * 班级id
     */
    private Integer classId;

    private static final long serialVersionUID = 1L;
}