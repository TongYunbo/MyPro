package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.ClassSyntheticAbilityMapper;
import com.tzcpa.service.treatment.InitClassSyntheticAbilityService;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化班级综合排行表
 * @author WangYao
 * 2019年6月10日
 */
@Service
@Slf4j
public class InitClassSyntheticAbilityServiceImpl implements InitClassSyntheticAbilityService{

	@Resource
	private ClassSyntheticAbilityMapper classSyntheticAbilityMapper;

	/**
	 * 初始化班级综合排行表
	 * @author WangYao
	 * @date 2019年6月10日
	 * @param classId
	 */
	@Override
	public void initClassSyntheticAbility(int classId) {
		log.info("初始化班级综合排行表  classId={}", classId);
		classSyntheticAbilityMapper.initClassSyntheticAbility(classId);
	}
}
