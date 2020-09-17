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
 * <p>Description：产品需求调研 </p>
 * @author LRS
 * @date 2019年5月20日
 */
@Slf4j
@Service("DemandSurveyServiceImpl")
@SuppressWarnings("rawtypes")
public class DemandSurveyServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Resource
	private QuestionService questionService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		try {
			HseRequest hseRequest = hrList.get(0);
			AnswerScoreDO scoreDO = new AnswerScoreDO(hseRequest);
			log.info("接收到的数据信息为：" + JSON.toJSONString(scoreDO));
			//定义变量
			int unit = 0;
			if (!scoreDO.isQualified()) {
				log.info("参数不全：" + hseRequest);
				//返回参数不全
				return ResponseResult.fail("参数不全");
			}
			//受战略的影响，查询出选择的战略并赋值给对象
			scoreDO.setStrategicChoice(osMapper.getStrategic(hseRequest, NormalConstant.VEHICLE_MODEL_H6));
			//设置答案为null，查询出所对应战略的标准答案
			scoreDO.setAnswer(null);
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			log.info("根据选择的答案去查找标准答案返回数据说明正确 checkRes={}", JsonUtil.listToJson(checkRes));
			//处理积分
			((DemandSurveyServiceImpl) AopContext.currentProxy()).doScore(checkRes, hseRequest, osMapper);

			List<String> answer = hseRequest.getAnswer();
			for (int i = 0; i < answer.size(); i++) {
				String answerP = answer.get(i);
				BalanceVariableDO balanceVariableDO = osMapper.getVariableInfo(VariableConstant.XQCP, "[" + JSON.toJSONString(answerP) + "]", scoreDO.getStrategicChoice(), scoreDO.getClassId());
				unit += Integer.valueOf(balanceVariableDO.getVariableVal());
			}
			if (Judge(unit)) {
				//查询延迟影响将战略置为空。
				scoreDO.setStrategicChoice(null);
				//加入延迟任务
				((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
			}
			//设计平衡积分卡
			BalanceVariableDO balanceVariableDO = new BalanceVariableDO();
			balanceVariableDO.setClassId(scoreDO.getClassId());
			balanceVariableDO.setTeamId(scoreDO.getTeamId());
			balanceVariableDO.setVariableName(VariableConstant.XQCP);
			balanceVariableDO.setVariableVal(String.valueOf(unit));
			balanceVariableDO.setUnit(NormalConstant.UNIT_BFH);
			log.info("查询到变量数据信息为：" + JSON.toJSONString(balanceVariableDO));
			osMapper.addBalancedVariable(balanceVariableDO);

			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}
	private boolean Judge(int unit) {
		if(unit<80){
			return true;
		}
		return false;
	}
	
	private void doScore(List<Map<String, Object>> checkRes, HseRequest hse, OSMapper osMapper) throws Exception{
		//所得积分
		double score = 0;
		//本题通过战略查询出的答案只有一条，所以可以直接取下标为0的数据
		Map<String, Object> crMap = checkRes.get(0);
		//获取标准答案
		List<String> answerList = JsonUtil.jsonToList(crMap.get("answer").toString(), String.class);
		for (int i = 0; i < hse.getAnswer().size(); i++) {
			if (answerList.contains(hse.getAnswer().get(i))) {
				score += Double.valueOf(crMap.get("score").toString());
			}
		}
		
		if (score == 0) {
			log.info("本题答案全错所以不得分，所写答案为：{},标准答案为：{}", hse.getAnswer(), crMap.get("answer").toString());
			return;
		}
		checkRes.get(0).put("score", score);
		handleAddScore(osMapper, hse, checkRes);
	}
	

}
