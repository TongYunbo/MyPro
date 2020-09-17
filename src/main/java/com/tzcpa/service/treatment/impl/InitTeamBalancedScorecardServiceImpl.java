package com.tzcpa.service.treatment.impl;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tzcpa.mapper.treatment.TeamBalancedScorecardMapper;
import com.tzcpa.service.treatment.InitTeamBalancedScorecardService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WangYao
 * 2019年5月20日
 */
@Service("initTeamBalancedScorecardService")
@Slf4j
public class InitTeamBalancedScorecardServiceImpl implements InitTeamBalancedScorecardService{

	@Resource
	private TeamBalancedScorecardMapper teamBalancedScorecardMapper;

	/**
	 * 初始化团队平衡记分卡
	 * @author WangYao
	 * @date 2019年5月20日
	 * @param classId
	 */
	@Async
	@Override
	public void initTeamBalancedScorecard(int classId, int teamId) {
		log.info("初始化班级平衡记分卡 classId={},teamId={}",classId,teamId);
        int count = teamBalancedScorecardMapper.initTeamBalancedScorecard(classId, teamId);
        log.info("初始化班级平衡记分卡 count={}",count);
	}
}
