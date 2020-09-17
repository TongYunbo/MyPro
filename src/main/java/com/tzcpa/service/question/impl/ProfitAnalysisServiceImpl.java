package com.tzcpa.service.question.impl;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.student.IQuestionProfitAnalysisService;
import com.tzcpa.utils.FormatNumberUtils;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:盈利能力分析 </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service("profitAnalysisService")
@SuppressWarnings("rawtypes")
public class ProfitAnalysisServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private IQuestionProfitAnalysisService paService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		log.info("盈利能力分析传过来的问题数据集合为：{}", JsonUtil.listToJson(hrList));
		try {
			HseRequest hseRequest = hrList.get(0);
			//查询盈利能力分析所对应的答案信息、分数根据传入的问题ID。
			String questionIds = NormalConstant.YLNL_QUESTIONIDS;
			List<Map<String, Object>> selectAnswerList = osMapper.findAnswerScoreByQuestionIds(hseRequest, questionIds);
			int score = (int) selectAnswerList.get(0).get("score");

			//获取本公司一到六月数据和
			Map<String, BigDecimal> assemblingData = paService.assemblingData(
					Integer.valueOf(hseRequest.getTimeLine().substring(0, 4)), hseRequest.getClassId(),
					hseRequest.getTeamId());
			//营业收入
			Double YYSR = assemblingData.get("233").doubleValue() / 10000000000D;
			Double YYSRBL = Double.valueOf(FormatNumberUtils.formatDouble(YYSR, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO));
			//销量
			Double sale = assemblingData.get("234").doubleValue();
			//营业成本
			Double yyCB = assemblingData.get("236").doubleValue() / 10000000000D;
			Double yyCBBL = Double.valueOf(FormatNumberUtils.formatDouble(yyCB, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO));
			//营业利润
			Double yyLR = assemblingData.get("440").doubleValue() / 10000000000D;
			Double yyLRBL = Double.valueOf(FormatNumberUtils.formatDouble(yyLR, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO));
			
			//毛利 =营业收入-营业成本 245
			Double maoLi = YYSRBL - yyCBBL;
			// 毛利率=毛利/营业收入 246
			Double maoLiRate = Double.valueOf(FormatNumberUtils.formatDouble(maoLi, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)) / YYSRBL;
			//单车利润（万元）=营业利润/销量 247
			Double lrDan = yyLRBL / sale * 10000;
			//单车毛利（万元）=毛利/销量 248
			Double maoLiDanLR = Double.valueOf(FormatNumberUtils.formatDouble(maoLi, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)) / sale * 10000;
			//单车售价（万元）营业收入/销量 249
			Double sjDan = YYSRBL / sale * 10000;
			//营业利润率=营业利润/营业收入 250
			Double yyLRate = yyLRBL / YYSRBL;

			// 毛利
			Double mlz = Double.valueOf(FormatNumberUtils.formatDouble(maoLi, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)) * 100000000 * 100;
			addMapDate(selectAnswerList, NormalConstant.ML_QUESTIONIDS,
					Double.valueOf(FormatNumberUtils.formatDouble(mlz, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO)),
					score);
			// 毛利率
			addMapDate(selectAnswerList, NormalConstant.MLR_QUESTIONIDS,
					Double.valueOf(FormatNumberUtils.formatDouble(maoLiRate * 100, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)),
					score);
			// 单车毛利
			Double dcml = Double.valueOf(FormatNumberUtils.formatDouble(maoLiDanLR, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)) * 10000 * 100;
			addMapDate(selectAnswerList, NormalConstant.MLD_QUESTIONIDS,
					Double.valueOf(FormatNumberUtils.formatDouble(dcml, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO)),
					score);
			// 单车利润
			Double dclr = Double.valueOf(FormatNumberUtils.formatDouble(lrDan, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)) * 10000 * 100;
			addMapDate(selectAnswerList, NormalConstant.LRD_QUESTIONIDS,
					Double.valueOf(FormatNumberUtils.formatDouble(dclr, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO)),
					score);
			// 单车售价
			Double dcsj = Double.valueOf(FormatNumberUtils.formatDouble(sjDan, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)) * 10000 * 100;
			addMapDate(selectAnswerList, NormalConstant.SJD_QUESTIONIDS,
					Double.valueOf(FormatNumberUtils.formatDouble(dcsj, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO)),
					score);
			// 营业利润率
			addMapDate(selectAnswerList, NormalConstant.YYLRL_QUESTIONIDS,
					Double.valueOf(FormatNumberUtils.formatDouble(yyLRate * 100, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)),
					score);

			log.info("盈利能力分析答案数据集合为：{}", JsonUtil.listToJson(selectAnswerList));
			//循环执行问题
			for (HseRequest hse : hrList) {
				//选择正确后进行添加积分
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, selectAnswerNew(hse, selectAnswerList));
			}
			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}
	
	private List<Map<String, Object>> selectAnswerNew(HseRequest hse, List<Map<String, Object>> selectAnswerList) {
		List<Map<String, Object>> selectList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (int i = 0; i < selectAnswerList.size(); i++) {
			map = selectAnswerList.get(i);
			//如果题ID和题答案都对上了加积分
			if (Integer.valueOf(map.get("questionId").toString()).equals(hse.getQuestionId())
					&& (Double.valueOf(map.get("answer").toString().replace("[\"", "").replace("\"]", "")).equals((hse.getAnswer().get(0) == null ? null : Double.valueOf(hse.getAnswer().get(0)))))) {
				selectList.add(map);
			}
		}
		return selectList;
	}

	/**
	 * 原有的逻辑
	 * @param scoreDO
	 * @param selectAnswerList
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<Map<String, Object>> selectAnswer(AnswerScoreDO scoreDO, List<Map<String, Object>> selectAnswerList) {
		List<Map<String, Object>> selectList = new ArrayList<Map<String, Object>>();
		
		for (int i = 0; i < selectAnswerList.size(); i++) {
			Integer questionId = (Integer) selectAnswerList.get(i).get("questionId");
			String answer = (String) selectAnswerList.get(i).get("answer");
			String answerScore = scoreDO.getAnswer(); 
			//处理答案中24.0比较24的问题。
			//240为此题对应的问题ID
			if(scoreDO.getQuestionId()==240){
				List<Double> jsonToList = JsonUtil.jsonToList(answer, Double.class);
				Double answerDouble=null;
				for (int j = 0; j < jsonToList.size(); j++) {
					answerDouble = jsonToList.get(0);
				}
				List<Double> jsonToListT = JsonUtil.jsonToList(answerScore, Double.class);
				Double answerScoreDouble=null;
				for (int j = 0; j < jsonToListT.size(); j++) {
					answerScoreDouble = jsonToListT.get(0);
				}
				if(questionId.equals(scoreDO.getQuestionId())&&answerDouble.equals(answerScoreDouble)){
					selectList.add(selectAnswerList.get(i));
					break;
				}
			}else{
				if(questionId.equals(scoreDO.getQuestionId())&&answer.equals(answerScore)){
					selectList.add(selectAnswerList.get(i));
					break;
				} 
			}
		}
		return selectList;
	}

	private void addMapDate(List<Map<String, Object>> selectAnswerList, int questionId,Double answer,int score){
		Map<String, Object> map1 = new HashMap<>();
		map1.put("questionId", questionId);
		map1.put("answer", answer);
		map1.put("score", score);
		selectAnswerList.add(map1);
	}
}



