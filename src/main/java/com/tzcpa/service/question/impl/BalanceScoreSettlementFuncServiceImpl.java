package com.tzcpa.service.question.impl;

import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.VNCConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.model.question.BalanceScoreSettlementDO;

import java.util.HashMap;
import java.util.Map;

/**
 * wangbj 平衡积分卡 实际值计算
 *
 * @date 2019年5月30日
 */
public class BalanceScoreSettlementFuncServiceImpl {


    // ========== 平衡积分卡 H6

    /**
     * 营业利润率（营业利润/营业收入）
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 营业利润率
     */
    public Double profitMargin(BalanceScoreSettlementDO balanceScore) {
    	Map<String, Object> param = getClassTeamYear(balanceScore);
        param.put("vehicleModel", NormalConstant.VEHICLE_MODEL_H6);
        Map<String, Object> finalValue = balanceScore.getTeamIntermediateMapper().selectTeamIntermediateYearSum(param);
        // H8最终销售额
        double sales = Double.parseDouble(finalValue.get("sales").toString());
        // H8最终营业成本合计
        double operatingCostPlus = Double.parseDouble(finalValue.get("operatingCostPlus").toString());
        // H8最终营业税金及附加
        double operatingTaxSurcharge = Double.parseDouble(finalValue.get("operatingTaxSurcharge").toString());
        // H8最终销售费用合计
        double salesTotal = Double.parseDouble(finalValue.get("salesTotal").toString());
        // H8最终管理费用合计
        double managementTotal = Double.parseDouble(finalValue.get("managementTotal").toString());
        // H8最终财务费用
        double financialCost = Double.parseDouble(finalValue.get("financialCost").toString());
        // H8最终资产减值损失合计
        double lossTotal = Double.parseDouble(finalValue.get("lossTotal").toString());

        double sbTemp = operatingCostPlus + operatingTaxSurcharge + salesTotal + managementTotal + financialCost + lossTotal;
        return (sales - sbTemp) / sales * 100;
    }

    /**
     * 总资产周转率（本年销售收入/平均总资产）
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 总资产周转率
     */
    public Double turnoverRate(BalanceScoreSettlementDO param) {
        //年度利润表中营业收入
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        Double operationRevenue = param.getBaJsMapper().findOperationRevenue(param, year);
        //资产负债本年总资产
        Double totalAssets = param.getBaJsMapper().findTotalAssets(param, year);
        //资产负债去年总资产
        Double lastTotalAssets = param.getBaJsMapper().findLastTotalAssets(param, year);
        Double averageTotalAssets = (totalAssets + lastTotalAssets) / 2;
        return operationRevenue / averageTotalAssets;
    }

    /**
     * @param param 平衡积分卡 实际值计算用参数
     * @return 年度利润表净利润
     */
    public Double netProfit(BalanceScoreSettlementDO balanceScore) {
    	Map<String, Object> param = getClassTeamYear(balanceScore);
        param.put("vehicleModel", NormalConstant.VEHICLE_MODEL_H6);
        Map<String, Object> finalValue = balanceScore.getTeamIntermediateMapper().selectTeamIntermediateYearSum(param);
        // H8最终销售额
        double sales = Double.parseDouble(finalValue.get("sales").toString());
        // H8最终营业成本合计
        double operatingCostPlus = Double.parseDouble(finalValue.get("operatingCostPlus").toString());
        // H8最终营业税金及附加
        double operatingTaxSurcharge = Double.parseDouble(finalValue.get("operatingTaxSurcharge").toString());
        // H8最终销售费用合计
        double salesTotal = Double.parseDouble(finalValue.get("salesTotal").toString());
        // H8最终管理费用合计
        double managementTotal = Double.parseDouble(finalValue.get("managementTotal").toString());
        // H8最终财务费用
        double financialCost = Double.parseDouble(finalValue.get("financialCost").toString());
        // H8最终资产减值损失合计
        double lossTotal = Double.parseDouble(finalValue.get("lossTotal").toString());
        // H8最终营业外收入
        double finalNonbusinessIncome = Double.parseDouble(finalValue.get("finalNonbusinessIncome").toString());
        // H8最终营业外支出
        double finalNonbusinessExpenditure = Double.parseDouble(finalValue.get("finalNonbusinessExpenditure").toString());

        double result = sales - operatingCostPlus - operatingTaxSurcharge - salesTotal - managementTotal;
        result = result - financialCost - lossTotal + finalNonbusinessIncome - finalNonbusinessExpenditure;
        return result * 0.85;
    }

    /**
     * 新产品溢价率（本年平均售价/本年战略基准平均售价-1）
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 新产品溢价率
     */
    public Double premiumRate(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        return param.getBaJsMapper().findPremiumRate(param, year) * 100;
    }

