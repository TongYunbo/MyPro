package com.tzcpa.service.question.impl;
import com.alibaba.fastjson.JSON;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.student.IQuestionProfitAnalysisService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.FormatNumberUtils;
import com.tzcpa.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * <p>Description:盈利能力判断 </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service("profitJudgmentServiceImpl")
@SuppressWarnings("rawtypes")
public class ProfitJudgmentServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Resource
	private IQuestionProfitAnalysisService paService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		log.info("传过来的问题数据集合为：{}", JsonUtil.listToJson(hrList));
		try {
			//处理第一道题
			HseRequest hse = hrList.get(0);
			AnswerScoreDO scoreDO = null;
			scoreDO = new AnswerScoreDO(hse);
			String jsonAnswer = JSON.toJSONString(hse.getAnswer());
			
			//获取本公司一到六月数据和
			Map<String, BigDecimal> assemblingData = paService.assemblingData(
					Integer.valueOf(hse.getTimeLine().substring(0, 4)), hse.getClassId(),
					hse.getTeamId());
			
//			//查询我公司2018年1-6月毛利（营业收入-营业成本）
//			Double ml = osMapper.findYyLrT(hse);
//			//查询我公司2018年1-6月销量
//			Integer sale = osMapper.findSale(hse);
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
			//单车毛利（万元）=毛利/销量 248
			Double maoLiDanLR = Double.valueOf(FormatNumberUtils.formatDouble(maoLi, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)) / sale * 10000;
			// 单车毛利
			Double dcml = Double.valueOf(FormatNumberUtils.formatDouble(maoLiDanLR, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)) * 10000 * 100;
			
			//处理他公司的单车毛利
			List<Map<String, Object>> tgsData = osMapper.getTGSData(hse.getClassId());
			//营业收入
			Double YYSRT = getTgsData(tgsData, "450") / 10000000000D;
			Double YYSRBLT = Double.valueOf(FormatNumberUtils.formatDouble(YYSRT, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO));
			//销量
			Double saleT = getTgsData(tgsData, "451");
			//营业成本
			Double yyCBT = getTgsData(tgsData, "453") / 10000000000D;
			Double yyCBBLT = Double.valueOf(FormatNumberUtils.formatDouble(yyCBT, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO));
			//营业利润
			Double yyLRT = getTgsData(tgsData, "458") / 10000000000D;
			Double yyLRBLT = Double.valueOf(FormatNumberUtils.formatDouble(yyLRT, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO));
			
			//毛利 =营业收入-营业成本 245
			Double maoLiT = YYSRBLT - yyCBBLT;
			//单车毛利（万元）=毛利/销量 248
			Double maoLiDanLRT = Double.valueOf(FormatNumberUtils.formatDouble(maoLiT, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)) / saleT * 10000;
			// 单车毛利
			Double dcmlT = Double.valueOf(FormatNumberUtils.formatDouble(maoLiDanLRT, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)) * 10000 * 100;
			log.info("单车毛利为：{}", dcmlT);
			//根据选择的答案去查找标准答案返回数据说明正确。
			scoreDO.setAnswer(null);
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			String answer = null;
			if (Double.valueOf(FormatNumberUtils.formatDouble(dcmlT, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO)) 
					> Double.valueOf(FormatNumberUtils.formatDouble(dcml, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO))) {
				answer = "[\"A\"]";
			} else if (Double.valueOf(FormatNumberUtils.formatDouble(dcmlT, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO)) 
					< Double.valueOf(FormatNumberUtils.formatDouble(dcml, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO))) {
				answer = "[\"B\"]";
			} else {
				answer = "[\"C\"]";
			}
			if (answer.equals(jsonAnswer)) {
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
			}
			//处理第二道题
			HseRequest hseT = hrList.get(1);
			scoreDO = new AnswerScoreDO(hseT);
			String jsonAnswerT = JSON.toJSONString(hseT.getAnswer());
//			//查询XX公司营业利润
//			Double LRXXD = osMapper.findLRFromOption(hseT);
//			//List<Double> jsonToListT = JsonUtil.jsonToList(LRXX, Double.class);
//			//查询他公司销量
//			Double TGSXL = osMapper.findXL(hseT);
			//查询他公司单车利润
			//单车利润（万元）=营业利润/销量 247
			Double lrDanT = yyLRBLT / saleT * 10000;
			// 单车利润
			Double dclrT = Double.valueOf(FormatNumberUtils.formatDouble(lrDanT, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)) * 10000 * 100;
			
			
			//本公司
			//单车利润（万元）=营业利润/销量 247
			Double lrDan = yyLRBL / sale * 10000;
			// 单车利润
			Double dclr = Double.valueOf(FormatNumberUtils.formatDouble(lrDan, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_TWO)) * 10000 * 100;
			//查询我公司2018年1-6月营业利润
//			Double lr = osMapper.findYyLr(hse);
		/*for (int i = 0; i < jsonToListT.size(); i++) {
		     LRXXD=jsonToListT.get(i);
		}*/
			if (Double.valueOf(FormatNumberUtils.formatDouble(dclrT, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO)) 
					> Double.valueOf(FormatNumberUtils.formatDouble(dclr, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO))) {
				answer = "[\"A\"]";
			} else if (Double.valueOf(FormatNumberUtils.formatDouble(dclrT, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO)) 
					< Double.valueOf(FormatNumberUtils.formatDouble(dclr, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO))) {
				answer = "[\"B\"]";
			} else {
				answer = "[\"C\"]";
			}
			if (answer.equals(jsonAnswerT)) {
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hseT, checkRes);
			}
			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}
	
	private Double getTgsData(List<Map<String, Object>> tgsData, String questionId){
		Map<String,Object> tgs = null;
		for (int i = 0; i < tgsData.size(); i++) {
			tgs = tgsData.get(i);
			if (tgs.get("questionId").toString().equals(questionId)) {
				break;
			}
		}
		return Double.valueOf(tgs != null ? tgs.get("QOD").toString() : null);
	}


}
