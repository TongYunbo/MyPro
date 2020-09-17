package com.tzcpa.service.student;

import com.tzcpa.model.student.MonthlySales;
import com.tzcpa.model.student.SingleMaterialCost;
import com.tzcpa.model.student.TeamPrice;

import java.util.List;
import java.util.Map;

/**
 * 月销量列表信息
 * @author WangYao
 * 2019年5月21日
 */
public interface DashboardRankingService {

	/**
	 * 月销量列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	Map<String,List<MonthlySales>> getMonthlySalesList(int year, int classId, int teamId);

	/**
	 * 团队价格列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	Map<String,List<TeamPrice>> getTeamPriceList(int year, int classId, int teamId);

	/**
	 * 单车材料成本列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	Map<String, List<SingleMaterialCost>> getSingleMaterialCostList(int year, int classId, int teamId);

	/**
	 * 市场份额占比
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param year
	 * @param classId
	 * @param id
	 * @return
	 */
	Map<String, Object> getMarketShare(int year, Integer classId, Integer teamId);

	/**
	 * 产品销售收入占比
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	List<Map<String, Object>> getSalesRevenue(int year, Integer classId, Integer teamId);

	/**
	 * 产品累计营业利润占比
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param year
	 * @param classId
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> getOperatingProfit(int year, Integer classId, Integer teamId);

	/**
	 * 月净利润、营业收入、销售毛利图表
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	List<Map<String, Object>> getMonthlyOperatingIncome(int year, Integer classId, Integer teamId);

	/**
	 * 月销售毛利率，销售净利率，营业利润率图表
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	List<Map<String, Object>> getMonthlyGrossMargin(int year, Integer classId, Integer teamId);
}
