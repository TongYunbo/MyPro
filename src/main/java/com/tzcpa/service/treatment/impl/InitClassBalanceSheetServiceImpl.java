package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.ClassBalanceSheetMapper;
import com.tzcpa.service.treatment.InitClassBalanceSheetService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WangYao
 * 2019年5月20日
 */
@Service("initClassBalanceSheetService")
@Slf4j
public class InitClassBalanceSheetServiceImpl implements InitClassBalanceSheetService{
	
	@Resource
	private ClassBalanceSheetMapper classBalanceSheetMapper;
	
	/**
	 * 初始化班级资产负债表
	 * @author WangYao
	 * @date 2019年5月20日
	 * @param classId 班级id
	 */
	@Async
	@Override
	public void initClassBalanceSheet(int classId) {
		log.info("初始化班级资产负债表 classId={}", classId);
		int count = classBalanceSheetMapper.initClassBalanceSheet(classId);
		log.info("初始化班级资产负债表 count={}", count);
	}

}
