package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.ClassPickupComparisonMapper;
import com.tzcpa.service.treatment.InitClassPickupComparisonService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WangYao
 * 2019年5月30日
 */
@Service
@Slf4j
public class InitClassPickupComparisonServiceImpl implements InitClassPickupComparisonService{

	@Resource
	private ClassPickupComparisonMapper classPickupComparisonMapper;

	/**
	 * 初始化皮卡预算questionId对照表
	 * @author WangYao
	 * @date 2019年5月30日
	 * @param classId
	 */
	@Override
	public void initClassPickupComparison(int classId) {
		log.info("初始化皮卡预算questionId对照表  classId={} ",classId);
		classPickupComparisonMapper.initClassPickupComparison(classId);
	}
}
