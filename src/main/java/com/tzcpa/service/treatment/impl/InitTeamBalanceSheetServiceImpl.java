package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.TeamBalanceSheetMapper;
import com.tzcpa.service.treatment.InitClassBalanceSheetService;
import com.tzcpa.service.treatment.InitTeamBalanceSheetService;

import lombok.extern.slf4j.Slf4j;

/**
 * 初始化团队资产负债表
 * @author WangYao
 * 2019年5月20日
 */
@Service("initTeamBalanceSheetService")
@Slf4j
public class InitTeamBalanceSheetServiceImpl implements InitTeamBalanceSheetService{

	@Resource
	private TeamBalanceSheetMapper teamBalanceSheetMapper;

	/**
	 * 初始化团队资产负债表
	 * @author WangYao
	 * @date 2019年5月20日
	 * @param classId 班级id
	 * @param teamId 团队id
	 */
	@Async
	@Override
	public void initTeamBalanceSheetService(int classId, int teamId) {
		log.info("初始化团队资产负债表 classId={},teamId={}",classId,teamId);
		int count = teamBalanceSheetMapper.initClassBalanceSheet(classId, teamId);
		log.info("初始化团队资产负债表 count={}",count);
	}

}
