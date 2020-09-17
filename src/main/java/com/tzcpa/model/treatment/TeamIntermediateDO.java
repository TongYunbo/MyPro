package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName
 * @Description
 * @Author wangbj
 * @Description
 * @Date 2019/5/10
 **/
@Data
@ToString
public class TeamIntermediateDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 团队id
     */
    private Integer teamId;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 车型
     */
    private String vehicleModel;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 战略选择
     */
    private String strategySelect;

    /**
     * 基准销量 单位 辆
     */
    private Long benchmarkSales;

    /**
     * 基准单价 单位分
     */
    private Long benchmarkUnitPrice;

    /**
     * 基准销售额
     */
    private Long benchmarkSalesPrice;

    /**
     * 基准单位材料成本
     */
    private Long benchmarkUnitMaterialCost;

    /**
     * 基准材料成本合计
     */
    private Long benchmarkMaterialsCostTotal;

    /**
     * 战略基准销量
     */
    private Long strategyBenchmarkSales;

    /**
     * 战略基准单价
     */
    private Long strategyBenchmarkUnitPrice;

    /**
     * 战略基准销售额
     */
    private Long strategyBenchmarkSalesPrice;

    /**
     * 战略基准单位材料成本
     */
    private Long strategyBenchmarkUnitMaterialCost;

    /**
     * 战略基准材料成本合计
     */
    private Long strategyBenchmarkMaterialsCostTotal;

    /**
     * 调整后销量
     */
    private Long adjustedSales;

    /**
     * 调整后单价
     */
    private Long adjustedUnitPrice;

    /**
     * 最终销售额
     */
    private Long finalSales;

    /**
     * 销量调后-单位材料成本
     */
    private Long salesAdjustedUnitMaterialCost;

    /**
     * 调整后单位材料成本
     */
    private Long adjustedUnitMaterialCost;

    /**
     * 最终材料成本
     */
    private Long finalMaterialCost;

    /**
     * 生产折旧费用
     */
    private Long productionDepreciationExpense;

    /**
     * 最终生产折旧
     */
    private Long finalProductionDepreciationExpense;
    
    /**
     * 基准能耗成本
     */
    private Long benchmarkEnergyCost;
    
    /**
     * 战略基准能耗成本
     */
    private Long strategyBenchmarkEnergyCost;
    
    /**
     * 销量调后-能耗成本
     */
    private Long salesAdjustedUnitEnergyCost;
    
    /**
     * 最终能耗成本
     */
    private Long finalEnergyCost;

    /**
     * 生产人工成本
     */
    private Long productiveLaborCost;
    
    /**
     * 最终生产人工成本
     */
    private Long finalProductiveLaborCost;
    
    /**
     * 基准营业成本加总
     */
    private Long benchmarkOperatingCostPlus;
    
    /**
     * 战略基准营业成本加总
     */
    private Long strategyBenchmarkOperatingCostPlus;
    
    /**
     * 最终营业成本合计
     */
    private Long finalOperatingCostPlus;

    /**
     * 基准毛利率
     */
    private Float benchmarkGrossMargin;
    
    /**
     * 战略基准毛利率
     */
    private Float strategyBenchmarkGrossMargin;
    
    /**
     * 最终毛利率
     */
    private Float finalGrossMargin;
    
    /**
     * 营业税金及附加（基准收入）
     */
    private Long benchmarkFinalOperatingTaxSurcharge;
    
    /**
     * 营业税金及附加（战略基准收入）
     */
    private Long strategyBenchmarkFinalOperatingTaxSurcharge;

    /**
     * 最终营业税金及附加
     */
    private Long finalOperatingTaxSurcharge;

    /**
     * 销售费用-宣传推广费
     */
    private Long salesPromotionalExpenses;

    /**
     * 销售费用-售后服务费
     */
    private Long salesAfterSalesServiceFee;
    
    /**
     * 调整后销售费用-售后服务费
     */
    private Long adjustedSalesAfterSalesServiceFee;

    /**
     * 销售费用-其他
     */
    private Long salesOther;

    /**
     * 销售费用合计
     */
    private Long salesTotal;

    /**
     * 调整宣传推广费
     */
    private Long adjustedPromotionalExpenses;
    
    /**
     * 最终宣传推广费
     */
    private Long finalPromotionalExpenses;
    
    /**
     * 调整售后服务费
     */
    private Long adjustedAftersalesServiceFee;
    
    /**
     * 最终售后服务费
     */
    private Long finalAftersalesServiceFee;
    
    /**
     * 调整销售费用-其他
     */
    private Long adjustedSalesOther;
    
    /**
     * 最终销售费用-其他
     */
    private Long finalSalesOther;
    
    /**
     * 最终销售费用合计
     */
    private Long finalSalesTotal;
    
    /**
     * 管理费用-研发费
     */
    private Long managementDevelopmentCost;

    /**
     * 调整后管理费用-研发费
     */
    private Long adjustedManagementDevelopmentCost;
    
    /**
     * 管理费用-折旧费
     */
    private Long managementDepreciationCost;

    /**
     * 管理费用-其他
     */
    private Long managementOther;
    
    /**
     * 填报的研发费用
     */
    private Long inDevelopmentCost;
    
    /**
     * 调整管理费用-折旧费
     */
    private Long adjustedManagementDepreciation;
    
    /**
     * 调整后管理费用-其他
     */
    private Long adjustedManagementOther;

    /**
     * 管理费用合计
     */
    private Long managementTotal;

    /**
     * 最终管理费用合计
     */
    private Long finalManagementTotal;
    
    /**
     * 财务费用
     */
    private Long financialCost;
    
    /**
     * 调整财务费用
     */
    private Long adjustedFinancialCost;
    
    /**
     * 最终财务费用
     */
    private Long finalFinancialCost;
    
    /**
     * 资产减值损失-坏账
     */
    private Long assetsImpairmentLossBadDebt;
    
    /**
     * 调整资产减值损失-坏账
     */
    private Long adjustedAssetsImpairmentLossBadDebt;
    
    /**
     * 最终资产减值损失-坏账
     */
    private Long finalAssetsImpairmentLossBadDebt;

    /**
     * 资产减值损失-存货
     */
    private Long assetsImpairmentLossInventory;
    
    /**
     * 
     */
    private Long adjustedAssetsImpairmentLossInventory;
    
    /**
     * 最终资产减值损失-存货
     */
    private Long finalAssetsImpairmentLossInventory;
    
    /**
     * 资产减值损失-固定资产
     */
//    private Long adjustedAssetsImpairmentLossFixedAssets;
    private Long assetsImpairmentLossFixedAssets;
    
    /**
     * 调整资产减值损失-固定资产
     */
    private Long finalAssetsImpairmentLossFixedAssets;
    
    /**
     * 最终资产减值损失-固定资产
     */
    private Long finalAssetsImpairmentLossTotal;
    
    /**
     * 最终资产减值损失合计
     */
    private Long nonbusinessIncome;
    
    /**
     * 营业外收入
     */
    private Long adjustedNonbusinessIncome;
    
    /**
     * 调整营业外收入
     */
    private Long finalNonbusinessIncome;
    
    /**
     * 最终营业外收入
     */
    private Long nonbusinessExpenditure;
    
    /**
     * 营业外支出
     */
    private Long adjustedNonbusinessExpenditure;
    
    /**
     * 调整营业外支出
     */
    private Long finalNonbusinessExpenditure;

    /**
     * 最终营业外支出
     */
    private String ymDate;
    
    /**
     * 其他市场份额
     */
    private Long otherMarketShares;
    
    /**
     * 按照车型分组
     */
    private String vehicleModelGroup;

}