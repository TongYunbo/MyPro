package com.tzcpa.mapper.student;

import com.tzcpa.model.student.MonthlySales;
import com.tzcpa.model.student.SingleMaterialCost;
import com.tzcpa.model.student.TeamMonthlyProfitStatementDO;
import com.tzcpa.model.student.TeamPrice;
import com.tzcpa.model.treatment.TeamIntermediateDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 月销量列表信息
 * @author WangYao
 * 2019年5月21日
 */
public interface DashboardRankingMapper {

	/**
	 * 月销量列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param intermediateDO
	 * @param currentMonth 
	 * @return
	 */
	List<MonthlySales> getMonthlySalesList(@Param("intermediateDO")TeamIntermediateDO intermediateDO);

	/**
	 * 团队价格列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param intermediateDO
	 * @return
	 */
	List<TeamPrice> getTeamPriceList(@Param("intermediateDO")TeamIntermediateDO intermediateDO);

	/**
	 * 单车材料成本列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param intermediateDO
	 * @param currentMonth 
	 * @return
	 */
	List<SingleMaterialCost> getSingleMaterialCostList(@Param("intermediateDO")TeamIntermediateDO intermediateDO);

	/**
	 * 市场份额占比--按照车型分组
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param intermediateDO
	 * @return
	 */
	List<Map<String,Object>> getMarketShareByGroup(TeamIntermediateDO intermediateDO);

	/**
	 * 市场份额占比
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param intermediateDO
	 * @return
	 */
	List<Map<String,Object>> getMarketShare(TeamIntermediateDO intermediateDO);

	/**
	 * 产品销售收入占比
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param intermediateDO
	 * @return
	 */
	List<Map<String, Object>> getSalesRevenue(TeamIntermediateDO intermediateDO);

	/**
	 * 产品累计营业利润占比
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param intermediateDO
	 * @return
	 */
	List<Map<String, Object>> getOperatingProfit(TeamIntermediateDO intermediateDO);

	/**
	 * 月净利润、营业收入、销售毛利图表
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param monthlyProfitStatementDO
	 * @return
	 */
	List<Map<String, Object>> getMonthlyOperatingIncome(@Param("monthlyProfitStatementDO")TeamMonthlyProfitStatementDO monthlyProfitStatementDO);

	/**
	 * 月销售毛利率，销售净利率，营业利润率图表
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param monthlyProfitStatementDO
	 * @param currentMonth 
	 * @return
	 */
	List<Map<String, Object>> getMonthlyGrossMargin(@Param("monthlyProfitStatementDO")TeamMonthlyProfitStatementDO monthlyProfitStatementDO);
}