    /**
     * 单车成本下降率（1-本年实际单车成本/本年战略基准成本给定）
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 单车成本下降率
     */
    public Double costReductionRate(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        return param.getBaJsMapper().findCostReductionRate(param, year) * 100;
    }

    /**
     * 新产品品类占有率（h6销量/总销量）
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 新产品品类占有率
     */
    public Double occupancyRate(BalanceScoreSettlementDO param) {
        //h6本年销量
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        int h6Sale = param.getBaJsMapper().findH6Sale(param, year);
        //全车型本年销量
        int allSale = param.getBaJsMapper().findAllSale(param, year);
        Double rate = Double.valueOf(h6Sale) / Double.valueOf(allSale) * 100;
        return rate;
    }

    /**
     * param，取变量XQCP的值
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return XQCP的值
     */
    public Double xQCP(BalanceScoreSettlementDO param) {
        String XQCP = param.getBaJsMapper().findXQCP(param);
        return Double.valueOf(XQCP);
    }

    /**
     * 4P营销题中取变量4PKHMYDH6值
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 4PKHMYDH6的值
     */
    public Double fPKHMYDH6(BalanceScoreSettlementDO param) {
        String fPKHMYDH6 = param.getBaJsMapper().findFPKHMYDH6(param);
        return Double.valueOf(fPKHMYDH6);
    }

    /**
     * 4P营销题中取变量4PJGMYDH6值。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 4PJGMYDH6的值
     */
    public Double fPJGMYDH6(BalanceScoreSettlementDO param) {
        String fPJGMYDH6 = param.getBaJsMapper().findFPJGMYDH6(param);
        return Double.valueOf(fPJGMYDH6);
    }

    /**
     * 运营产能题中取变量YYLCYH值。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return YYLCYH值
     */
    public Double yYLCYH(BalanceScoreSettlementDO param) {
        String yYLCYH = param.getBaJsMapper().findYylcyh(param);
        return Double.valueOf(yYLCYH);
    }

    /**
     * 研发费用降低率（1-（上年研发投入/上上年研发投入）*100%）
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 研发费用降低率
     */
    public Double yFfjd(BalanceScoreSettlementDO param) {
        //本年研发费用
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        Double yValue = param.getBaJsMapper().findYvalue(param, year - 1);
        //上一年研发费用
        Double yLastValue = 0.0;
        if (year.equals(2011) || year.equals(2012)) {
            yLastValue = param.getBaJsMapper().findTBYF(year - 2);
            if (year.equals(2011)) {
            	yValue = param.getBaJsMapper().findTBYF(year - 1);
			}
        } else {
            yLastValue = param.getBaJsMapper().findYvalue(param, year - 2);
        }

        Double yFfjd = 0.0;
        if (yLastValue.equals(0.0)) {
            yFfjd = 0.0;
        } else {
            yFfjd = Double.valueOf((1 - (yValue / yLastValue)) * 100);
        }
        return yFfjd;
    }

    /**
     * 新产品贡献率（H6收入/总收入）
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 新产品贡献率
     */
    public Double contributionRate(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        //查询H6的本年销售额
        Double saleH6 = param.getBaJsMapper().findSalesH6(param, year);
        //查询所有车型的本年销售额
        Double saleAll = param.getBaJsMapper().findSalesAll(param, year);
        return Double.valueOf(saleH6 / saleAll * 100);
    }

    /**
     * 修改前：研发费用占收入比(本年填报研发费用/本年总收入)
     * 修改后：取做填报研发费用中的YFYX WTL 2019-06-21
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 研发费用占收入比
     */
    public Double yFfyzb(BalanceScoreSettlementDO param) {
        //本年研发费用
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
//        Double yValue = param.getBaJsMapper().findYvalue(param, year);
//        //利润表中的营业收入
//        Long yySr = param.getBaJsMapper().findYysr(param, year);
//        return Double.valueOf(yValue / yySr * 100);

        //如果为2011年则使用配置的2010年的YFYX
        if (year.equals(Integer.valueOf(NormalConstant.YEAR_2011))) {
            return Double.valueOf(param.getOsMapper().selectConfVariable(VNCConstant.YFFYZSRB_2010));
        }
        //如果为非2011年则取上一年做研发费用投入时的入库H6 YFYX值
        return Double.valueOf(param.getBaJsMapper().getRdvValue(param.getClassId(), param.getTeamId(), VNCConstant.YFFYZSRB, year - 1));
    }

    /**
     * 每年研发费用投入题变量YFFX值。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return YFFX值
     */
    public Double yFfx(BalanceScoreSettlementDO param) {
        String yFfx = param.getBaJsMapper().findYffx(param);
        return Double.valueOf(yFfx);
    }

    //企业文化建设活动数量
    public Double qYwenh(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        Double qYwenh = param.getBaJsMapper().findQywenH(param, year);
        if (qYwenh == null) {
            qYwenh = 8d;
        }
        return qYwenh;
    }

