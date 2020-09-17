package com.tzcpa.model.treatment;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
/**
 * 班级资产负债表信息
 * @author WangYao
 * 2019年5月21日
 */
@Data
@ToString
public class ClassBalanceSheetDO implements Serializable {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 流动资金
     */
    private Long currentAssets;

    /**
     * 货币资金
     */
    private Long monetaryCapital;

    /**
     * 应收票据及应收账款
     */
    private Long notesAccountsReceivable;

    /**
     * 预付款项
     */
    private Long prepayment;

    /**
     * 其他应收款
     */
    private Long otherReceivables;

    /**
     * 存货
     */
    private Long inventory;

    /**
     * 原材料
     */
    private Long rawMaterial;

    /**
     * 库存商品(产成品)
     */
    private Long finishedGoods;

    /**
     * 流动资产合计
     */
    private Long totalCurrentAssets;

    /**
     * 非流动资产
     */
    private Long noncurrentAssets;

    /**
     * 长期股权投资
     */
    private Long longTermEquityInvestment;

    /**
     * 固定资产原价
     */
    private Long originalValueFixedAssets;

    /**
     * 累计折旧
     */
    private Long accumulatedDepreciation;

    /**
     * 固定资产净值
     */
    private Long netValueFixedAssets;

    /**
     * 固定资产减值准备
     */
    private Long fixedAssetsDepreciationReserves;

    /**
     * 固定资产净额
     */
    private Long netFixedAssets;

    /**
     * 在建工程
     */
    private Long constructionInProcess;

    /**
     * 无形资产
     */
    private Long intangibleAssets;

    /**
     * 发放贷款及款项
     */
    private Long makingLoansPayments;

    /**
     * 递延所得税资产
     */
    private Long deferredTaxAssets;

    /**
     * 其他非流动资产
     */
    private Long otherNoncurrentAssets;

    /**
     * 非流动资产合计
     */
    private Long totalNoncurrentAssets;

    /**
     * 资产总计
     */
    private Long totalAssets;

    /**
     * 流动负债
     */
    private Long currentLiabilities;

    /**
     * 流动负债
     */
    private Long shortTermBorrowing;

    /**
     * 应付票据及应付账款
     */
    private Long notesAccountsPayable;

    /**
     * 预收款项
     */
    private Long unearnedRevenue;

    /**
     * 应付职工薪酬
     */
    private Long payrollPayable;

    /**
     * 应交税费
     */
    private Long taxPayable;

    /**
     * 其他应付款
     */
    private Long otherPayables;

    /**
     * 一年内到期的非流动负债
     */
    private Long noncurrentLiabilitiesMatureOneYear;

    /**
     * 其他流动负债
     */
    private Long otherCurrentLiability;

    /**
     * 流动负债合计
     */
    private Long totalCurrentLiability;

    /**
     * 非流动负债
     */
    private Long noncurrentLiability;

    /**
     * 长期借款
     */
    private Long longTermLoan;

    /**
     * 应付债券
     */
    private Long bondsPayable;

    /**
     * 长期应付款
     */
    private Long longTermPayable;

    /**
     * 长期应付职工薪酬
     */
    private Long longTermCompensationEmployees;

    /**
     * 预计负债
     */
    private Long anticipationLiabilities;

    /**
     * 递延收益
     */
    private Long deferredIncome;

    /**
     * 递延所得税负债
     */
    private Long deferredIncomeTaxLiabilities;

    /**
     * 其他非流动负债
     */
    private Long otherNoncurrentLiabilities;

    /**
     * 非流动负债合计
     */
    private Long totalNoncurrentLiabilities;

    /**
     * 负债合计
     */
    private Long totalLiability;

    /**
     * 所有者权益
     */
    private Long ownershipInterest;

    /**
     * 股本
     */
    private Long capitalization;

    /**
     * 资本公积
     */
    private Long capitalReserve;

    /**
     * 盈余公积
     */
    private Long earnedSurplus;

    /**
     * 未分配利润
     */
    private Long undistributedProfit;

    /**
     * 所有者权益合计
     */
    private Long totalOwnersEquity;

    /**
     * 负债和所有者权益总计
     */
    private Long totalLiabilitiesEquity;

    /**
     * 班级ID
     */
    private Integer classId;

    private static final long serialVersionUID = 1L;
}