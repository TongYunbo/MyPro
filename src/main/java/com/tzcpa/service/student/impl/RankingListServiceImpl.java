package com.tzcpa.service.student.impl;

import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.TeacherInfoConstant;
import com.tzcpa.mapper.student.RankingListMapper;
import com.tzcpa.mapper.treatment.ClassQuestionDescMapper;
import com.tzcpa.model.student.SyntheticAbilityDO;
import com.tzcpa.model.student.SyntheticAbilityDTO;
import com.tzcpa.service.student.RankingListService;
import com.tzcpa.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 排行榜
 * @author WangYao
 * 2019年6月3日
 */
@Service
@Slf4j
public class RankingListServiceImpl implements RankingListService{

	@Resource
	private RankingListMapper rankingListMapper;

	@Resource
	private ClassQuestionDescMapper classQuestionDescMapper;

	/**
	 * 盈利能力列表
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getProfitabilityList(int classId) {
		log.info("盈利能力列表  classId={} ", classId);
		List<Map<String, Object>> list = rankingListMapper.getProfitabilityList(classId);
		log.info("盈利能力列表  list={}", JSON.toJSONString(list));
		return list;
	}

	/**
	 * 收入能力列表
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getEarningPowerList(int classId) {
		log.info("收入能力列表  classId={} ", classId);
		List<Map<String, Object>> list = rankingListMapper.getEarningPowerList(classId);
		log.info("收入能力列表  list={} ", list);
		return list;
	}

	/**
	 * 资产规模列表
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getAssetSizeList(int classId) {
		log.info("资产规模列表  classId={} ", classId);
		Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
		int nowYear = DateUtil.getDateAttr(currentMonth, Calendar.YEAR);
        int month = DateUtil.getDateAttr(currentMonth, Calendar.MONTH) + 1;
        int day = DateUtil.getDateAttr(currentMonth, Calendar.DAY_OF_MONTH);
		List<Map<String, Object>> list;
		if(nowYear != 2010){
		    if(month == 12 && day==31){
            }else{
		        nowYear --;
            }
			list = rankingListMapper.getAssetSizeList(classId,nowYear);
		}else{
			list = new ArrayList<>(0);
		}

		log.info("资产规模列表  list={} ", list);
		return list;
	}

	/**
	 * 执行能力列表
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getExecutiveCapabilityList(int classId) {
		log.info("执行能力列表  classId={} ", classId);
		List<Map<String, Object>> list = rankingListMapper.getExecutiveCapabilityList(classId);
		log.info("执行能力列表  list={} ", list);
		return list;
	}

	/**
	 * CEO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCEOScoreList(int classId) {
		log.info("CEO得分  classId={} ", classId);
		List<Map<String, Object>> list = rankingListMapper.getCEOScoreList(classId);
		log.info("CEO得分  list={} ", list);
		return list;
	}

	/**
	 * CMO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCMOScoreList(int classId) {
		log.info("CMO得分  classId={} ", classId);
		List<Map<String, Object>> list = rankingListMapper.getCMOScoreList(classId);
		log.info("CMO得分  list={} ", list);
		return list;
	}

	/**
	 * COO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCOOScoreList(int classId) {
		log.info("COO得分  classId={} ", classId);
		List<Map<String, Object>> list = rankingListMapper.getCOOScoreList(classId);
		log.info("COO得分  list={} ", list);
		return list;
	}
	
	/**
	 * CFO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCFOScoreList(int classId) {
		log.info("CFO得分  classId={} ", classId);
		List<Map<String, Object>> list = rankingListMapper.getCFOScoreList(classId);
		log.info("CFO得分  list={} ", list);
		return list;
	}
	
	/**
	 * CRO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCROScoreList(int classId) {
		log.info("CRO得分  classId={} ", classId);
		List<Map<String, Object>> list = rankingListMapper.getCROScoreList(classId);
		log.info("CRO得分  list={} ", list);
		return list;
	}

	/**
	 * 影响综合能力排名列表
	 * @author WangYao
	 * @date 2019年6月4日
	 * @param classId
	 * @return
	 */
	public List<Map<String,Object>> getAffectSyntheticAbility(int classId) {
		log.info("影响综合能力排名列表  classId={} ", classId);

		// 获取得分
		Map<Integer, Float> weightMap = new HashMap<Integer, Float>();
		List<Map<String, Object>> weightList = rankingListMapper.getWeightMap(classId);
		log.info("weightList={}",JSON.toJSONString(weightList));
		weightList.forEach(map->{
			weightMap.put(Integer.valueOf(map.get("valueOfItem").toString()),Float.valueOf(map.get("weight").toString()));
		});

		List<SyntheticAbilityDO> resultMapList = new ArrayList<SyntheticAbilityDO>();
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();

		//1.系统计分卡考核得分累计
		List<SyntheticAbilityDTO> scorecardTotalScoreList = rankingListMapper.getScorecardTotalScoreList(classId);
		if(null != scorecardTotalScoreList && scorecardTotalScoreList.size()!=0){
			log.info("系统计分卡考核得分累计  ScorecardTotalScoreList={}",scorecardTotalScoreList);
			Map<Double, List<SyntheticAbilityDTO>> scorecardTotalScoreMap = scorecardTotalScoreList.stream().collect(Collectors.groupingBy(SyntheticAbilityDTO::getRanking, LinkedHashMap::new, Collectors.toList()));
			this.computeScore(classId, weightMap, resultMapList, scorecardTotalScoreMap,TeacherInfoConstant.one);
		}else{
			log.info("系统计分卡考核得分累计  数据为空");
		}

		//2.团队操作积分累计
		List<SyntheticAbilityDTO> teamTotalScoreList = rankingListMapper.getTeamTotalScoreList(classId);
		if(null != teamTotalScoreList && teamTotalScoreList.size()!=0){
			log.info("团队操作积分累计  teamTotalScoreList={}",teamTotalScoreList);
			Map<Double, List<SyntheticAbilityDTO>> teamTotalScoreMap = teamTotalScoreList.stream().collect(Collectors.groupingBy(SyntheticAbilityDTO::getRanking, LinkedHashMap::new, Collectors.toList()));
			this.computeScore(classId, weightMap, resultMapList, teamTotalScoreMap,TeacherInfoConstant.two);
		}else{
			log.info("团队操作积分累计  数据为空");
		}
		
		//3.净利润（加总）
		List<SyntheticAbilityDTO> retainedProfitsTotalList = rankingListMapper.getRetainedProfitsTotalList(classId);
		if(null != retainedProfitsTotalList && retainedProfitsTotalList.size()!=0){
			log.info("净利润（加总）  retainedProfitsTotalList={}",retainedProfitsTotalList);
			Map<Double, List<SyntheticAbilityDTO>> retainedProfitsTotalMap = retainedProfitsTotalList.stream().collect(Collectors.groupingBy(SyntheticAbilityDTO::getRanking, LinkedHashMap::new, Collectors.toList()));
			this.computeScore(classId, weightMap, resultMapList, retainedProfitsTotalMap,TeacherInfoConstant.three);
		}else{
			log.info("净利润（加总）  数据为空");
		}
		
		//4.营业收入（加总）
		List<SyntheticAbilityDTO> operationRevenueTotalList = rankingListMapper.getOperationRevenueTotalList(classId);
		if(null != operationRevenueTotalList && operationRevenueTotalList.size()!=0){
			log.info("营业收入（加总）  operationRevenueTotalList={}",operationRevenueTotalList);
			Map<Double, List<SyntheticAbilityDTO>> operationRevenueTotalMap = operationRevenueTotalList.stream().collect(Collectors.groupingBy(SyntheticAbilityDTO::getRanking, LinkedHashMap::new, Collectors.toList()));
			this.computeScore(classId, weightMap, resultMapList, operationRevenueTotalMap,TeacherInfoConstant.four);
		}else{
			log.info("营业收入（加总）   数据为空");
		}
		
		//5.销售毛利率（平均）
		List<SyntheticAbilityDTO> grossProfitMarginTotalList = rankingListMapper.getGrossProfitMarginTotalList(classId);
		if(null !=  grossProfitMarginTotalList && grossProfitMarginTotalList.size()!=0){
			log.info("销售毛利率（平均）  grossProfitMarginTotalList={}",grossProfitMarginTotalList);
			Map<Double, List<SyntheticAbilityDTO>> grossProfitMarginTotalMap = grossProfitMarginTotalList.stream().collect(Collectors.groupingBy(SyntheticAbilityDTO::getRanking, LinkedHashMap::new, Collectors.toList()));
			this.computeScore(classId, weightMap, resultMapList, grossProfitMarginTotalMap,TeacherInfoConstant.five);
		}else{
			log.info("销售毛利率（平均）   数据为空");
		}
		
		//6.总资产（最终报表数）
		List<SyntheticAbilityDTO> totalAssetsList = rankingListMapper.getTotalAssetsList(classId);
		if(null !=  totalAssetsList && totalAssetsList.size()!=0){
			log.info("总资产（最终报表数） totalAssetsList={}",totalAssetsList);
			Map<Double, List<SyntheticAbilityDTO>> totalAssetsMap = totalAssetsList.stream().collect(Collectors.groupingBy(SyntheticAbilityDTO::getRanking, LinkedHashMap::new, Collectors.toList()));
			this.computeScore(classId, weightMap, resultMapList, totalAssetsMap,TeacherInfoConstant.six);
		}else{
			log.info("总资产（最终报表数）   数据为空");
		}
		
		//7.净资产收益率（平均）
		List<SyntheticAbilityDTO> returnOnEquityScoreList = rankingListMapper.getReturnOnEquityScoreList(classId);
		if(null != returnOnEquityScoreList && returnOnEquityScoreList.size()!=0){
			log.info("净资产收益率（平均）  returnOnEquityScoreList={}",returnOnEquityScoreList);
			Map<Double, List<SyntheticAbilityDTO>> returnOnEquityScoreMap = returnOnEquityScoreList.stream().collect(Collectors.groupingBy(SyntheticAbilityDTO::getRanking, LinkedHashMap::new, Collectors.toList()));
			this.computeScore(classId, weightMap, resultMapList, returnOnEquityScoreMap,TeacherInfoConstant.seven);
		}else{
			log.info("净资产收益率（平均）   数据为空");
		}
		
		//8.销量（分三个产品）H6
		List<SyntheticAbilityDTO> h6TotalSalesVolumeList = rankingListMapper.getH6TotalSalesVolumeList(classId);
		if(null != h6TotalSalesVolumeList && h6TotalSalesVolumeList.size()!=0){
			log.info("销量（分三个产品）H6  h6TotalSalesVolumeList={}",h6TotalSalesVolumeList);
			Map<Double, List<SyntheticAbilityDTO>> h6TotalSalesVolumeMap = h6TotalSalesVolumeList.stream().collect(Collectors.groupingBy(SyntheticAbilityDTO::getRanking, LinkedHashMap::new, Collectors.toList()));
			this.computeScore(classId, weightMap, resultMapList, h6TotalSalesVolumeMap,TeacherInfoConstant.eight);
		}else{
			log.info("销量（分三个产品）H6   数据为空");
		}
		
		//9.销量（分三个产品）H8
		List<SyntheticAbilityDTO> h8TotalSalesVolumeList = rankingListMapper.getH8TotalSalesVolumeList(classId);
		if(null != h8TotalSalesVolumeList && h8TotalSalesVolumeList.size()!=0){
			log.info("销量（分三个产品）H8  h8TotalSalesVolumeList={}",h8TotalSalesVolumeList);
			Map<Double, List<SyntheticAbilityDTO>> h8TotalSalesVolumeMap = h8TotalSalesVolumeList.stream().collect(Collectors.groupingBy(SyntheticAbilityDTO::getRanking, LinkedHashMap::new, Collectors.toList()));
			this.computeScore(classId, weightMap, resultMapList, h8TotalSalesVolumeMap,TeacherInfoConstant.nine);
		}else{
			log.info("销量（分三个产品）H8   数据为空");
		}
		
		//10.销量（分三个产品）WEY
		List<SyntheticAbilityDTO> weyTotalSalesVolumeList = rankingListMapper.getWEYTotalSalesVolumeList(classId);
		if(null != weyTotalSalesVolumeList && weyTotalSalesVolumeList.size()!=0){
			log.info("销量（分三个产品）WEY  weyTotalSalesVolumeList={}",weyTotalSalesVolumeList);
			Map<Double, List<SyntheticAbilityDTO>> weyTotalSalesVolumeMap = weyTotalSalesVolumeList.stream().collect(Collectors.groupingBy(SyntheticAbilityDTO::getRanking, LinkedHashMap::new, Collectors.toList()));
			this.computeScore(classId, weightMap, resultMapList, weyTotalSalesVolumeMap,TeacherInfoConstant.ten);
		}else{
			log.info("销量（分三个产品）WEY   数据为空");
		}
		
		log.info("resultMapList={}",JSON.toJSONString(resultMapList));
		Map<String, List<SyntheticAbilityDO>> resultMap = resultMapList.stream().collect(Collectors.groupingBy(SyntheticAbilityDO::getKey, LinkedHashMap::new, Collectors.toList()));
		log.info("resultMap={}",JSON.toJSONString(resultMap));
		resultMap.forEach((K,V)->{
			Map<String,Object> map = new HashMap<>();
			V.forEach(syntheticAbilityDO->{
				if(map.containsKey(syntheticAbilityDO.getKey())){
					map.put("score",Float.valueOf(map.get("score").toString())+ syntheticAbilityDO.getScore());
				}else{
					map.put("score",syntheticAbilityDO.getScore());
					map.put("classId",classId);
					map.put("teamName",syntheticAbilityDO.getTeamName());
					map.put("teamId",syntheticAbilityDO.getTeamId());
					map.put(syntheticAbilityDO.getKey(),syntheticAbilityDO.getKey());
				}
			});
			log.info("map={}",JSON.toJSONString(map));
			mapList.add(map);
		});
		return mapList;
	}

