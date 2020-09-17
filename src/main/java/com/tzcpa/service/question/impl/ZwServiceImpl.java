package com.tzcpa.service.question.impl;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.question.AStrategicChoicesService;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.student.TeamStrategyMapService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description:自建外购决策影响 </p>
 * @author LRS
 * @date 2019年5月13日
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class ZwServiceImpl extends AStrategicChoicesService  {
	
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
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception {
		try {
			HseRequest hseRequest = hrList.get(0);
			AnswerScoreDO scoreDO = new AnswerScoreDO(hseRequest);
			log.info("接收到的数据信息为：" + JSON.toJSONString(scoreDO));
			if (!scoreDO.isQualified()) {
				log.info("参数不全：" + hseRequest);
				//返回参数不全
				return ResponseResult.fail("参数不全");
			}
			//做选择战略操作
			((AStrategicChoicesService) AopContext.currentProxy()).doStrategicChoices(osMapper, hseRequest, itidService, NormalConstant.H8, tsmService);

			//进行影响处理
			((AStrategicChoicesService) AopContext.currentProxy()).handleFinanceImpact(osMapper, scoreDO, hseRequest, null);
			//20190619修改正常影响加入延迟任务
			((AStrategicChoicesService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);

			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}


}