    /**
     * 产品平均售价增长率（今年平均单价/去年平均单价）-1。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 产品平均售价增长率
     */
    public Double productRate(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        //本年所有车型最终销售额合计
        double finalSales = param.getBaJsMapper().findFinalSales(param, year);
        //本年所有车型调整后销量
        double adjustedSales = param.getBaJsMapper().findAdjustedSales(param, year);
        //去年所有车型最终销售额合计
        /*Double finalLastSales = param.getBaJsMapper().findFinalLastSales(param, year);
        if (finalLastSales == null) {
            finalLastSales = 100000d;
        }
        //去年所有车型调整后销量
        Double adjustedLastSales = param.getBaJsMapper().findAdjustedLastSales(param, year);
        if (adjustedLastSales == null) {
            adjustedLastSales = 1d;
        }*/
        //2010年车辆单价 5.976777万元
        Double DJ = param.getBaJsMapper().findDj();

        return Double.valueOf(((finalSales / adjustedSales) / (DJ) - 1) * 100);
    }

    /**
     * 培训体系建设题中取变量PXH6值。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return PXH6值
     */
    public Double pXH6(BalanceScoreSettlementDO param) {
        String pXH6 = param.getBaJsMapper().findPxH6(param);
        return Double.valueOf(pXH6);
    }

    /**
     * 关键人才引进题中取变量GJRCH6值。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return GJRCH6值
     */
    public Double gJRCH6(BalanceScoreSettlementDO param) {
        String gJRCH6 = param.getBaJsMapper().findGjrH6(param);
        return Double.valueOf(gJRCH6);
    }

    /**
     * 组织结构调整题中取变量ZZJGH6值。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return ZZJGH6值
     */
    public Double zZJGH6(BalanceScoreSettlementDO param) {
        String zZJGH6 = param.getBaJsMapper().findZzJGH6(param);
        return Double.valueOf(zZJGH6);
    }

    //品牌建设推广活动数量
    public Double pJSTG(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        Double pJSTG = param.getBaJsMapper().findPJTG(param, year);
        if (pJSTG == null) {
            pJSTG = 6d;
        }
        return pJSTG;
    }

    /**
     * 取自hseRequest题变量FXSBH6。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return FXSBH6值
     */
    public Double fXSBH6(BalanceScoreSettlementDO param) {
        String fXSBH6 = param.getBaJsMapper().findFxSBH6(param);
        if (fXSBH6 == null) {
            fXSBH6 = "80";
        }
        return Double.valueOf(fXSBH6);
    }

    // ========== 平衡积分卡 H8

    /**
     * (H8最终销售额-
     * (H8最终营业成本合计+H8最终营业税金及附加+H8最终销售费用合计+H8最终管理费用合计+H8最终财务费用+H8最终资产减值损失合计)
     * )/H8最终销售额
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return H8营业利润率
     */
    public Double h8OperatingProfit(BalanceScoreSettlementDO balanceScore) {
        Map<String, Object> param = getClassTeamYear(balanceScore);
        param.put("vehicleModel", NormalConstant.VEHICLE_MODEL_H8);
        Map<String, Object> finalValue = balanceScore.getTeamIntermediateMapper().selectTeamIntermediateYearSum(param);
        // H8最终销售额
        double sales = Double.parseDouble(finalValue.get("sales").toString());
        // H8最终营业成本合计
        double operatingCostPlus = Double.parseDouble(finalValue.get("operatingCostPlus").toString());
        // H8最终营业税金及附加
        double operatingTaxSurcharge = Double.parseDouble(finalValue.get("operatingTaxSurcharge").toString());
        // H8最终销售费用合计
        double salesTotal = Double.parseDouble(finalValue.get("salesTotal").toString());
        // H8最终管理费用合计
        double managementTotal = Double.parseDouble(finalValue.get("managementTotal").toString());
        // H8最终财务费用
        double financialCost = Double.parseDouble(finalValue.get("financialCost").toString());
        // H8最终资产减值损失合计
        double lossTotal = Double.parseDouble(finalValue.get("lossTotal").toString());

        double sbTemp = operatingCostPlus + operatingTaxSurcharge + salesTotal + managementTotal + financialCost + lossTotal;
        return (sales - sbTemp) / sales * 100;
    }

