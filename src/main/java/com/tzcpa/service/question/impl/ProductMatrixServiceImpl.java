package com.tzcpa.service.question.impl;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.student.TeamStrategyMapService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:产品矩阵</p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class ProductMatrixServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private TeamStrategyMapService tsmService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		log.info("传过来的问题数据集合为：{}", hrList);
		try {
			AnswerScoreDO scoreDO = null;
			for (HseRequest hse : hrList) {
				scoreDO = new AnswerScoreDO(hse);
				//根据选择的答案去查找标准答案返回数据说明正确。
				List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
				//选择正确后进行添加积分
				if (checkRes != null && !checkRes.isEmpty()) {
					((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
				}
			}

			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}
	

}
