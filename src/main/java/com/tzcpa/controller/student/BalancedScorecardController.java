package com.tzcpa.controller.student;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.student.BalancedScorecard;
import com.tzcpa.service.student.BalancedScorecardService;

/**
 * 平衡记分卡答题卡
 * @author WangYao
 * 2019年5月17日
 */
@RestController
@RequestMapping("/balance")
@Slf4j
public class BalancedScorecardController {

	@Autowired
	private BalancedScorecardService balancedScorecardService;
	
	/**
	 * 查看平衡记分卡答题卡
	 * @author WangYao
	 * @date 2019年5月17日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/list")
	public ResponseResult<List<BalancedScorecard>> getBalancedScList(@RequestBody JSONObject jsonObject){
		log.info("查询平衡记分卡答题卡 jsonObject={}",jsonObject);
		int classId = jsonObject.getInteger("classId");
		int questionId = jsonObject.getInteger("questionId");
		int teamId = jsonObject.getInteger("teamId");
		List<BalancedScorecard> list = balancedScorecardService.getBalancedScList(classId, questionId, teamId);
		log.info("查询团队平衡记分卡答题卡 返回结果 list={}", JSON.toJSONString(list));
		return new ResponseResult<List<BalancedScorecard>>(list);
	}
	
	/**
	 * 查询每年年底平衡记分卡列表
	 * @author WangYao
	 * @date 2019年5月30日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/teamList")
	public ResponseResult<List<Map<String, Object>>> getTeamList(@RequestBody JSONObject jsonObject){
		log.info("查询每年年底平衡记分卡列表 jsonObject={}",jsonObject);
		int classId = jsonObject.getInteger("classId");
		int teamId = jsonObject.getInteger("teamId");
		int year = jsonObject.getInteger("year");
		List<Map<String, Object>> list = balancedScorecardService.getTeamList(classId, teamId, year);
		log.info("查询每年年底平衡记分卡列表 返回结果 list={}", JSON.toJSONString(list));
		return new ResponseResult<List<Map<String, Object>>>(list);
	}
	
	/**
	 * 获取年份
	 * @author WangYao
	 * @date 2019年6月3日
	 * @return
	 */
	@RequestMapping("/getBalancedScorecardYear")
	public ResponseResult<List<Map<String, Object>>> getBalancedScorecardYear(@RequestBody JSONObject jsonObject){
		log.info("获取年份");
		Integer classId = jsonObject.getInteger("classId");
		Integer teamId = jsonObject.getInteger("teamId");
		List<Map<String, Object>> list = balancedScorecardService.getBalancedScorecardYear(classId, teamId);
		log.info("获取年份 返回结果 list={}", JSON.toJSONString(list));
		return new ResponseResult<List<Map<String, Object>>>(list);
	}
}