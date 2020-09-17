package com.tzcpa.service.question.impl;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;


/**
 * <p>Description:部门业绩评价指标分析（此题为主观题，并且无影响只需要添加答案） </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Service
@SuppressWarnings("rawtypes")
public class DepartmentalPerformanceServiceImpl extends AHseService {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;

	@Override
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		//@处理注释
		//题为在月底，所以执行延迟任务 WTL
//		handleDelayedUpdate(osMapper, hrList.get(0), itidService);
		return ResponseResult.success();
	}
	
	

}
