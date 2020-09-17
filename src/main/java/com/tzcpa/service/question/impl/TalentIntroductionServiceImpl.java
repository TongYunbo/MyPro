package com.tzcpa.service.question.impl;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.BalanceVariableDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description：人才引进 </p>
 * @author LRS
 * @date 2019年5月20日
 */
@Slf4j
@Service("TalentIntroductionServiceImpl")
@SuppressWarnings("rawtypes")
public class TalentIntroductionServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Resource
	private QuestionService questionService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		log.info("传过来的问题数据集合为：{}", hrList);
		try{

		AnswerScoreDO scoreDO = null;
		//获取战略
		String sc = osMapper.getStrategic(hrList.get(0), NormalConstant.VEHICLE_MODEL_H6);
		for (HseRequest hse : hrList) {
			scoreDO = new AnswerScoreDO(hse);
			//受战略的影响，查询出选择的战略并赋值给对象
			scoreDO.setStrategicChoice(sc);
			//根据选择的答案去查找标准答案返回数据说明正确。
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			log.info("根据选择的答案去查找标准答案返回数据说明正确 checkRes={}",JsonUtil.listToJson(checkRes));
			//选择正确后进行添加积分
			if (checkRes != null && !checkRes.isEmpty()) {
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
			}
			//材料成本和能耗成本添加团队的延迟执行任务
			((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
			//设计平衡积分卡
			BalanceVariableDO balanceVariableDO= osMapper.getVariableInfo(VariableConstant.GJRCH6,scoreDO.getAnswer(), scoreDO.getStrategicChoice(),scoreDO.getClassId());
			balanceVariableDO.setTC(scoreDO.getClassId(), scoreDO.getTeamId());
			log.info("查询到变量数据信息为：" + JSON.toJSONString(balanceVariableDO));
			osMapper.addBalancedVariable(balanceVariableDO);
			
		}
		
		return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}
	
	

}
