package com.tzcpa.service.student;

import java.util.List;
import java.util.Map;

import com.tzcpa.model.student.BalancedScorecard;

/**
 * 平衡记分卡题目
 * @author WangYao
 * 2019年5月17日
 */
public interface BalancedScorecardService {

	/**
	 * 查看平衡记分卡题目列表
	 * @author WangYao
	 * @date 2019年5月17日
	 * @param classId 班级id
	 * @param questionId 问题id
	 * @param teamId 团队id
	 * @return
	 */
	List<BalancedScorecard> getBalancedScList(int classId, int questionId, int teamId);

	/**
	 * 查询每年年底平衡记分卡列表
	 * @author WangYao
	 * @date 2019年5月30日
	 * @param classId
	 * @param teamId
	 * @param year
	 * @return
	 */
	List<Map<String, Object>> getTeamList(int classId, int teamId, int year);

	/**
	 * 获取年份
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	List<Map<String, Object>> getBalancedScorecardYear(Integer classId, Integer teamId);

}