package com.tzcpa.service.question.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.tzcpa.constant.NormalConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.question.AStrategicChoicesService;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.student.TeamStrategyMapService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description: 生产决策-停产决策（TWEY）</p>
 * @author WTL
 * @date 2019年6月4日
 */
@Service("weyStrategicChoicesServiceImpl")
@SuppressWarnings("rawtypes")
public class WeyStrategicChoicesServiceImpl extends AStrategicChoicesService {

	@Resource
	private OSMapper osMapper;

	@Resource
	private InitTeamIntermediateService itidService;
	
	@Resource
	private QuestionService questionService;
	
	@Resource
	private TeamStrategyMapService tsmService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		try {
			HseRequest hseRequest = hrList.get(0);
			//做选择战略操作
			((WeyStrategicChoicesServiceImpl) AopContext.currentProxy()).doStrategicChoices(osMapper, hseRequest, itidService, NormalConstant.WEY, tsmService);
			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}

}

