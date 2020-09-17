package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.ClassFixedChargeAmortizationRatioMapper;
import com.tzcpa.service.treatment.InitClassFixedChargeAmortizationRatioService;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化固定费用分摊比例
 * @author WangYao
 * 2019年5月20日
 */
@Service("nitClassFixedChargeAmortizationRatioService")
@Slf4j
public class InitClassFixedChargeAmortizationRatioServiceImpl implements InitClassFixedChargeAmortizationRatioService{

	@Resource
	private ClassFixedChargeAmortizationRatioMapper classFixedChargeAmortizationRatioMapper;

	/**
	 * 初始化固定费用分摊比例
	 * @author WangYao
	 * @date 2019年5月20日
	 * @param classId 班级id
	 */
	@Override
	public void initClassFixedChargeAmortizationRatio(int classId) {
		log.info("初始化固定费用分摊比例 classId={}",classId);
		int count = classFixedChargeAmortizationRatioMapper.initClassFixedChargeAmortizationRatio(classId);
		log.info("初始化固定费用分摊比例 count={}",count);
	}
}
