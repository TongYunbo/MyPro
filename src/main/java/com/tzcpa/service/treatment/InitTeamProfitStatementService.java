package com.tzcpa.service.treatment;

import org.apache.ibatis.annotations.Param;

/**
 * 初始化团队利润表
 * @author WangYao
 * 2019年5月20日
 */

public interface InitTeamProfitStatementService {

	/**
	 * 初始化团队利润表
	 * @author WangYao
	 * @date 2019年5月20日
	 * @param classId
	 * @param teamId
	 */
	void initTeamProfitStatement(@Param(value="classId")int classId, @Param(value="teamId")int teamId);
}
