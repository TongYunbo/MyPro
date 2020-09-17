package com.tzcpa.service.question.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.SearchFinanceConstant;
import com.tzcpa.constant.VNCConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.exceptions.GetValueException;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.model.student.SearchFinanceDTO;
import com.tzcpa.service.question.ARDEIService;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.FormatNumberUtils;

import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description: 研发费用投入8</p>
 * @author WTL
 * @date 2019年5月23日
 */
@Service("rDEIEightService")
@SuppressWarnings("rawtypes")
public class RDEIEightServiceImpl extends ARDEIService {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Resource
	private QuestionService questionService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception {
		try {
			HseRequest hse = hrList.get(0);
			List<String[]> vList = new ArrayList<>();
			vList.add(new String[]{VariableConstant.YF14H6, NormalConstant.H6,
					VariableConstant.YFYX14H61 + "," + VariableConstant.YFYX14H62});
			vList.add(new String[]{VariableConstant.YF14WEY, NormalConstant.WEY,
					VariableConstant.YFYXWEY1 + "," + VariableConstant.YFYXWEY2});
			
			//获取本年最终收入（营业收入）
			Long operationVal = osMapper.getFinanceVal(new SearchFinanceDTO(hse, SearchFinanceConstant.TTPS_TARGET_TABLE,
					SearchFinanceConstant.OR_TARGET_COLUMN,
					Integer.valueOf(hse.getTimeLine().substring(0, 4)), null/*"and vehicle_model = '"+ vModel +"'"*/));
			//获取营业收入为0的时候返回数据为null
			if (operationVal == null) {
				throw new GetValueException("获取"+ Integer.valueOf(hse.getTimeLine().substring(0, 4)) +"年的营业收入时为：" + operationVal);
			}
			
			//获取需要填报研发费用占本年总收入的最小系数
			Double confVariable = Double.valueOf(osMapper.getConfVariable(VNCConstant.YFTRMINXS, Integer.valueOf(hse.getTimeLine().substring(0, 4))));
			//如果填报的研发费用达不到本年收入乘最小系数值，则置成本年收入乘最小系数值
			if (operationVal * confVariable > Double.valueOf(hse.getAnswer().get(0)) * 1000000) {
				List<String> list = new ArrayList<>();
				list.add(FormatNumberUtils.formatDouble(operationVal * confVariable / 1000000, FormatNumberUtils.FORMAT_DOUBLE_DECIMAL_ZERO));
				hse.setAnswer(list);
			}
			
			//添加研报费用
			((ARDEIService) AopContext.currentProxy()).doAddRDValue(osMapper, hse);
			//只做积分不做影响
			((ARDEIService) AopContext.currentProxy()).doScore(new ArrayList<String[]>(), vList,
					new AnswerScoreDO(hse.getClassId(), hse.getTeamId(), hse.getTimeLine(), hse.getQuestionId()), hse, false, osMapper);

			//做题过程中 研发费用填报的题目
			itidService.updateInitInDevelopmentCost(assemblyHandleRDEIVariables(hse));
			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}

}

