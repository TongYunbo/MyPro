package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.ClassFixedParamMapper;
import com.tzcpa.service.treatment.InitClassFixedParamService;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化固定值比例信息
 * @author WangYao
 * 2019年5月22日
 */
@Service("initClassFixedParamService")
@Slf4j
public class InitClassFixedParamServiceImpl implements InitClassFixedParamService{

	@Resource
	private ClassFixedParamMapper classFixedParamMapper;
	
	/**
	 * 初始化固定值配置信息
	 * @author WangYao
	 * @date 2019年5月22日
	 */
	@Async
	@Override
	public void initClassFixedParam(int classId) {
		log.info("初始化固定值配置信息 classId={}",classId);
		int count = classFixedParamMapper.initClassFixedParam(classId);
		log.info("初始化固定值配置信息 count={}",count);
	}

}
