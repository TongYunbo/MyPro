package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.ClassStrategyMapMapper;
import com.tzcpa.service.treatment.InitClassStrategyMapService;

import lombok.extern.slf4j.Slf4j;

/**
 * 班级战略地图
 * @author WangYao
 * 2019年5月29日
 */
@Service
@Slf4j
public class InitClassStrategyMapServiceImpl implements InitClassStrategyMapService{

	@Resource
	private ClassStrategyMapMapper classStrategyMapMapper;

	/**
	 * 初始化班级战略地图
	 * @author WangYao
	 * @date 2019年5月29日
	 * @param classId
	 */
	@Override
	public void initClassStrategyMap(int classId) {
		log.info("初始化班级战略地图  classId={}",classId);
		classStrategyMapMapper.initClassStrategyMap(classId);
	}
}
