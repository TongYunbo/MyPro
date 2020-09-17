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
import com.tzcpa.service.student.TeamStrategyMapService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description: H6战略选择2011及2013</p>
 * @author WTL
 * @date 2019年5月24日
 */
@SuppressWarnings("rawtypes")
@Service("h6SCService")
public class H6StrategicChoicesServiceImpl extends AStrategicChoicesService {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itIDService;
	
	@Resource
	private TeamStrategyMapService tsmService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		HseRequest hse = hrList.get(0);
		//做选择战略操作
		try {
			((AStrategicChoicesService) AopContext.currentProxy()).
			doStrategicChoices(osMapper, hse, itIDService, NormalConstant.H6, tsmService);
		return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}

}