	/**
	 * @Author hanxf
	 * @Date 11:42 2019/6/6
	 * @param classId 班级id
	 * @param weightMap 权重
	 * @param resultMapList 最终计算的得分
	 * @param maps 每项排名信息
	 * @param projectKey
	 * @return void
	**/
	private void computeScore(int classId, Map<Integer, Float> weightMap, List<SyntheticAbilityDO> resultMapList, Map<Double, List<SyntheticAbilityDTO>> maps, int projectKey) {
		int count = 0;
		for (Map.Entry<Double, List<SyntheticAbilityDTO>> entry : maps.entrySet()) {

			List<SyntheticAbilityDTO> entryValue = entry.getValue();
			for (SyntheticAbilityDTO syntheticAbility : entryValue){
				SyntheticAbilityDO syntheticAbilityDO = new SyntheticAbilityDO();
				syntheticAbilityDO.setKey(classId+"_"+syntheticAbility.getTeamId());
				syntheticAbilityDO.setClassId(classId);
				syntheticAbilityDO.setTeamId(syntheticAbility.getTeamId());
				syntheticAbilityDO.setTeamName(syntheticAbility.getTeamName());
				int score = 100 - count * 10;
				if (score < 0) {
					score = 0;
				}
				syntheticAbilityDO.setScore(score * weightMap.get(projectKey));
				log.info("teamName:"+syntheticAbilityDO.getTeamName()+"+++"+"key:"+syntheticAbilityDO.getKey()+"+++"+"score:"+syntheticAbilityDO.getScore()+"+++"+"teamId:"+syntheticAbilityDO.getTeamId());
//				if(resultMap.containsKey("score")){
//					resultMap.put("score",Float.valueOf(resultMap.get("score").toString())+ score * weightMap.get(projectKey));
//				}else{
//					resultMap.put("score",score * weightMap.get(projectKey));
//				}
				resultMapList.add(syntheticAbilityDO);
			}
			count++ ;
		}
	}

	/**
	 * 综合能力排名列表
	 * @author WangYao
	 * @date 2019年6月4日
	 * @param classId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getSyntheticAbility(int classId){
		log.info("综合能力排名列表   classId={}",classId);

		List<Map<String, Object>> resultMapList = getAffectSyntheticAbility(classId);
		log.info("排序前   综合能力排名列表   resultMapList={}", resultMapList);
		if(null != resultMapList && resultMapList.size()!=0){
			Collections.sort(resultMapList, new Comparator<Map<String, Object>>() {

				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					 Integer teamId1 = Integer.valueOf(o1.get("teamId").toString()) ;
				     Integer teamId2 = Integer.valueOf(o2.get("teamId").toString()) ;
					return -teamId2.compareTo(teamId1);
				}
			});
		}
		log.info("排序后   resultMapList={}",resultMapList);
		return resultMapList;
	}
}