    /**
     * 利润表中的营业收入/资产负债表中的资产总计(去年和今年两年平均)
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 总资产周转率
     */
    public Double assetTurnoverRate(BalanceScoreSettlementDO balanceScore) {
        Map<String, Object> param = getClassTeamYear(balanceScore);
        Map<String, Object> teamProfitCurr = balanceScore.getTeamProfitStatementMapper().selectTeamProfitStatement(param);
        // 营业收入
        double operationRevenue = Double.parseDouble(teamProfitCurr.get("operationRevenue").toString());

        // 资产总计
        Long TotalAssetsCurr = balanceScore.getTeamBalanceSheetMapper().selectTotalAssets(param);
        param.put("year", Integer.parseInt(param.get("year").toString()) - 1);
        Long TotalAssetsFront = balanceScore.getTeamBalanceSheetMapper().selectTotalAssets(param);

        return operationRevenue / ((TotalAssetsCurr + TotalAssetsFront) / 2);
    }

    /**
     * H8销售收入/H8战略初始化销售收入
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 销售目标达成率
     */
    public Double goalAchievementRate(BalanceScoreSettlementDO balanceScore) {
        Map<String, Object> param = getClassTeamYear(balanceScore);
        param.put("vehicleModel", NormalConstant.VEHICLE_MODEL_H8);
        Map<String, Object> finalValue = balanceScore.getTeamIntermediateMapper().selectTeamIntermediateYearSum(param);
        // H8销售收入
        double sales = Double.parseDouble(finalValue.get("sales").toString());
        // H8战略初始化销售收入
        double strategySalesPrice = Double.parseDouble(finalValue.get("strategySalesPrice").toString());
        return sales / strategySalesPrice * 100;
    }

    /**
     * (H8最终销售额/H8调整后销量)/H8战略基准单价-1
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return H8单车溢价率
     */
    public Double h8PremiumRate(BalanceScoreSettlementDO balanceScore) {
        Map<String, Object> param = getClassTeamYear(balanceScore);
        param.put("vehicleModel", NormalConstant.VEHICLE_MODEL_H8);
        Map<String, Object> finalValue = balanceScore.getTeamIntermediateMapper().selectTeamIntermediateYearSum(param);
        // H8最终销售额
        double sales = Double.parseDouble(finalValue.get("sales").toString());
        // H8调整后销量
        double adjustedSales = Double.parseDouble(finalValue.get("adjustedSales").toString());
        // H8战略基准单价
        double strategyUnitPrice = Double.parseDouble(finalValue.get("strategyUnitPrice").toString());
        return ((sales / adjustedSales) / strategyUnitPrice - 1)*100;
    }

    /**
     * 1-(H8最终材料成本/H8调整后销量)/(H8战略基准材料成本合计/H8战略基准销量)
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return H8单车成本下降率
     */
    public Double h8CostReductionRate(BalanceScoreSettlementDO balanceScore) {
        Map<String, Object> param = getClassTeamYear(balanceScore);
        param.put("vehicleModel", NormalConstant.VEHICLE_MODEL_H8);
        Map<String, Object> finalValue = balanceScore.getTeamIntermediateMapper().selectTeamIntermediateYearSum(param);
        // H8最终材料成本
        double materialCost = Double.parseDouble(finalValue.get("materialCost").toString());
        // H8调整后销量
        double adjustedSales = Double.parseDouble(finalValue.get("adjustedSales").toString());
        // H8战略基准材料成本合计
        double strategyCostTotal = Double.parseDouble(finalValue.get("strategyCostTotal").toString());
        // H8战略基准销量
        double strategySales = Double.parseDouble(finalValue.get("strategySales").toString());
        return (1 - (materialCost / adjustedSales) / (strategyCostTotal / strategySales)) * 100;
    }

    /**
     * 利润表中的今年营业总成本/利润表中的去年营业总成本-1
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 成本费用增长率
     */
    public Double costGrowthRate(BalanceScoreSettlementDO balanceScore) {
        Map<String, Object> param = getClassTeamYear(balanceScore);
        Map<String, Object> teamProfitCurr = balanceScore.getTeamProfitStatementMapper().selectTeamProfitStatement(param);
        param.put("year", Integer.parseInt(param.get("year").toString()) - 1);
        Map<String, Object> teamProfitFront = balanceScore.getTeamProfitStatementMapper().selectTeamProfitStatement(param);
        // 营业总成本
        double totalOperatingCostCurr = Double.parseDouble(teamProfitCurr.get("totalOperatingCost").toString());
        double totalOperatingCostFront = Double.parseDouble(teamProfitFront.get("totalOperatingCost").toString());
        return (totalOperatingCostCurr / totalOperatingCostFront - 1) * 100;
    }

    /**
     * H8调整后销量(万辆)/市场销量给个定数(在那个配置表中配置)
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return H8市场占有率
     */
    public Double h8MarketShare(BalanceScoreSettlementDO balanceScore) {
        Map<String, Object> param = getClassTeamYear(balanceScore);
        param.put("vehicleModel", NormalConstant.VEHICLE_MODEL_H8);
        Map<String, Object> finalValue = balanceScore.getTeamIntermediateMapper().selectTeamIntermediateYearSum(param);
        // H8调整后销量
        double adjustedSales = Double.parseDouble(finalValue.get("adjustedSales").toString());
        // 市场销量给定个数
        param.put("vCode", VNCConstant.SCXLGD);
        Double testCarNum = balanceScore.getBaJsMapper().findAdjustSale(Integer.valueOf(balanceScore.getTimeLine().substring(0, 4)));
        return (adjustedSales / testCarNum) * 100;
    }

