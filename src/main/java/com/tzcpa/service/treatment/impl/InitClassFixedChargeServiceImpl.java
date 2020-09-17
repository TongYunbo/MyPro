package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.ClassFixedChargeMapper;
import com.tzcpa.service.treatment.InitClassFixedChargeService;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化固定费用
 * @author WangYao
 * 2019年5月20日
 */
@Service("initClassFixedChargeService")
@Slf4j
public class InitClassFixedChargeServiceImpl implements InitClassFixedChargeService{

	@Resource
	private ClassFixedChargeMapper classFixedChargeMapper;

	/**
	 * 初始化固定费用
	 * @author WangYao
	 * @date 2019年5月20日
	 * @param classId 班级id
	 */
	@Override
	public void initClassFixedCharge(int classId) {
		log.info("初始化固定费用 classId={}",classId);
		int count = classFixedChargeMapper.initClassFixedCharge(classId);
		log.info("初始化固定费用 count={}",count);
	}
}
