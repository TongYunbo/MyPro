package com.tzcpa.model.treatment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 团队利润表信息
 * @author WangYao
 * 2019年5月21日
 */
@Data
@ToString
public class TeamProfitStatementDO implements Serializable {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 营业总收入
     */
    private Long grossRevenue;

    /**
     * 营业收入
     */
    private Long operationRevenue;

    /**
     * 营业总成本
     */
    private Long totalOperatingCost;

    /**
     * 营业成本
     */
    private Long operatingCost;

    /**
     * 税金及附加
     */
    private Long saleTaxAddict;

    /**
     * 销售费用
     */
    private Long sellingExpenses;

    /**
     * 管理费用
     */
    private Long administrativeCost;

    /**
     * 研发费用
     */
    private Long developmentExpenditure;

    /**
     * 财务费用
     */
    private Long financialCost;

    /**
     * 资产减值损失
     */
    private Long assetsImpairmentLoss;

    /**
     * 营业利润（亏损以“-”号填列）
     */
    private Long operatingProfit;

    /**
     * 加：营业外收入
     */
    private Long nonbusinessIncome;

    /**
     * 减：营业外支出
     */
    private Long nonbusinessExpenditure;

    /**
     * 利润总额（亏损总额以“-”号填列）
     */
    private Long totalProfit;

    /**
     * 减：所得税费用
     */
    private Long incomeTaxExpense;

    /**
     * 净利润（净亏损以“-”号填列）
     */
    private Long retainedProfits;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 团队id
     */
    private Integer teamId;

    /**
     * 最终材料成本
     */
    private Long finalMaterialCost;

    /**
     * 本年年末未分配利润
     */
    private  Long endYearUndistributedProfits;

    /**
     * '本年年初未分配利润'
     */
    private  Long beginYearUndistributedProfits;

    /**
     * 本年未分配利润增减变动金额
     */
    private  Long currentYearUndistributedProfitsChange;

    /**
     * 未分配利润-本年净利润
     */
    private  Long undistributedProfitsNetProfit;

    /**
     * 未分配利润-提取盈余公
     */
    private  Long undistributedProfitsWithdrawalSurplusReserves;

    /**
     * 未分配利润-对股东的分配
     */
    private  Long undistributedProfitsDistributionShareholders;

    /**
     * 未分配利润-其它
     */
    private  Long undistributedProfitsOther;


    private static final long serialVersionUID = 1L;

}