    /**
     * 变量 4PKHMYDH8
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 客户满意度
     */
    public Double customerSatisfaction(BalanceScoreSettlementDO balanceScore) {
        return getVariableVal(balanceScore, VariableConstant.FPKHMYDH8);
    }

    /**
     * 变量 XQCGH8
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 意向客户需求满足率
     */
    public Double satisfactionRate(BalanceScoreSettlementDO balanceScore) {
        return getVariableVal(balanceScore, VariableConstant.XQCGH8);
    }

    /**
     * 变量 YYLCYH
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 生产流程最短时间降低率
     */
    public Double processReductionRate(BalanceScoreSettlementDO balanceScore) {
        return getVariableVal(balanceScore, VariableConstant.YYLCYH);
    }

    /**
     * (本年总收入/本年度总销量)/(上年总收入/上年度总销量)-1
     * 今年的(利润表中的营业总收入/中间表调整后销量(万辆)所有车型合计)/
     * 去年的(利润表中的营业总收入/中间表调整后销量(万辆)所有车型合计)-1
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 全线产品平均售价增长率
     */
    public Double averagePriceGrowthRate(BalanceScoreSettlementDO balanceScore) {
        Map<String, Object> param = getClassTeamYear(balanceScore);
        Map<String, Object> teamProfitCurr = balanceScore.getTeamProfitStatementMapper().selectTeamProfitStatement(param);
        // 今年营业总收入
        double grossRevenueCurr = Double.parseDouble(teamProfitCurr.get("grossRevenue").toString());
        // 今年调整后销量
        Long adjustedSalesCurr = balanceScore.getTeamIntermediateMapper().selectAdjustedSales(param);

        param.put("year", Integer.parseInt(param.get("year").toString()) - 1);
        Map<String, Object> teamProfitFront = balanceScore.getTeamProfitStatementMapper().selectTeamProfitStatement(param);
        // 去年营业总收入
        double grossRevenueFront = Double.parseDouble(teamProfitFront.get("grossRevenue").toString());
        // 去年调整后销量
        Long adjustedSalesFront = balanceScore.getTeamIntermediateMapper().selectAdjustedSales(param);

        return ((grossRevenueCurr / adjustedSalesCurr) / (grossRevenueFront / adjustedSalesFront) - 1) * 100;
    }

    /**
     * H8收入/总收入
     * H8最终销售额/利润表中的营业总收入
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 新产品贡献率
     */
    public Double newContributionRate(BalanceScoreSettlementDO balanceScore) {
        Map<String, Object> param = getClassTeamYear(balanceScore);
        param.put("vehicleModel", NormalConstant.VEHICLE_MODEL_H8);
        Map<String, Object> finalValue = balanceScore.getTeamIntermediateMapper().selectTeamIntermediateYearSum(param);
        Map<String, Object> teamProfit = balanceScore.getTeamProfitStatementMapper().selectTeamProfitStatement(param);
        // H8最终销售额
        double sales = Double.parseDouble(finalValue.get("sales").toString());
        // 营业总收入
        double grossRevenue = Double.parseDouble(teamProfit.get("grossRevenue").toString());
        return sales / grossRevenue * 100;
    }

    /**
     * 研发费用投入从t_team_rdv表取/利润表中的营业收入
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 研发费用占收入比
     */
    public Double rDCostProportion(BalanceScoreSettlementDO balanceScore) {
//        Map<String, Object> param = getClassTeamYear(balanceScore);
//        Map<String, Object> teamProfit = balanceScore.getTeamProfitStatementMapper().selectTeamProfitStatement(param);
//        // 营业收入
//        double operationRevenue = Double.parseDouble(teamProfit.get("operationRevenue").toString());
//        // 研发费用投入
//        param.put("vCode", VNCConstant.TBYFFYTR);
//        //添加标志说明此code使用t_team_rdv表
//        param.put("isYT", 1);
//        String devFee = balanceScore.getOsMapper().selectTeamRdv(param);
//        return Double.parseDouble(devFee) / operationRevenue;
        return Double.valueOf(balanceScore.getBaJsMapper().getRdvValue(balanceScore.getClassId(), balanceScore.getTeamId(),
                VNCConstant.YFFYZSRB, Integer.valueOf(balanceScore.getTimeLine().substring(0, 4)) - 1));
    }

