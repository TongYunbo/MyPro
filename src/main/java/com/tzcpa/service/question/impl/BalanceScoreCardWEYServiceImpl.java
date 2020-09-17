package com.tzcpa.service.question.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.BalanceMapper;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.question.BalanceScoreService;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description:平衡记录卡wey</p>
 * @author LRS
 * @date 2019年5月29日
 */
@Slf4j
@Service("balanceScoreCardWEYServiceImpl")
@SuppressWarnings("rawtypes")
public class BalanceScoreCardWEYServiceImpl extends BalanceScoreService {
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private BalanceMapper bsMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Resource
	private QuestionService questionService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception {
		log.info("传过来的问题数据集合为：{}", hrList);
		try {
			AnswerScoreDO scoreDO = null;
			//答案匹配度map
			Map<String, Double> ppdMap = new HashMap<>();
			String beYear = NormalConstant.WEY_YEAR;
			//设定每道题添加分数初始值
			int scoreAmount = 0;
			//问题ID的字符串
			String questionIds = "";
			String answerT = "";
			//循环执行问题
			for (HseRequest hse : hrList) {
				//初始化题的答案
				answerT = "";
				scoreDO = new AnswerScoreDO(hse);
				//做学生积分处理
				((BalanceScoreService) AopContext.currentProxy()).doScore(scoreDO, scoreAmount, hse, osMapper, null, ppdMap);
				List<String> answerList = hse.getAnswer();
				for (int i = 0; i < answerList.size(); i++) {
					answerT += "'[" + JSON.toJSONString(answerList.get(i)) + "]',";
				}

				scoreDO.setAnswer(answerT.substring(0, answerT.length() - 1));
				scoreDO.setIsSelect(1);
				// 进行添加延迟影响
				((BalanceScoreService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);

				questionIds += "'" + hse.getQuestionId() + "',";
			}
			((BalanceScoreService) AopContext.currentProxy()).BalanceAdd(bsMapper, hrList, beYear, NormalConstant.PHJFK_WEY_SC, ppdMap, questionIds.substring(0, questionIds.length() - 1));
			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}
	

}
