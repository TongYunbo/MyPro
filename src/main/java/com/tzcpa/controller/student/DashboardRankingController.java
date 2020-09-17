package com.tzcpa.controller.student;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.student.MonthlySales;
import com.tzcpa.model.student.SingleMaterialCost;
import com.tzcpa.model.student.TeamPrice;
import com.tzcpa.service.student.DashboardRankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 仪表盘排行
 * @author WangYao
 * 2019年5月21日
 */
@RestController
@RequestMapping("/dashboardRanking")
@Slf4j
public class DashboardRankingController {

	@Autowired
	private DashboardRankingService dashboardRankingService;
	
	/**
	 * 月销量列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/monthlySaleslist")
	public ResponseResult<Map<String,List<MonthlySales>>> getMonthlySalesList(@RequestBody JSONObject jsonObject){
		log.info("月销量列表信息 jsonObject={} ",jsonObject);
		int year = jsonObject.getInteger("year");
		int classId = jsonObject.getInteger("classId");
		int teamId = jsonObject.getInteger("teamId");
		Map<String,List<MonthlySales>> list = dashboardRankingService.getMonthlySalesList(year, classId, teamId);
		log.info("月销量列表信息 list={} ",JSON.toJSONString(list));
		return new ResponseResult<Map<String,List<MonthlySales>>>(list);
	}
	
	/**
	 * 团队价格列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/teamPricelist")
	public ResponseResult<Map<String,List<TeamPrice>>> getTeamPriceList(@RequestBody JSONObject jsonObject){
		log.info("团队价格列表信息 jsonObject={}",jsonObject);
		int year = jsonObject.getInteger("year");
		int classId = jsonObject.getInteger("classId");
		int teamId = jsonObject.getInteger("teamId");
		Map<String,List<TeamPrice>> list = dashboardRankingService.getTeamPriceList(year, classId, teamId);
		log.info("团队价格列表信息 list={} ",JSON.toJSONString(list));
		return new ResponseResult<Map<String,List<TeamPrice>>>(list);
	}
	
	/**
	 * 单车材料成本列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/singleMaterialCostlist")
	public ResponseResult<Map<String,List<SingleMaterialCost>>> getSingleMaterialCostList(@RequestBody JSONObject jsonObject){
		log.info("单车材料成本列表信息 jsonObject={}",jsonObject);
		int year = jsonObject.getInteger("year");
		int classId = jsonObject.getInteger("classId");
		int teamId = jsonObject.getInteger("teamId");
		Map<String,List<SingleMaterialCost>> list = dashboardRankingService.getSingleMaterialCostList(year, classId, teamId);
		log.info("单车材料成本列表信息 list={}",JSON.toJSONString(list));
		return new ResponseResult<Map<String, List<SingleMaterialCost>>>(list);
	}
	
	/**
	 * 市场份额占比
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/marketShare")
	public ResponseResult<Map<String, Object>> getMarketShare(@RequestBody JSONObject jsonObject){
		log.info("市场份额占比 jsonObject={}", jsonObject);
		int year = jsonObject.getInteger("year");
		int classId = jsonObject.getInteger("classId");
		int teamId = jsonObject.getInteger("teamId");
		Map<String, Object> map = dashboardRankingService.getMarketShare(year, classId, teamId);
		log.info("市场份额占比 map={}", JSON.toJSONString(map));
		return new ResponseResult<Map<String, Object>>(map);
	}
	
	/**
	 * 产品销售收入占比
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/salesRevenue")
	public ResponseResult<List<Map<String,Object>>> getSalesRevenue(@RequestBody JSONObject jsonObject){
		log.info("产品销售收入占比 jsonObject={}", jsonObject);
		int year = jsonObject.getInteger("year");
		int classId = jsonObject.getInteger("classId");
		int teamId = jsonObject.getInteger("teamId");
		List<Map<String,Object>> list = dashboardRankingService.getSalesRevenue(year, classId, teamId);
		log.info("产品销售收入占比 list={}", JSON.toJSONString(list));
		return new ResponseResult<List<Map<String,Object>>>(list);
	}
	
	/**
	 * 产品累计营业利润占比
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/operatingProfit")
	public ResponseResult<List<Map<String,Object>>> getOperatingProfit(@RequestBody JSONObject jsonObject){
		log.info("产品累计营业利润占比 jsonObject={}", jsonObject);
		int year = jsonObject.getInteger("year");
		int classId = jsonObject.getInteger("classId");
		int teamId = jsonObject.getInteger("teamId");
		List<Map<String,Object>> list = dashboardRankingService.getOperatingProfit(year, classId, teamId);
		log.info("产品累计营业利润占比 list={}", JSON.toJSONString(list));
		return new ResponseResult<List<Map<String,Object>>>(list);
	}
	
	/**
	 * 月净利润、营业收入、销售毛利图表
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/monthlyOperatingIncome")
	public ResponseResult<List<Map<String,Object>>> getMonthlyOperatingIncome(@RequestBody JSONObject jsonObject){
		log.info("月净利润、营业收入、销售毛利图表 jsonObject={}", jsonObject);
		int year = jsonObject.getInteger("year");
		int classId = jsonObject.getInteger("classId");
		int teamId = jsonObject.getInteger("teamId");
		List<Map<String,Object>> list = dashboardRankingService.getMonthlyOperatingIncome(year, classId, teamId);
		log.info("月净利润、营业收入、销售毛利图表 list={}", JSON.toJSONString(list));
		return new ResponseResult<List<Map<String,Object>>>(list);
	}
	
	/**
	 * 月销售毛利率，销售净利率，营业利润率图表
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/monthlyGrossMargin")
	public ResponseResult<List<Map<String,Object>>> getMonthlyGrossMargin(@RequestBody JSONObject jsonObject){
		log.info("月销售毛利率，销售净利率，营业利润率图表 jsonObject={}", jsonObject);
		int year = jsonObject.getInteger("year");
		int classId = jsonObject.getInteger("classId");
		int teamId = jsonObject.getInteger("teamId");
		List<Map<String,Object>> list = dashboardRankingService.getMonthlyGrossMargin(year, classId, teamId);
		log.info("月销售毛利率，销售净利率，营业利润率图表 list={}", JSON.toJSONString(list));
		return new ResponseResult<List<Map<String,Object>>>(list);
	}
}
