package com.tzcpa.mapper.treatment;

import org.apache.ibatis.annotations.Param;

public interface TeamBalancedScorecardMapper {

	/**
	 * 初始化团队平衡记分卡
	 * @author WangYao
	 * @date 2019年5月20日
	 * @param classId
	 * @param teamId 
	 * @return
	 */
	int initTeamBalancedScorecard(@Param(value="classId")int classId, @Param(value="teamId")int teamId);
}