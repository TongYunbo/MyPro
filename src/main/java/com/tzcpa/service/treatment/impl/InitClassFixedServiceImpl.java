package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tzcpa.service.treatment.InitClassFixedChargeAmortizationRatioService;
import com.tzcpa.service.treatment.InitClassFixedChargeAmortizationService;
import com.tzcpa.service.treatment.InitClassFixedChargeService;
import com.tzcpa.service.treatment.InitClassFixedService;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化固定费用 表信息
 * @author WangYao
 * 2019年5月22日
 */
@Service
@Slf4j
public class InitClassFixedServiceImpl implements InitClassFixedService{

	@Resource
    private InitClassFixedChargeService initClassFixedChargeService;
    @Resource
    private InitClassFixedChargeAmortizationRatioService initClassFixedChargeAmortizationRatioService;
    @Resource
    private InitClassFixedChargeAmortizationService initClassFixedChargeAmortizationService;

    /**
	 * 初始化固定费用 表信息
	 * @author WangYao
	 * @date 2019年5月22日
	 * @param classId
	 */
    @Async
	@Override
	public void initClassFixed(int classId) {
		log.info("初始化固定费用 表信息 classId={}",classId);
		//初始化固定费用
	    initClassFixedChargeService.initClassFixedCharge(classId);
	    //初始化固定费用分摊比例
	    initClassFixedChargeAmortizationRatioService.initClassFixedChargeAmortizationRatio(classId);
	    //初始化固定费用乘以分摊比例表信息
	    initClassFixedChargeAmortizationService.initClassFixedChargeAmortization(classId);
	}
}
