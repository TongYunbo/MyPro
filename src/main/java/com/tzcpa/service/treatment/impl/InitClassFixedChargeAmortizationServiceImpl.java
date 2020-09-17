package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.ClassFixedChargeAmortizationMapper;
import com.tzcpa.service.treatment.InitClassFixedChargeAmortizationService;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化固定费用中间表
 * @author WangYao
 * 2019年5月22日
 */
@Service("initClassFixedChargeAmortizationService")
@Slf4j
public class InitClassFixedChargeAmortizationServiceImpl implements InitClassFixedChargeAmortizationService{

	@Resource
	private ClassFixedChargeAmortizationMapper classFixedChargeAmortizationMapper;

	/**
	 * 初始化固定费用中间表
	 * @author WangYao
	 * @date 2019年5月22日
	 * @param classId
	 */
	@Override
	public void initClassFixedChargeAmortization(int classId) {
		log.info("初始化固定费用中间表 classId={}",classId);
		int count = classFixedChargeAmortizationMapper.initClassFixedChargeAmortization(classId);
		log.info("初始化固定费用中间表 count={}",count);
	}
}
