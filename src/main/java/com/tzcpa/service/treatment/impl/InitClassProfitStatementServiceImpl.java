package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.ClassProfitStatementMapper;
import com.tzcpa.service.treatment.InitClassProfitStatementService;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化班级利润表
 * @author WangYao
 * 2019年5月20日
 */
@Service("initClassProfitStatementService")
@Slf4j
public class InitClassProfitStatementServiceImpl implements InitClassProfitStatementService{
	
	@Resource
	private ClassProfitStatementMapper classProfitStatementMapper;

	/**
	 * 初始化班级利润表
	 * @author WangYao
	 * @date 2019年5月20日
	 * @param classId
	 */
	@Async
	@Override
	public void initClassProfitStatement(int classId) {
		log.info("初始化班级利润表 classId={}",classId);
		int count = classProfitStatementMapper.initClassProfitStatement(classId);
		log.info("初始化班级利润表 count={}",count);
	}
	
}
