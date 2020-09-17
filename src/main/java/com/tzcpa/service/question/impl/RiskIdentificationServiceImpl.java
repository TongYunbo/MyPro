package com.tzcpa.service.question.impl;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.mapper.treatment.TeamIntermediateMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.BalanceVariableDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.DateUtil;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:风险识别H6</p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service("RiskIdentificationServiceImpl")
@SuppressWarnings("rawtypes")
public class RiskIdentificationServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Resource
    private QuestionService questionService;
	
	@Resource
	private TeamIntermediateMapper tiMapper;
	
	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception {
		log.info("传过来的问题数据集合为：{}", hrList);
		try {
			AnswerScoreDO scoreDO = new AnswerScoreDO(hrList.get(0));
			//35题的答案做查询影响的时候用
			String ai35 = "";
			//35题的答案做查询答案积分的时候用
			String ac35 = "";
			//定义答对题目变量；
			double count = 0.0;

			//设置执行任务需要变量
			List<String[]> viList = new ArrayList<>();
			//变量值的基础值
			String vBaseValue = "class_id = " + scoreDO.getClassId() + " AND team_id = " + scoreDO.getTeamId()
					+ " AND `year` = " + scoreDO.getTimeLine().substring(0, 4);

			//本题所在月
			int month = DateUtil.getDateAttr(DateUtil.strToDate(DateUtil.FORMART_4, scoreDO.getTimeLine()), Calendar.MONTH) + 1;
			// 资产减值损失-坏账1
			viList.add(new String[]{VariableConstant.ZCJZSSHZ1, NormalConstant.REPLACEMENT_VALUE,
					vBaseValue + " AND MONTH <= " + month});
			// 资产减值损失-坏账2
			viList.add(new String[]{VariableConstant.ZCJZSSHZ2, NormalConstant.REPLACEMENT_VALUE,
					vBaseValue + " AND MONTH <= " + month});
			//全年营业成本总额(百分之80出发几率)
			viList.add(new String[]{VariableConstant.QNYYCBZE, NormalConstant.JUDGE_EFFECT_REPLACE, vBaseValue, NormalConstant.BFZ80JVFF, null});
			//全年生产工人工资总额
			viList.add(new String[]{VariableConstant.QNSCGRGZZE, NormalConstant.REPLACEMENT_VALUE, vBaseValue});
			//循环执行问题
			for (HseRequest hse : hrList) {
				scoreDO = new AnswerScoreDO(hse);
				count = ((RiskIdentificationServiceImpl) AopContext.currentProxy()).doImpactAndScore(count, scoreDO, hse, null, viList);

				ai35 += "'[\"" + scoreDO.getQuestionId() + "\"]',";
				ac35 += "\"" + scoreDO.getQuestionId() + "\",";
			}

			//处理问题ID为35的主题
			scoreDO.setAnswer("[" + ac35.substring(0, ac35.length() - 1) + "]");
			scoreDO.setQuestionId(NormalConstant.FXSBYD_1_QUESTION_ID);
			count = ((RiskIdentificationServiceImpl) AopContext.currentProxy()).doImpactAndScore(count, scoreDO, hrList.get(0), ai35.substring(0, ai35.length() - 1), viList);

			//设计平衡积分卡
			BalanceVariableDO balanceVariableDO = new BalanceVariableDO();
			balanceVariableDO.setVariableName(VariableConstant.FXSBH6);
			balanceVariableDO.setUnit("%");
			balanceVariableDO.setVariableVal(String.valueOf((count / 12) * 100));
			balanceVariableDO.setClassId(scoreDO.getClassId());
			balanceVariableDO.setTeamId(scoreDO.getTeamId());
			log.info("计算后的变量值为：{}", String.valueOf((count / 12) * 100));
			osMapper.addBalancedVariable(balanceVariableDO);

			return ResponseResult.success();
		} catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 处理影响和积分
	 * @param count
	 * @param scoreDO
	 * @param hse
	 * @param ai35 做35题需要的参数
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public double doImpactAndScore(double count, AnswerScoreDO scoreDO, HseRequest hse, String ai35, List<String[]> viList) throws Exception {
		//设定每道题添加分数初始值
		try {
			int scoreAmount = 0;
			//做学生积分处理
			//如果是35题的话
			if (scoreDO.getQuestionId().equals(NormalConstant.FXSBYD_1_QUESTION_ID)) {
				hse.setQuestionId(scoreDO.getQuestionId());
				count = ((RiskIdentificationServiceImpl) AopContext.currentProxy()).doScore35(scoreDO, scoreAmount, hse, count);
				//赋值做影响处理的答案及设置是否选中
				scoreDO.setAnswer(ai35);
				scoreDO.setIsSelect(1);
			} else { //否则
				count = ((RiskIdentificationServiceImpl) AopContext.currentProxy()).doScoreOther(scoreDO, hse, count);
				scoreDO.setISAndUA();
			}

			//进行影响处理
			((AHseService) AopContext.currentProxy()).handleFinanceImpact(osMapper, scoreDO, hse, viList);
			((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, viList);
			return count;
		}catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 做除主题之外的积分处理
	 * @param scoreDO
	 * @param hse
	 * @param count
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public double doScoreOther(AnswerScoreDO scoreDO, HseRequest hse, double count) throws Exception{
		try {
			//根据选择的答案去查找标准答案返回数据说明正确
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			if (checkRes == null || checkRes.isEmpty()) {
				return count;
			}
			((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
			return ++count;
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
	public double doScore35(AnswerScoreDO scoreDO, int scoreAmount, HseRequest hse,double count) throws Exception{
		try {
			String answer = scoreDO.getAnswer();
			scoreDO.setAnswer(null);
			//根据选择的答案去查找标准答案返回数据说明正确
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);

			scoreDO.setAnswer(answer);
			//选择正确后进行添加积分（也有可能选择正确后有影响，目前没不兼容，只是组织结构）
			if (checkRes != null && !checkRes.isEmpty()) {
				//计算得分合计
				for (Map<String, Object> crMap : checkRes) {
					//后台获取到答案
					String answerList = (String) crMap.get("answer");
					List<String> jsonToList = JsonUtil.jsonToList(answerList, String.class);
					//前台传入答案
					List<String> array = JsonUtil.jsonToList(scoreDO.getAnswer(), String.class);
					for (int i = 0; i < array.size(); i++) {
						String ans = array.get(i);
						if (jsonToList.contains(ans)) {
							scoreAmount += (int) crMap.get("score");
							count += 1;
						}
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
