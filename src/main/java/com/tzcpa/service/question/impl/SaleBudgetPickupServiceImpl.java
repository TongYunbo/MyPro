package com.tzcpa.service.question.impl;

import com.tzcpa.constant.MessageConstant;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.VNCConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 皮卡车销售预算</p>
 *
 * @author wangbj 皮卡车销售预算
 * @date 2019年5月22日
 */
@Slf4j
@SuppressWarnings("rawtypes")
@Service("saleBudgetPickupService")
public class SaleBudgetPickupServiceImpl extends AHseService {

    @Resource
    private OSMapper osMapper;

    @Override
	@Transactional(rollbackFor = Exception.class)
    public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
    	
    	log.info("皮卡销售预算接收值：" + JsonUtil.listToJson(hrList));
        try {
			// 判断问题数量
			if (!NormalConstant.SALEBUDGETPICKUP_QUESTION_NUM.equals(hrList.size()) && hrList.size() != 1) {
				return ResponseResult.fail(MessageConstant.SALEBUDGETPICKUP_QUESTION_NUM_FAIL);
			}

			List<String[]> viList = new ArrayList<>();
			BigDecimal pickMin = null;
			BigDecimal pickupChange = null;
			//如果为一条说明没有答案直接置为0
			if (hrList.size() != 1) {
				BigDecimal pickupA = BigDecimal.ZERO;
				BigDecimal pickupAadd = new BigDecimal(osMapper.selectConfVariable(VNCConstant.CCPCZ));
				BigDecimal pickupB = BigDecimal.ZERO;
				BigDecimal pickupBprice = new BigDecimal(osMapper.selectConfVariable(VNCConstant.FDJDJ)).multiply(new BigDecimal(1000000));
				BigDecimal pickupBadd = pickupAadd.add(new BigDecimal(osMapper.selectConfVariable(VNCConstant.FDJCZ)));
				BigDecimal pickupC = BigDecimal.ZERO;
				BigDecimal pickupCprice = new BigDecimal(osMapper.selectConfVariable(VNCConstant.BSXDJ)).multiply(new BigDecimal(1000000));
				;
				BigDecimal pickupCadd = pickupAadd.add(new BigDecimal(osMapper.selectConfVariable(VNCConstant.BSXCZ)));
				BigDecimal pickupD = BigDecimal.ZERO;
				BigDecimal pickupDprice = new BigDecimal(osMapper.selectConfVariable(VNCConstant.QTBJDJ)).multiply(new BigDecimal(1000000));
				;
				BigDecimal pickupDadd = pickupAadd.add(new BigDecimal(osMapper.selectConfVariable(VNCConstant.QTBJCZ)));
				BigDecimal pickupE = new BigDecimal(osMapper.selectConfVariable(VNCConstant.YJXL));

				String ast = null;
				// 多小题循环处理
				for (HseRequest hseRequest : hrList) {
					AnswerScoreDO answerScoreDO = new AnswerScoreDO(hseRequest);
					// 单题分数处理
					List<Map<String, Object>> checkRes = osMapper.checkRes(answerScoreDO);
					if (checkRes != null && !checkRes.isEmpty()) {
						((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hseRequest, checkRes);
					}
					// 答案数组json转list
					List<String> answerTempList = hseRequest.getAnswer();
					ast = answerTempList.get(0);
					ast = (ast == null || "null".equals(ast) || StringUtils.isBlank(ast)) ? "0" : ast;
					// 预计生产量
					if (NormalConstant.SALEBUDGETPICKUP_QUESTION_ID_A.equals(answerScoreDO.getQuestionId())) {
						pickupA = new BigDecimal(ast);
					}
					// 预计采购发动机金额
					if (NormalConstant.SALEBUDGETPICKUP_QUESTION_ID_B.equals(answerScoreDO.getQuestionId())) {
						pickupB = new BigDecimal(ast);
					}
					// 预计采购变速箱金额
					if (NormalConstant.SALEBUDGETPICKUP_QUESTION_ID_C.equals(answerScoreDO.getQuestionId())) {
						pickupC = new BigDecimal(ast);
					}
					// 预计采购其他部件金额
					if (NormalConstant.SALEBUDGETPICKUP_QUESTION_ID_D.equals(answerScoreDO.getQuestionId())) {
						pickupD = new BigDecimal(ast);
					}
				}

				// 计算 A，B，C，D 值
				pickupA = pickupA.add(pickupAadd);
				pickupB = pickupB.divide(pickupBprice, 0, BigDecimal.ROUND_HALF_DOWN).add(pickupBadd);
				pickupC = pickupC.divide(pickupCprice, 0, BigDecimal.ROUND_HALF_DOWN).add(pickupCadd);
				pickupD = pickupD.divide(pickupDprice, 0, BigDecimal.ROUND_HALF_DOWN).add(pickupDadd);
				// 取最小数值 min（A,B,C,D,E)
				pickMin = pickupA.min(pickupB).min(pickupC).min(pickupD).min(pickupE);

				//最小的销量不小于或者等于0的时候进行计算成本系数计算
				if (pickMin.compareTo(new BigDecimal(0)) == 1) {
					// 整车单价 B+C+D单价
					BigDecimal pickSumPrice = pickupBprice.add(pickupCprice).add(pickupDprice);
					// B（辆）/min(A,B,C,D,E辆)*B单价/(B+C+D单价)+
					// C（辆）/min(A,B,C,D,E辆)*C单价/(B+C+D单价)+
					// D（辆）/min(A,B,C,D,E辆)*D单价/(B+C+D单价)=皮卡单车材料成本的调整系数
					BigDecimal pickupChangeB = pickupB.divide(pickMin, 8, BigDecimal.ROUND_HALF_DOWN)
							.multiply(pickupBprice).divide(pickSumPrice, 8, BigDecimal.ROUND_HALF_DOWN);
					BigDecimal pickupChangeC = pickupC.divide(pickMin, 8, BigDecimal.ROUND_HALF_DOWN)
							.multiply(pickupCprice).divide(pickSumPrice, 8, BigDecimal.ROUND_HALF_DOWN);
					BigDecimal pickupChangeD = pickupD.divide(pickMin, 8, BigDecimal.ROUND_HALF_DOWN)
							.multiply(pickupDprice).divide(pickSumPrice, 8, BigDecimal.ROUND_HALF_DOWN);
					pickupChange = pickupChangeB.add(pickupChangeC).add(pickupChangeD);
				}
			}

			// 销量影响=基础数据的销量*min（A,B,C,D,E)/131356
			boolean b = pickMin == null || pickMin.compareTo(new BigDecimal(0)) < 1;
			viList.add(new String[]{VariableConstant.PICKUPMIN, NormalConstant.REPLACEMENT_VALUE, b ? "0" : pickMin.toString()});
			// 单车材料成本影响=皮卡单车材料成本*调整系数
			viList.add(new String[]{VariableConstant.PICKUPCHANGE, NormalConstant.REPLACEMENT_VALUE, b ? "0" : pickupChange.toString()});

			// 增加延迟任务
			AnswerScoreDO answerScoreDO = new AnswerScoreDO(hrList.get(0));
			answerScoreDO.setQuestionId(NormalConstant.PKXSYS_QUESTION_ID);
			answerScoreDO.setAnswer(null);
			((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, answerScoreDO, viList);

			return ResponseResult.success();
		}catch (Exception e){
        	throw e;
		}
    }

}
