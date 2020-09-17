package com.tzcpa.mapper.treatment;

import org.apache.ibatis.annotations.Param;

import com.tzcpa.model.question.BalanceScoreSettlementDO;

/**
 * <p>Description: </p>
 * @author lrs
 * @date 2019年5月30日
 */
public interface BalanceJsMapper {
	/**
	 * 查询营业利润率
	 * @param param
	 * @return Double
	 */
	Double findProfitMargin(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询年度利润表中营业收入
	 * @param param
	 * @return Double
	 */
	Double findOperationRevenue(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询资产负债本年总资产
	 * @param param
	 * @return Double
	 */
	Double findTotalAssets(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询资产负债去年总资产
	 * @param param
	 * @return Double
	 */
	Double findLastTotalAssets(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询年度利润表净利润
	 * @param param
	 * @return Double
	 */
	Double findNetProfit(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询新产品溢价率（本年平均售价/本年战略基准平均售价-1）
	 * @param param
	 * @param year 
	 * @return Double
	 */
	Double findPremiumRate(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询单车成本下降率（本年实际单车成本/本年战略基准成本给定-1）
	 * @param param
	 * @param year 
	 * @return Double
	 */
	Double findCostReductionRate(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询取变量XQCP的值
	 * @param param
	 * @return Double
	 */
	String findXQCP(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * 查询H6本年销售量的值
	 * @param param
	 * @return Double
	 */
	int findH6Sale(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询全部车型本年销售量的值
	 * @param param
	 * @return Double
	 */
	int findAllSale(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询4PKHMYDH6的值
	 * @param param
	 * @return Double
	 */
	String findFPKHMYDH6(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * 查询4PJGMYDH6的值
	 * @param param
	 * @return Double
	 */
	String findFPJGMYDH6(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * 查询变量YYLCYH值
	 * @param param
	 * @return Double
	 */
	String findYylcyh(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * 查询H6的最终销售额
	 * @param param
	 * @return Double
	 */
	Double findSalesH6(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询所有车型的最终销售额
	 * @param param
	 * @return Double
	 */
	Double findSalesAll(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询每年研发费用投入题变量YFFX值
	 * @param param
	 * @return String
	 */
	String findYffx(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * 查询培训体系建设题中取变量PXH6值
	 * @param param
	 * @return String
	 */
	String findPxH6(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * 查询关键人才引进题中取变量GJRCH6值
	 * @param param
	 * @return String
	 */
	String findGjrH6(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * 查询组织结构调整题中取变量ZZJGH6值
	 * @param param
	 * @return String
	 */
	String findZzJGH6(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * 查询取自风险识别题变量FXSBH6。
	 * @param param
	 * @return String
	 */
	String findFxSBH6(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * 查询本年研发费用。
	 * @param param
	 * @param year 
	 * @return Double
	 */
	Double findYvalue(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询上一年研发费用。
	 * @param param
	 * @param year 
	 * @return Double
	 */
	Double findYLastValue(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询本年营业收入。
	 * @param param
	 * @param year 
	 * @return Integer
	 */
	Long findYysr(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询企业文化数量。
	 * @param param
	 * @param year 
	 * @return Double
	 */
	Double findQywenH(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询本年最终销售额合计。
	 * @param param
	 * @param year 
	 * @return Double
	 */
	Double findFinalSales(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询本年调整后销量。
	 * @param param
	 * @param year 
	 * @return Double
	 */
	Double findAdjustedSales(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询去年最终销售额合计。
	 * @param param
	 * @param year 
	 * @return Double
	 */
	Double findFinalLastSales(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询去年调整后销量。
	 * @param param
	 * @param year 
	 * @return Double
	 */
	Double findAdjustedLastSales(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询品牌建设推广活动数量.
	 * @param param
	 * @param year 
	 * @return Double
	 */
	Double findPJTG(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询WEY产品销售收入.
	 * @param param
	 * @param year 
	 * @return Double
	 */
	Double salesRevenue(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询WEY产品销售收入.
	 * @param param
	 * @param year 
	 * @return Double
	 */
	double findPremiumRateWEY(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询WEY产品成本下降率.
	 * @param param
	 * @param year 
	 * @return Double
	 */
	Double findCostReductionRateWEY(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * WEY满意度-2题中取变量PPMYD-WEY值.
	 * @param param
	 * @return String
	 */
	String findPPMYD(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * (wey最终销售额（亿元）-WEY最终营业成本（亿元）)/wey最终销售额（亿元）.
	 * @param param
	 * @param year
	 * @return String
	 */
	Double grossprofitMargin(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * WEY市场份额（年销量/行业总销量给定）.
	 * @param param
	 * @param year
	 * @return Double
	 */
	Double findMarketShare(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * WEY满意度-2题中取变量HKMYD-WEY值.
	 * @param param
	 * @param year
	 * @return String
	 */
	String findHKMYD(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * WEY满意度-2题中取变量4PKHXYL-WEY值.
	 * @param param
	 * @param year
	 * @return String
	 */
	String findFPKHXYL(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * 调整后销量.
	 * @param param
	 * @param year
	 * @return String
	 */
	Double findXL(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 调整后销量.
	 * @param param
	 * @param year
	 * @return String
	 */
	Double findNum(@Param("year")Integer year);
	/**
	 * 查询本年销售额.
	 * @param param
	 * @param year
	 * @return Double
	 */
	Double findsJRate(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询去年销售额.
	 * @param param
	 * @param year
	 * @return Double
	 */
	Double findsLastJRate(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * WEY收入.
	 * @param param
	 * @param year
	 * @return Double
	 */
	Double zZRate(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 所有车型收入.
	 * @param param
	 * @param year
	 * @return Double
	 */
	Double findZzRate(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 内部控制变量NBKZWEY.
	 * @param param
	 * @param year
	 * @return Double
	 */
	String findNBKZWEY(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * 去年营业收入.
	 * @param param
	 * @param year
	 * @return Double
	 */
	Double zZLastRate(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * WEY净利润.
	 * @param param
	 * @param year
	 * @return Double
	 */
	Double findJLR(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * XJSPPWEY值.
	 * @param param
	 * @param year
	 * @return Double
	 */
	String findxJSPPWEY(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * YGNLWEY值.
	 * @param param
	 * @return Double
	 */
	String findYGNLWEY(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * XXJSWEY值.
	 * @param param
	 * @return Double
	 */
	String findxXJSWEY(@Param("param")BalanceScoreSettlementDO param);
	/**
	 * XXJSWEY值.
	 * @param param
	 * @return Double
	 */
	Double findMLSale(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 本年末存货金额值.
	 * @param param
	 * @return Double
	 */
	Double findKCXJRate(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 去年末存货金额值.
	 * @param param
	 * @return Double
	 */
	Double findKCXJRateLast(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查询全部车型销量.
	 * @param param
	 * @return Double
	 */
	Double findAllModle(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查讯本年产能.
	 * @param param
	 * @return Double
	 */
	Double findCn(@Param("param")BalanceScoreSettlementDO param,@Param("year")Integer year);
	/**
	 * 查讯2010年H6车辆单价.
	 * @param 
	 * @return Double
	 */
	Double findDj();
	
	/**
	 * 获取rdv表中的value值数据
	 * @return
	 */
	String getRdvValue(@Param("classId") Integer classId, @Param("teamId") Integer teamId, @Param("vCode") String vCode,
			@Param("year") Integer year);
	
	
	Double findAdjustSale(@Param("year")Integer year);
	
	/**
	 * 查询给定研发费
	 * @param year
	 * @return
	 */
	Double findTBYF(@Param("year")Integer year);

}

