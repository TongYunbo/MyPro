package com.tzcpa.model.student;

import lombok.Data;

/**
 * @ClassName AnnualAnalysisDO
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/31 9:39
 * @Version 6.0
 **/
@Data
public class AnnualAnalysisDO {

    /**
     * 净资产收益率
     */
    private Double returnOnEqu;

    /**
     * 净利润
     */
    private Long netProfit;

    /**
     * 营业收入
     */
    private Long revenue;

    /**
     * 每股净资产
     */
    private Long netAssetPerShare;

    /**
     * 每股收益
     */
    private Long earningPerShare;

    /**
     * 销售毛利
     */
    private Long  salesMargin;

    /**
     * 销售毛利率
     */
    private Double salesMarginRat;

    /**
     * 销售净利率
     */
    private Double salesNetRat;

    /**
     * 营业利润率
     */
    private Double opeProfRatio;

    /**
     * 每股收益同比增长率
     */
    private Double earningPerShareRat;

    /**
     * 营业收入同比增长率
     */
    private Double growthOpeRevenue;

    /**
     * 净利润同比增长率
     */
    private Double growthNetProRat;

    /**
     * 净资产收益率同比增长率
     */
    private Double growthNetAssetRat;

    /**
     * 存货周转率
     */
    private Double  stockTurnoverRat ;

    /**
     * 总资产周转率
     */
    private Double assetTurnoverRat;

    /**
     * 总资产
     */
    private Long asset;

    /**
     * 净资产
     */
    private Long assetNet;

    /**
     * 资产负债率
     */
    private Double assetDebtRat;

    /**
     * 流动比率
     */
    private Double currentRat;

    /**
     * 速动比率
     */
    private Double quickRat;

    /**
     * 销量增长率
     */
    private Double salesGrowthRat;

    /**
     * 营业收入构成-轿车
     */
    private Double revenueCompositionCar;

    /**
     * 营业收入构成-H6
     */
    private Double revenueCompositionH6;

    /**
     * 营业收入构成-皮卡
     */
    private Double revenueCompositionPick;

    /**
     * 营业收入构成-WEY
     */
    private Double revenueCompositionWEY;

    /**
     * 营业收入构成-H8
     */
    private Double revenueCompositionH8;

    /**
     * 净利润构成-轿车
     */
    private Double netPompositionCar;

    /**
     * 净利润构成-H6
     */
    private Double netPompositionH6;

    /**
     * 净利润构成-皮卡
     */
    private Double netPompositionPick;

    /**
     * 净利润构成-WEY
     */
    private Double netPompositionWEY;

    /**
     * 净利润构成-H8
     */
    private Double netPompositionH8;

    /**
     * 研发费用
     */
    private Long  devExpenditure ;

    /**
     * 研发费用投入占当年营收比
     */
    private Long devExpenditureRat;

    /**
     * H6市场份额占比
     */
    private Long marketShareH6;

    /**
     * H8市场份额占比
     */
    private Long marketShareH8;

    /**
     * WEY市场份额占比
     */
    private Long marketShareWEY;

    /**
     * 新产品贡献率
     */
    private Double contOfNewProduc;



}
