package com.tzcpa.service.question.impl;

import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.VNCConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;

import com.tzcpa.service.treatment.InitTeamIntermediateService;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;


/**
 * <p>Description:总体战略选择 </p>
 *
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service()
@SuppressWarnings("rawtypes")
public class AllStrateServiceImpl extends AHseService {

    @Resource
    private OSMapper osMapper;

    @Resource
    private InitTeamIntermediateService initTeamIntermediateService;



	@Override
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		log.info("接收到的数据信息为：" + JSON.toJSONString(hrList));
		try {
			AnswerScoreDO scoreDO = null;
			List<Map<String, Object>> checkRes = null;
			for (HseRequest hse : hrList) {
				scoreDO = new AnswerScoreDO(hse);
				checkRes = osMapper.checkRes(scoreDO);
                ((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
			}

			// wangbj 增加研发费用
			String temp = osMapper.selectConfVariable(VNCConstant.YFFY2010);
			log.info(temp);
			Double developmentCost = Double.parseDouble(temp) / 1000000d;
			log.info(developmentCost.toString());
			HseRequest hseRequest = hrList.get(0);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("classId", hseRequest.getClassId());
			map.put("teamId", hseRequest.getTeamId());
			map.put("year", hseRequest.getTimeLine().substring(0, 4));
			map.put("inDevelopmentCost", developmentCost.toString());
				initTeamIntermediateService.updateInitInDevelopmentCost(map);

			// 初始化下一年的填报研发费用数据
			initTeamIntermediateService.initNextYearRDCost(osMapper, scoreDO.newHseInstance(), initTeamIntermediateService);
			return ResponseResult.success();
		}catch (Exception e){
			log.error("计算影响失败",e);
			throw e;
		}
	}

}
