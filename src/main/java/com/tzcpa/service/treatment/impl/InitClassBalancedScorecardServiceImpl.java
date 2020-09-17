package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.ClassBalancedScorecardMapper;
import com.tzcpa.service.treatment.InitClassBalancedScorecardService;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化平衡积分卡
 * @author WangYao
 * 2019年5月20日
 */
@Service("initClassBalancedScorecardService")
@Slf4j
public class InitClassBalancedScorecardServiceImpl implements InitClassBalancedScorecardService{

	@Resource
	private ClassBalancedScorecardMapper classBalancedScorecardMapper;

	/**
	 * 初始化班级平衡记分卡
	 * @author WangYao
	 * @date 2019年5月20日
	 * @param classId
	 */
	@Async
	@Override
	public void initClassBalancedScorecard(int classId) {
		log.info("初始化班级平衡记分卡 classId={}",classId);
        int count = classBalancedScorecardMapper.initClassBalancedScorecard(classId);
        log.info("初始化班级平衡记分卡 count={}",count);
	}
}
