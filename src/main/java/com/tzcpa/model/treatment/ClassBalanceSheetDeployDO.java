package com.tzcpa.model.treatment;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 班级资产负债表配置信息
 * @author WangYao
 * 2019年5月21日
 */
@Data
@ToString
public class ClassBalanceSheetDeployDO implements Serializable {
    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 流动资产
     */
    private Long currentAssets;

    /**
     * 应收票据及应收账款
     */
    private Double notesAccountsReceivable;

    /**
     * 预付款项
     */
    private Double prepayment;

    /**
     * 其他应收款
     */
    private Long otherReceivables;

    /**
     * 存货
     */
    private Double inventory;

    /**
     * 原材料
     */
    private Double rawMaterial;

    /**
     * 库存商品(产成品)
     */
    private Double finishedGoods;

    /**
     * 其他流动资产
     */
    private Double otherCurrentAssets;

    /**
     * 非流动资产
     */
    private Double noncurrentAssets;

    /**
     * 长期股权投资
     */
    private Float longTermEquityInvestment;

    /**
     * 固定资产原价
     */
    private Long originalValueFixedAssets;

    /**
     * 累计折旧
     */
    private Long accumulatedDepreciation;

    /**
     * 固定资产减值准备
     */
    private Long fixedAssetsDepreciationReserves;

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
    private Float makingLoansPayments;

    /**
     * 商誉
     */
    private Float businessReputation;

    /**
     * 递延所得税资产
     */
    private Float deferredTaxAssets;

    /**
     * 其他非流动资产
     */
    private Float otherNoncurrentAssets;

    /**
     * 流动负债
     */
    private Float currentLiabilities;

    /**
     * 短期借款
     */
    private Long shortTermBorrowing;

    /**
     * 应付票据及应付账款
     */
    private Float notesAccountsPayable;

    /**
     * 预收款项
     */
    private Float unearnedRevenue;

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
    private Float noncurrentLiabilitiesMatureOneYear;

    /**
     * 其他流动负债
     */
    private Float otherCurrentLiability;

    /**
     * 非流动负债
     */
    private Float noncurrentLiability;

    /**
     * 长期借款
     */
    private Float longTermLoan;

    /**
     * 应付债券
     */
    private Float bondsPayable;

    /**
     * 长期应付款
     */
    private Float longTermPayable;

    /**
     * 长期应付职工薪酬
     */
    private Float longTermCompensationEmployees;

    /**
     * 预计负债
     */
    private Float anticipationLiabilities;

    /**
     * 递延收益
     */
    private Long deferredIncome;

    /**
     * 递延所得税负债
     */
    private Float deferredIncomeTaxLiabilities;

    /**
     * 其他非流动负债
     */
    private Float otherNoncurrentLiabilities;

    /**
     * 所有者权益
     */
    private Float ownershipInterest;

    /**
     * 股本
     */
    private Long capitalization;

    /**
     * 资本公积
     */
    private Long capitalReserve;

    /**
     * 班级id
     */
    private Integer classId;

    private static final long serialVersionUID = 1L;
}