    /**
     * 变量 FXSBH8
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 风险监控机制完善度
     */
    public Double monitoringPerfection(BalanceScoreSettlementDO balanceScore) {
        return getVariableVal(balanceScore, VariableConstant.FXSBH8);
    }

    /**
     * H8最终销售额-H8最终材料成本
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return H8销售毛利
     */
    public Double h8SalesProfit(BalanceScoreSettlementDO balanceScore) {
        Map<String, Object> param = getClassTeamYear(balanceScore);
        param.put("vehicleModel", NormalConstant.VEHICLE_MODEL_H8);
        Map<String, Object> finalValue = balanceScore.getTeamIntermediateMapper().selectTeamIntermediateYearSum(param);
        // H8最终销售额
        double sales = Double.parseDouble(finalValue.get("sales").toString());
        // H8最终营业成本
        double materialCost = Double.parseDouble(finalValue.get("operatingCostPlus").toString());
        return sales - materialCost;
    }

    /**
     * 背景资料交代个6次或者8次，这个最终显示赋值为背景中交代的数值。可以给定，考核时显示。
     * 这个数据为配置值放在t_conf_variable表，表里会用到code的配置在t_conf_vnc
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 组织媒体参与车辆试驾测评10次
     */
    public Double mediaTestDrive(BalanceScoreSettlementDO balanceScore) {
        Map<String, Object> param = getClassTeamYear(balanceScore);
        // 组织媒体参与车辆试驾测评
        param.put("vCode", VNCConstant.ZZMTCY);
        String testCarNum = balanceScore.getOsMapper().selectTeamRdv(param);
        return Double.parseDouble(testCarNum);
    }

    /**
     * 变量 YGNLH8
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 员工培训有效率
     */
    public Double trainingEfficiency(BalanceScoreSettlementDO balanceScore) {
        return getVariableVal(balanceScore, VariableConstant.YGNLH8);
    }

    /**
     * 变量 YGYWH8
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 员工业务能力升级
     */
    public Double capabilityUpgrading(BalanceScoreSettlementDO balanceScore) {
        return getVariableVal(balanceScore, VariableConstant.YGYWH8);
    }

    /**
     * 变量 QYWHH8
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 企业文化塑造完成度
     */
    public Double culturalCompleteness(BalanceScoreSettlementDO balanceScore) {
        return getVariableVal(balanceScore, VariableConstant.QYWHH8);
    }

    /**
     * (亿元)(1-15%)*(H8最终销售额-H8最终营业成本合计-H8最终营业税金及附加-H8最终销售费用合计-H8最终管理费用合计-
     * H8最终财务费用-H8最终资产减值损失合计+H8最终营业外收入-H8最终营业外支出)
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return H8净利润
     */
    public Double h8NetProfit(BalanceScoreSettlementDO balanceScore) {
        Map<String, Object> param = getClassTeamYear(balanceScore);
        param.put("vehicleModel", NormalConstant.VEHICLE_MODEL_H8);
        Map<String, Object> finalValue = balanceScore.getTeamIntermediateMapper().selectTeamIntermediateYearSum(param);
        // H8最终销售额
        double sales = Double.parseDouble(finalValue.get("sales").toString());
        // H8最终营业成本合计
        double operatingCostPlus = Double.parseDouble(finalValue.get("operatingCostPlus").toString());
        // H8最终营业税金及附加
        double operatingTaxSurcharge = Double.parseDouble(finalValue.get("operatingTaxSurcharge").toString());
        // H8最终销售费用合计
        double salesTotal = Double.parseDouble(finalValue.get("salesTotal").toString());
        // H8最终管理费用合计
        double managementTotal = Double.parseDouble(finalValue.get("managementTotal").toString());
        // H8最终财务费用
        double financialCost = Double.parseDouble(finalValue.get("financialCost").toString());
        // H8最终资产减值损失合计
        double lossTotal = Double.parseDouble(finalValue.get("lossTotal").toString());
        // H8最终营业外收入
        double finalNonbusinessIncome = Double.parseDouble(finalValue.get("finalNonbusinessIncome").toString());
        // H8最终营业外支出
        double finalNonbusinessExpenditure = Double.parseDouble(finalValue.get("finalNonbusinessExpenditure").toString());

        double result = sales - operatingCostPlus - operatingTaxSurcharge - salesTotal - managementTotal;
        result = result - financialCost - lossTotal + finalNonbusinessIncome - finalNonbusinessExpenditure;
        return result * 0.85;
    }

    /**
     * 资产负债表
     * 1-(本年末存货金额/上年末存货金额)
     *
     * @param balanceScore 平衡积分卡 实际值计算用参数
     * @return 期末库存下降比率
     */
    public Double inventoryDeclineRate(BalanceScoreSettlementDO balanceScore) {
        Map<String, Object> param = getClassTeamYear(balanceScore);
        Long inventoryCurr = balanceScore.getTeamBalanceSheetMapper().getInventory(param);
        param.put("year", Integer.parseInt(param.get("year").toString()) - 1);
        Long inventoryFront = balanceScore.getTeamBalanceSheetMapper().getInventory(param);
        return (1 - ((double) inventoryCurr / (double) inventoryFront)) * 100;
    }


