package com.tzcpa.service.question.impl;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:五力分析 </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class ForcesAnalysisServiceImpl extends AHseService  {
	
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
			int scoreAmount = 0;
			//循环执行问题
			for (HseRequest hse : hrList) {
				scoreDO = new AnswerScoreDO(hse);
				//做学生积分处理
				((ForcesAnalysisServiceImpl) AopContext.currentProxy()).doScore(scoreDO, scoreAmount, hse);
			}

			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 做答案处理
	 * @param scoreDO
	 * @param scoreAmount
	 * @param hse
	 */
	@Transactional(rollbackFor = Exception.class)
	public void doScore(AnswerScoreDO scoreDO, int scoreAmount, HseRequest hse) throws Exception{
		try {
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
					List<String> jsonToList = JsonUtil.jsonToList(crMap.get("answer").toString(), String.class);
					//前台传入答案
					List<String> answerPar = hse.getAnswer();
					for (int i = 0; i < answerPar.size(); i++) {
						if (jsonToList.contains(answerPar.get(i))) {
							scoreAmount += (int) crMap.get("score");
						}
					}
				}
				checkRes.get(0).put("score", scoreAmount);
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
			}
		}catch (Exception e){
             throw e;
		}
	}


	

}
