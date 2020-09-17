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
 * <p>Description:运营-流程优化 </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service("ProcessOptimizationServiceImpl")
@SuppressWarnings("rawtypes")
public class ProcessOptimizationServiceImpl extends AHseService  {
	
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
		try {
			AnswerScoreDO scoreDO = null;
			//设定每道题添加分数初始值
//		int scoreAmount = 0;
			List<Map<String, Object>> checkRes = null;

			//27题答案
			String answer27 = "";
			//主题26题答案
			String answer26 = "";
			//循环执行问题
			for (HseRequest hse : hrList) {
				scoreDO = new AnswerScoreDO(hse);

				//根据选择的答案去查找标准答案返回数据说明正确
				checkRes = osMapper.checkRes(scoreDO);
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);

				if (hse.getQuestionId().equals(NormalConstant.YYLCYH_M_QUESTION_ID + 1)) {
					answer27 += "\"" + hse.getAnswer().get(0) + "\"";
				}
				answer26 += "\"" + hse.getAnswer().get(0) + "\",";
			}

			//添加主题ID及主题答案
			scoreDO.setQuestionId(NormalConstant.YYLCYH_M_QUESTION_ID);
			//设置查询影响的答案
			scoreDO.setAnswer("[" + answer26.substring(0, answer26.length() - 1) + "]");
			//销量加入延迟影响
			((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
			//设计平衡积分卡
			//设置查询变量信息的答案
			scoreDO.setAnswer("[" + answer27 + "]");
			BalanceVariableDO balanceVariableDO = osMapper.getVariableInfo(VariableConstant.YYLCYH, scoreDO.getAnswer(), scoreDO.getStrategicChoice(), scoreDO.getClassId());
			balanceVariableDO.setTC(scoreDO.getClassId(), scoreDO.getTeamId());
			log.info("根据变量名称查找变量信息 balanceVariableDO={}", JSON.toJSONString(balanceVariableDO));
			osMapper.addBalancedVariable(balanceVariableDO);

			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 做答案处理(没有用到，捋题的时候发现没有用，但忘记了之前做题时的思路，所以目前保留，之前调用处加注释保留 WTL 2019-05-29)
	 * @param scoreDO
	 * @param scoreAmount
	 * @param hse
	 */
	@SuppressWarnings("unused")
	private void doScore(AnswerScoreDO scoreDO, int scoreAmount, HseRequest hse) throws Exception{
		//将传入答案置空去除筛选
		scoreDO.setAnswer(null);
		//根据选择的答案去查找标准答案返回数据说明正确
		List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
		//由于非空判断，将答案重新赋值
		scoreDO.setAnswer(String.valueOf(hse.getAnswer()));
		//选择正确后进行添加积分（也有可能选择正确后有影响，目前没不兼容，只是组织结构）
		if (checkRes != null && !checkRes.isEmpty()) {
			//计算得分合计
			for (Map<String, Object> crMap : checkRes) {
				//后台获取到答案
				String answerList = (String) crMap.get("answer");
				List<String> jsonToList = JsonUtil.jsonToList(answerList, null);
				//前台传入答案
				List<String> answerPar = hse.getAnswer();
				//List<String> jsonParList = JsonUtil.jsonToList(answerPar, null);
				String[] array=answerPar.toArray(new String[answerPar.size()]);
				for (int i = 0; i < array.length; i++) {
			         String ans=array[i];
			         if(jsonToList.contains(ans)){ 
			        	 scoreAmount += (int) crMap.get("score");
			          }
			     }
			}
			checkRes.get(0).put("score", scoreAmount);
			handleAddScore(osMapper, hse, checkRes);
		}
		
	}
	

}