    // 准备参数
    private Map<String, Object> getClassTeamYear(BalanceScoreSettlementDO balanceScore) {
        String currYear = balanceScore.getTimeLine().substring(0, 4);
        Map<String, Object> param = new HashMap<>();
        param.put("classId", balanceScore.getClassId());
        param.put("teamId", balanceScore.getTeamId());
        param.put("year", Integer.parseInt(currYear));
        return param;
    }

    // 查询变量值
    private Double getVariableVal(BalanceScoreSettlementDO balanceScore, String variableName) {
        Integer classId = balanceScore.getClassId();
        Integer teamId = balanceScore.getTeamId();
        Map<String, Object> result = balanceScore.getOsMapper().getVariableRecord(classId, teamId, variableName);
        return Double.parseDouble(result.get("variableVal").toString());
    }

    // ========== 平衡积分卡 H8
    // ========== 平衡积分卡 WEY

    /**
     * WEY产品销售收入
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return WEY产品销售收入
     */
    public Double salesRevenue(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        return param.getBaJsMapper().salesRevenue(param, year);
    }

    /**
     * 总资产周转率（销售收入/平均总资产）
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 总资产周转率
     */
    public Double allTurnoverRate(BalanceScoreSettlementDO param) {
        //年度利润表中营业收入
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        Double operationRevenue = param.getBaJsMapper().findOperationRevenue(param, year);
        //资产负债本年总资产
        Double totalAssets = param.getBaJsMapper().findTotalAssets(param, year);
        //资产负债去年总资产
        Double lastTotalAssets = param.getBaJsMapper().findLastTotalAssets(param, year);
        Double averageTotalAssets = (totalAssets + lastTotalAssets) / 2;
        return operationRevenue / averageTotalAssets;

    }

    /**
     * WEY单车溢价率（WEY实际平均售价/WEY战略标准售价-1）
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return WEY单车溢价率
     */
    public Double premiumRateWEY(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        return param.getBaJsMapper().findPremiumRateWEY(param, year) * 100;
    }

    /**
     * WEY成本下降率 （1-2017年单车实际平均成本/2017年单车战略标准成本）
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return WEY成本下降率
     */
    public Double costReductionRateWEY(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        return (1 - param.getBaJsMapper().findCostReductionRateWEY(param, year)) * 100;
    }

    /**
     * WEY满意度-2题中取变量PPMYD-WEY值
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return PPMYD-WEY
     */
    public Double pPMYD(BalanceScoreSettlementDO param) {
        String pPMYD = param.getBaJsMapper().findPPMYD(param);
        return Double.valueOf(pPMYD);
    }

    /**
     * 新车WEY毛利率(wey最终销售额（亿元）-WEY最终营业成本（亿元）)/wey最终销售额（亿元）
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return PPMYD-WEY
     */
    public Double grossprofitMargin(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        Double grossprofitMargin = param.getBaJsMapper().grossprofitMargin(param, year);
        return grossprofitMargin * 100;
    }

    /**
     * WEY市场份额（年销量/行业总销量给定）
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return WEY市场份额
     */
    public Double marketShare(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        Double nSale = param.getBaJsMapper().findMarketShare(param, year);
        Double hySale = param.getBaJsMapper().findAdjustSale(year);
        return nSale / hySale * 100;
    }

    /**
     * WEY满意度-2题中取变量HKMYD-WEY值
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return HKMYD-WEY
     */
    public Double hKMYD(BalanceScoreSettlementDO param) {
        String hKMYD = param.getBaJsMapper().findHKMYD(param);
        return Double.valueOf(hKMYD);
    }

    /**
     * 4p营销第五题变量4PKHXYL-WEY----(1)
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 4PKHXYL-WEY
     */
    public Double fPKHXYL(BalanceScoreSettlementDO param) {
        String fPKHXYL = param.getBaJsMapper().findFPKHXYL(param);
        return Double.valueOf(fPKHXYL);
    }

    /**
     * WEY生产工人人均产量【年度销量/生产工人人数】
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return WEY生产工人人均产量
     */
    public Double averageCL(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        Double XL = param.getBaJsMapper().findXL(param, year);
        Double Num = param.getBaJsMapper().findNum(year);
        return XL / Num;
    }

