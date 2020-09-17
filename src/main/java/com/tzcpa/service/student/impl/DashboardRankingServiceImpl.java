package com.tzcpa.service.student.impl;

import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.Constant;
import com.tzcpa.mapper.student.DashboardRankingMapper;
import com.tzcpa.mapper.student.StuAnswerMapper;
import com.tzcpa.mapper.treatment.ClassQuestionDescMapper;
import com.tzcpa.model.student.*;
import com.tzcpa.model.treatment.TeamIntermediateDO;
import com.tzcpa.service.student.DashboardRankingService;
import com.tzcpa.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 月销量列表信息
 * @author WangYao
 * 2019年5月21日
 */
@Service
@Slf4j
public class DashboardRankingServiceImpl implements DashboardRankingService{

	@Resource
	private DashboardRankingMapper dashboardRankingMapper;

	@Resource
	private StuAnswerMapper stuAnswerMapper;

	@Resource
	private ClassQuestionDescMapper classQuestionDescMapper;
	
	/**
	 * 月销量列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public Map<String,List<MonthlySales>> getMonthlySalesList(int year, int classId, int teamId) {
		log.info("月销量列表信息 year={},classId={},teamId={}",year,classId,teamId);
		Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
		System.out.println("currentMonth="+currentMonth);
		TeamIntermediateDO intermediateDO = new TeamIntermediateDO();
		intermediateDO.setClassId(classId);
		intermediateDO.setTeamId(teamId);
		intermediateDO.setYear(year);
		int nowYear = DateUtil.getDateAttr(currentMonth, 1);
		int month = 12;
		if(nowYear == year){
			month = DateUtil.getDateAttr(currentMonth, 2)+1;
		}
		intermediateDO.setMonth(month);
		// 判断S80是否停产
		if(Constant.SEVENYEAR == year || Constant.EIGHTYEAR == year){
			StuAnswerDO answer = stuAnswerMapper.getAnswer(classId, teamId, Constant.PRODUCTION_DECISIONS);
			log.info("单车材料成本列表信息 answer={}",JSON.toJSONString(answer));
			// 停产
			if (Constant.PRODUCTION_DECISIONS_ANSWER.equals(answer.getAnswer())) {
				intermediateDO.setVehicleModel("h8");
			}
		}
		List<MonthlySales> list = dashboardRankingMapper.getMonthlySalesList(intermediateDO);
		Map<String, List<MonthlySales>> mapList = list.stream().collect(Collectors.groupingBy(MonthlySales::getVehicleModel));
		log.info("月销量列表信息 list={}",JSON.toJSONString(mapList));
		return mapList;
	}
	
	/**
	 * 团队价格列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public Map<String,List<TeamPrice>> getTeamPriceList(int year, int classId, int teamId) {
		log.info("团队价格列表信息 year={},classId={},teamId={}",year,classId,teamId);
		Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
		TeamIntermediateDO intermediateDO = new TeamIntermediateDO();
		intermediateDO.setClassId(classId);
		intermediateDO.setTeamId(teamId);
		intermediateDO.setYear(year);
		int nowYear = DateUtil.getDateAttr(currentMonth, 1);
		int month = 12;
		if(nowYear == year){
			month = DateUtil.getDateAttr(currentMonth, 2)+1;
		}
		intermediateDO.setMonth(month);
		// 判断S80是否停产
		if(Constant.SEVENYEAR == year || Constant.EIGHTYEAR == year){
			StuAnswerDO answer = stuAnswerMapper.getAnswer(classId, teamId, Constant.PRODUCTION_DECISIONS);
			log.info("单车材料成本列表信息 answer={}",JSON.toJSONString(answer));
			// 停产
			if (Constant.PRODUCTION_DECISIONS_ANSWER.equals(answer.getAnswer())) {
				intermediateDO.setVehicleModel("h8");
			}
		}
		List<TeamPrice> list = dashboardRankingMapper.getTeamPriceList(intermediateDO);
		Map<String, List<TeamPrice>> mapList = list.stream().collect(Collectors.groupingBy(TeamPrice::getVehicleModel));
		log.info("团队价格列表信息 mapList={}",JSON.toJSONString(mapList));
		return mapList;
	}
	
	/**
	 * 单车材料成本列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public Map<String, List<SingleMaterialCost>> getSingleMaterialCostList(int year, int classId, int teamId) {
		log.info("单车材料成本列表信息 year={},classId={},teamId={}",year,classId,teamId);
		Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
		TeamIntermediateDO intermediateDO = new TeamIntermediateDO();
		intermediateDO.setClassId(classId);
		intermediateDO.setTeamId(teamId);
		intermediateDO.setYear(year);
		int nowYear = DateUtil.getDateAttr(currentMonth, 1);
		int month = 12;
		if(nowYear == year){
			month = DateUtil.getDateAttr(currentMonth, 2)+1;
		}
		intermediateDO.setMonth(month);

		if(Constant.ONEYEAR == year || Constant.TWOYEAR== year || Constant.THREEYEAR== year){
			intermediateDO.setVehicleModel("2013h8");
		}
		if(Constant.FOURYEAR == year || Constant.FIVEYEAR== year || Constant.SIXYEAR== year){
			intermediateDO.setVehicleModel("wey");
		}
		// 判断S80是否停产
		if(Constant.SEVENYEAR == year || Constant.EIGHTYEAR == year){
			StuAnswerDO answer = stuAnswerMapper.getAnswer(classId, teamId, Constant.PRODUCTION_DECISIONS);
			log.info("单车材料成本列表信息 answer={}",JSON.toJSONString(answer));
			// 停产
			if (Constant.PRODUCTION_DECISIONS_ANSWER.equals(answer.getAnswer())) {
				intermediateDO.setVehicleModel("h8");
			}
		}
		List<SingleMaterialCost> list = dashboardRankingMapper.getSingleMaterialCostList(intermediateDO);
		Map<String, List<SingleMaterialCost>> mapList = list.stream().collect(Collectors.groupingBy(SingleMaterialCost::getVehicleModel));
		log.info("单车材料成本列表信息 mapList={}",mapList);
		return mapList;
	}

	/**
	 * 市场份额占比
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public Map<String, Object> getMarketShare(int year, Integer classId, Integer teamId) {
		log.info("市场份额占比 year={},classId={},teamId={}",year,classId,teamId);
		int answerMonth = stuAnswerMapper.getAnswerMonth(classId, teamId, year);
		log.info("查询答题月份 answerMonth={}",answerMonth);
		HashMap<String, Object> map = new HashMap<>();
		if(answerMonth == 0){
			return map;
		}
		TeamIntermediateDO intermediateDO = new TeamIntermediateDO();
		intermediateDO.setClassId(classId);
		intermediateDO.setTeamId(teamId);
		intermediateDO.setYear(year);
		intermediateDO.setMonth(answerMonth);
		List<Map<String,Object>> list = dashboardRankingMapper.getMarketShareByGroup(intermediateDO);
		List<Map<String,Object>> totalList = dashboardRankingMapper.getMarketShare(intermediateDO);
		map.put("list", list);
		map.put("totalList", totalList);
		log.info("单车材料成本列表信息 map={}",JSON.toJSONString(map));
		return map;
	}

	/**
	 * 产品销售收入占比
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getSalesRevenue(int year, Integer classId, Integer teamId) {
		log.info("产品销售收入占比 year={},classId={},teamId={}",year,classId,teamId);
		TeamIntermediateDO intermediateDO = new TeamIntermediateDO();
		intermediateDO.setClassId(classId);
		intermediateDO.setTeamId(teamId);
		intermediateDO.setYear(year);
		List<Map<String,Object>> list = dashboardRankingMapper.getSalesRevenue(intermediateDO);
		log.info("产品销售收入占比 list={}",list);
		return list;
	}

	/**
	 * 产品累计营业利润占比
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getOperatingProfit(int year, Integer classId, Integer teamId) {
		log.info("产品累计营业利润占比 year={},classId={},teamId={}",year,classId,teamId);

		TeamIntermediateDO intermediateDO = new TeamIntermediateDO();
		intermediateDO.setClassId(classId);
		intermediateDO.setTeamId(teamId);
		intermediateDO.setYear(year);
		//2013年不查询h8
		if(Constant.ONEYEAR == year || Constant.TWOYEAR== year || Constant.THREEYEAR== year){
			intermediateDO.setVehicleModel("2013h8");
		}
		if(Constant.FOURYEAR == year || Constant.FIVEYEAR== year || Constant.SIXYEAR== year){
			intermediateDO.setVehicleModel("wey");
		}
		// 判断S80是否停产
		if(Constant.SEVENYEAR == year || Constant.EIGHTYEAR == year){
			StuAnswerDO answer = stuAnswerMapper.getAnswer(classId, teamId, Constant.PRODUCTION_DECISIONS);
			log.info("产品累计营业利润占比 answer={}",JSON.toJSONString(answer));
			// 停产
			if (Constant.PRODUCTION_DECISIONS_ANSWER.equals(answer.getAnswer())) {
				intermediateDO.setVehicleModel("h8");
			}
		}

		List<Map<String,Object>> list = dashboardRankingMapper.getOperatingProfit(intermediateDO);
		log.info("产品累计营业利润占比 list={}",list);
		return list;
	}

	/**
	 * 月净利润、营业收入、销售毛利图表
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getMonthlyOperatingIncome(int year, Integer classId, Integer teamId) {
		log.info("月净利润、营业收入、销售毛利图表 year={},classId={},teamId={}",year,classId,teamId);
		Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
		TeamMonthlyProfitStatementDO monthlyProfitStatementDO = new TeamMonthlyProfitStatementDO();
		monthlyProfitStatementDO.setClassId(classId);
		monthlyProfitStatementDO.setTeamId(teamId);
		monthlyProfitStatementDO.setYear(year);
		int nowYear = DateUtil.getDateAttr(currentMonth, 1);
		int month = 12;
		if(nowYear == year){
			month = DateUtil.getDateAttr(currentMonth, 2)+1;
		}
		monthlyProfitStatementDO.setMonth(month);
		List<Map<String,Object>> list = dashboardRankingMapper.getMonthlyOperatingIncome(monthlyProfitStatementDO);
		log.info("月净利润、营业收入、销售毛利图表 list={}",list);
		return list;
	}

	/**
	 * 月销售毛利率，销售净利率，营业利润率图表
	 * @author WangYao
	 * @date 2019年5月23日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public List<Map<String,Object>> getMonthlyGrossMargin(int year, Integer classId, Integer teamId) {
		log.info("月销售毛利率，销售净利率，营业利润率图表 year={},classId={},teamId={}",year,classId,teamId);
		Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
		log.info("currentMonth = {}",currentMonth);
		TeamMonthlyProfitStatementDO monthlyProfitStatementDO = new TeamMonthlyProfitStatementDO();
		monthlyProfitStatementDO.setClassId(classId);
		monthlyProfitStatementDO.setTeamId(teamId);
		monthlyProfitStatementDO.setYear(year);
		int nowYear = DateUtil.getDateAttr(currentMonth, 1);
		int month = 12;
		if(nowYear == year){
			month = DateUtil.getDateAttr(currentMonth, 2)+1;
		}
		monthlyProfitStatementDO.setMonth(month);
		List<Map<String,Object>> list = dashboardRankingMapper.getMonthlyGrossMargin(monthlyProfitStatementDO);
		log.info("月销售毛利率，销售净利率，营业利润率图表 list={}",list);
		return list;
	}
}
