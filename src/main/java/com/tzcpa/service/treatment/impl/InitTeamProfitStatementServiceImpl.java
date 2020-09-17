package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.TeamProfitStatementMapper;
import com.tzcpa.service.treatment.InitTeamProfitStatementService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WangYao
 * 2019年5月20日
 */
@Service
@Slf4j
public class InitTeamProfitStatementServiceImpl implements InitTeamProfitStatementService{
	
	@Resource
	private TeamProfitStatementMapper teamProfitStatementMapper;

	/**
	 * 初始化团队利润表
	 * @author WangYao
	 * @date 2019年5月20日
	 * @param classId
	 * @param teamId
	 */
	@Async
	@Override
	public void initTeamProfitStatement(int classId, int teamId) {
		log.info("初始化团队利润表 classId={},teamId={}",classId,teamId);
		int count = teamProfitStatementMapper.initTeamProfitStatement(classId, teamId);
		log.info("初始化团队利润表 count={}",count);
	}
	
}