    /**
     * 全线产品平均售价增长率【（本年总收入/本年度总销量）/（上年总收入/上年度总销量）-1】
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return 全线产品平均售价增长率
     */
    public Double sJRate(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        //（本年总收入/本年度总销量）
        Double sJRate = param.getBaJsMapper().findsJRate(param, year);
        //上年总收入/上年度总销量）
        Double sJLastRate = param.getBaJsMapper().findsLastJRate(param, year);
        return (sJRate / sJLastRate - 1) * 100;
    }

    /**
     * WEY收入/总收入
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return Double
     */
    public Double zZRate(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        Double zZateWEY = param.getBaJsMapper().zZRate(param, year);
        Double zZateALL = param.getBaJsMapper().findZzRate(param, year);
        return zZateWEY / zZateALL * 100;
    }

    /**
     * 研发费用占收入比(本年填报研发费用/本年总收入)
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return Double
     */
    public Double yFFYZB(BalanceScoreSettlementDO param) {
//        //本年研发费用
//        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
//        Double yValue = param.getBaJsMapper().findYvalue(param, year);
//        //利润表中营业收入
//        Double zZateWEY = param.getBaJsMapper().zZRate(param, year);
//        return yValue / zZateWEY * 100;
        return Double.valueOf(param.getBaJsMapper().getRdvValue(param.getClassId(), param.getTeamId(),
                VNCConstant.YFFYZSRB, Integer.valueOf(param.getTimeLine().substring(0, 4)) - 1));

    }

    /**
     * 取内部控制变量NBKZWEY
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return Double
     */
    public Double nBKZWEY(BalanceScoreSettlementDO param) {
        String nBKZWEY = param.getBaJsMapper().findNBKZWEY(param);
        return Double.valueOf(nBKZWEY);
    }

    /**
     * 总体收入增长率（今年-去年营业总收入）/今年
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return Double
     */
    public Double allZZRate(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        //本年营业收入
        Double zZateWEY = param.getBaJsMapper().zZLastRate(param, year);
        //去年营业收入
        Double zZateLastWEY = param.getBaJsMapper().zZLastRate(param, year - 1);
        return (zZateWEY - zZateLastWEY) / zZateLastWEY * 100;
    }

    /**
     * WEY净利润
     *（1-15%）*（wey最终销售额（亿元)-wey最终营业成本合计（亿元）-wey最终营业税金及附加-wey最终销售费用合计（亿元）-wey最终管理费用合计（亿元）-wey最终财务费用（亿元）-wey最终资产减值损失合计（亿元）+wey最终营业外收入（亿元）-wey最终营业外支出（亿元））
     * @param param 平衡积分卡 实际值计算用参数
     * @return Double
     */
    public Double jLR(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        Double jLR = param.getBaJsMapper().findJLR(param, year);
        return (1-0.15)*jLR;
    }

    /**
     * 取产品设计能力提升率题的变量XJSPPWEY值。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return Double
     */
    public Double xJSPPWEY(BalanceScoreSettlementDO param) {
        String xJSPPWEY = param.getBaJsMapper().findxJSPPWEY(param);
        return Double.valueOf(xJSPPWEY);
    }

    /**
     * 员工能力培训WEY题中取变量YGNLWEY。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return Double
     */
    public Double yGNLWEY(BalanceScoreSettlementDO param) {
        String yGNLWEY = param.getBaJsMapper().findYGNLWEY(param);
        return Double.valueOf(yGNLWEY);
    }

    /**
     * 信息化建设完成度WEY题中取变量XXJSWEY。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return Double
     */
    public Double xXJSWEY(BalanceScoreSettlementDO param) {
        String xXJSWEY = param.getBaJsMapper().findxXJSWEY(param);
        return Double.valueOf(xXJSWEY);
    }

    /**
     * WEY销售毛利。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return Double
     */
    public Double mLSale(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        Double mlSale = param.getBaJsMapper().findMLSale(param, year);
        return mlSale;
    }

    /**
     * 期末库存下降比率【1-（本年末存货金额/上年末存货金额）】。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return Double
     */
    public Double kCXJRate(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        //本年存货金额
        Double kCXJRate = param.getBaJsMapper().findKCXJRate(param, year);
        //上年存货金额
        Double kCXJRateLast = param.getBaJsMapper().findKCXJRateLast(param, year);
        return ((1 - kCXJRate / kCXJRateLast) * 100);
    }

    /**
     * WEY产能利用率【（年销量/给定17年产能10万辆，18年产能给定15万辆）*100%】。
     *
     * @param param 平衡积分卡 实际值计算用参数
     * @return Double
     */
    public Double cNLYRate(BalanceScoreSettlementDO param) {
        Integer year = Integer.valueOf(param.getTimeLine().substring(0, 4));
        //所有WEY车型调整后销量
        Double allModel = param.getBaJsMapper().findAllModle(param, year);
        //查询产能
        Double cN = param.getBaJsMapper().findCn(param, year);
        return allModel / cN * 100;
    }
}
