package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.ClassBalanceSheetDeployMapper;
import com.tzcpa.service.treatment.InitClassBalanceSheetDeployService;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化班级资产负债表配置信息
 * @author WangYao
 * 2019年5月20日
 */
@Service("initClassBalanceSheetDeployService")
@Slf4j
public class InitClassBalanceSheetDeployServiceImpl implements InitClassBalanceSheetDeployService{

	@Resource
	private ClassBalanceSheetDeployMapper classBalanceSheetDeployMapper;
	
	/**
	 * 初始化班级资产负债表配置信息
	 * @author WangYao
	 * @date 2019年5月20日
	 * @param classId
	 */
	@Async
	@Override
	public void initClassBalanceSheetDeploy(int classId) {
		log.info("初始化班级资产负债表配置信息 classId={}", classId);
		int count = classBalanceSheetDeployMapper.initClassBalanceSheetDeploy(classId);
		log.info("初始化班级资产负债表配置信息 count={}", count);
	}

}
