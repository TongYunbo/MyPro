package com.tzcpa.controller.student;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.service.student.RankingListService;

import lombok.extern.slf4j.Slf4j;

/**
 * 排行榜
 * @author WangYao
 * 2019年6月3日
 */
@RestController
@RequestMapping("/rankingList")
@Slf4j
public class RankingListController {

	@Autowired
	private RankingListService rankingListService;
	
	/**
	 * 盈利能力列表
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/profitability")
	public ResponseResult<List<Map<String, Object>>> getProfitabilityList(@RequestBody JSONObject jsonObject){
		log.info("盈利能力列表  jsonObject={}",jsonObject);
		int classId = jsonObject.getInteger("classId");
		List<Map<String, Object>> profitabilityList = rankingListService.getProfitabilityList(classId);
		log.info("盈利能力列表  profitabilityList={} ", profitabilityList);
		return new ResponseResult<List<Map<String,Object>>>(profitabilityList);
	}
	
	/**
	 * 收入能力列表
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/earningPower")
	public ResponseResult<List<Map<String, Object>>> getEarningPowerList(@RequestBody JSONObject jsonObject){
		log.info("收入能力列表   jsonObject={}", jsonObject);
		int classId = jsonObject.getInteger("classId");
		List<Map<String, Object>> earningPowerList = rankingListService.getEarningPowerList(classId);
		log.info("收入能力列表   earningPowerList={}", earningPowerList);
		return new ResponseResult<List<Map<String,Object>>>(earningPowerList);
	}
	
	/**
	 * 资产规模列表
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/assetSize")
	public ResponseResult<List<Map<String, Object>>> getAssetSizeList(@RequestBody JSONObject jsonObject){
		log.info("资产规模列表   jsonObject={}", jsonObject);
		int classId = jsonObject.getInteger("classId");
		List<Map<String, Object>> assetSizeList = rankingListService.getAssetSizeList(classId);
		log.info("资产规模列表   ssetSizeList={}", assetSizeList);
		return new ResponseResult<List<Map<String,Object>>>(assetSizeList);
	}
	
	/**
	 * 执行能力列表
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/executiveCapability")
	public ResponseResult<List<Map<String, Object>>> getExecutiveCapabilityList(@RequestBody JSONObject jsonObject){
		log.info("执行能力列表  jsonObject={}", jsonObject);
		int classId = jsonObject.getInteger("classId");
		List<Map<String, Object>> executiveCapabilityList = rankingListService.getExecutiveCapabilityList(classId);
		log.info("执行能力列表   executiveCapabilityList={}", executiveCapabilityList);
		return new ResponseResult<List<Map<String,Object>>>(executiveCapabilityList);
	}
	
	/**
	 * CEO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/CEOScore")
	public ResponseResult<List<Map<String, Object>>> getCEOScoreList(@RequestBody JSONObject jsonObject){
		log.info("CEO得分  jsonObject={}", jsonObject);
		int classId = jsonObject.getInteger("classId");
		List<Map<String, Object>> CEOScoreList = rankingListService.getCEOScoreList(classId);
		log.info("CEO得分   CEOScoreList={}", CEOScoreList);
		return new ResponseResult<List<Map<String,Object>>>(CEOScoreList);
	}
	
	/**
	 * CMO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/CMOScore")
	public ResponseResult<List<Map<String, Object>>> getCMOScoreList(@RequestBody JSONObject jsonObject){
		log.info("CMO得分  jsonObject={}", jsonObject);
		int classId = jsonObject.getInteger("classId");
		List<Map<String, Object>> CMOScoreList = rankingListService.getCMOScoreList(classId);
		log.info("CMO得分   CMOScoreList={}", CMOScoreList);
		return new ResponseResult<List<Map<String,Object>>>(CMOScoreList);
	}
	
	/**
	 * COO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/COOScore")
	public ResponseResult<List<Map<String, Object>>> getCOOScoreList(@RequestBody JSONObject jsonObject){
		log.info("COO得分  jsonObject={}", jsonObject);
		int classId = jsonObject.getInteger("classId");
		List<Map<String, Object>> COOScoreList = rankingListService.getCOOScoreList(classId);
		log.info("COO得分   COOScoreList={}", COOScoreList);
		return new ResponseResult<List<Map<String,Object>>>(COOScoreList);
	}
	
	/**
	 * CFO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/CFOScore")
	public ResponseResult<List<Map<String, Object>>> getCFOScoreList(@RequestBody JSONObject jsonObject){
		log.info("CFO得分  jsonObject={}", jsonObject);
		int classId = jsonObject.getInteger("classId");
		List<Map<String, Object>> CFOScoreList = rankingListService.getCFOScoreList(classId);
		log.info("CFO得分   CFOScoreList={}", CFOScoreList);
		return new ResponseResult<List<Map<String,Object>>>(CFOScoreList);
	}
	
	/**
	 * CRO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/CROScore")
	public ResponseResult<List<Map<String, Object>>> getCROScoreList(@RequestBody JSONObject jsonObject){
		log.info("CRO得分  jsonObject={}", jsonObject);
		int classId = jsonObject.getInteger("classId");
		List<Map<String, Object>> CROScoreList = rankingListService.getCROScoreList(classId);
		log.info("CRO得分   CROScoreList={}", CROScoreList);
		return new ResponseResult<List<Map<String,Object>>>(CROScoreList);
	}
	
	/**
	 * 综合能力排名列表
	 * @author WangYao
	 * @date 2019年6月4日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/getSyntheticAbility")
	public ResponseResult<List<Map<String, Object>>> getSyntheticAbility(@RequestBody JSONObject jsonObject){
		log.info("综合能力排名列表  jsonObject={}", jsonObject);
		int classId = jsonObject.getInteger("classId");
		List<Map<String, Object>> syntheticAbilityList = rankingListService.getSyntheticAbility(classId);
		log.info("综合能力排名列表   syntheticAbilityList={}", syntheticAbilityList);
		return new ResponseResult<List<Map<String,Object>>>(syntheticAbilityList);
	}
}
