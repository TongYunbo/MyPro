package com.tzcpa.service.question.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.tzcpa.service.student.AHseService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.question.ARDEIService;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description: 研发费用投入5</p>
 * @author WTL
 * @date 2019年5月22日
 */
@Service("rDEIFiveService")
@SuppressWarnings("rawtypes")
public class RDEIFiveServiceImpl extends ARDEIService {

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
			vList.add(new String[]{VariableConstant.YF14H8, NormalConstant.H8,
					VariableConstant.YFYX14H81 + "," + VariableConstant.YFYX14H82});
			((ARDEIService) AopContext.currentProxy()).handleRDEI(osMapper, hse, false, vList, itidService, questionService);
			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}

}

