package com.tzcpa.service.question.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:价值链分析 </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service("ValueAnalysisServiceImpl")
@SuppressWarnings("rawtypes")
public class ValueAnalysisServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		log.info("传过来的问题数据集合为：{}", hrList);
		try {
			AnswerScoreDO scoreDO = null;
			//定义答对题目变量
			int count = 0;
			//存储变量信息的集合
			List<String[]> viList = new ArrayList<>();
			for (HseRequest hse : hrList) {
				scoreDO = new AnswerScoreDO(hse);
				//做学生积分处理
				count = ((ValueAnalysisServiceImpl) AopContext.currentProxy()).doScore(scoreDO, hse, count);
			}
			double xL = (count - 9) * 0.01;
			viList.add(new String[]{VariableConstant.XL, NormalConstant.REPLACEMENT_VALUE, String.valueOf(xL), null, null});
			//去除答案过滤选项
			if(scoreDO!=null){
				scoreDO.setAnswer(null);
				scoreDO.setQuestionId(NormalConstant.JZL_ZT_QUESTION_ID);
			}

			//对销量加入延迟影响
			((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, viList);

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
	public int doScore(AnswerScoreDO scoreDO, HseRequest hse,int count) throws Exception{
		try {
			//将传入答案置空去除筛选
			scoreDO.setAnswer(null);
			//根据选择的答案去查找标准答案返回数据说明正确
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			if (checkRes != null && !checkRes.isEmpty()) {
				//设定每道题添加分数初始值
				double scoreAmount = 0;
				//查询除答案只有一条数据所以直接取第一条数据
				Map<String, Object> crMap = checkRes.get(0);
				//后台标准答案
				List<String> jsonToList = JsonUtil.jsonToList(crMap.get("answer").toString(), String.class);
				//前台传入答案
				List<String> answerPar = hse.getAnswer();
				for (int i = 0; i < answerPar.size(); i++) {
					if (jsonToList.contains(answerPar.get(i))) {
						scoreAmount += Double.valueOf(crMap.get("score").toString());
						count++;
					}
				}
				checkRes.get(0).put("score", scoreAmount);
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
			}
			return count;
		}catch (Exception e){
			throw e;
		}
	}

	

}
