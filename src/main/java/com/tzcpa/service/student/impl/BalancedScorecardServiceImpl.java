package com.tzcpa.service.student.impl;

import com.tzcpa.mapper.student.BalancedScorecardMapper;
import com.tzcpa.mapper.treatment.ClassQuestionDescMapper;
import com.tzcpa.model.student.BalancedScorecard;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.service.student.BalancedScorecardService;
import com.tzcpa.utils.DateUtil;
import com.tzcpa.utils.UserSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 平衡记分卡答题卡
 * @author WangYao
 * 2019年5月17日
 */
@Service
@Slf4j
public class BalancedScorecardServiceImpl implements BalancedScorecardService{
	
	@Resource
	private BalancedScorecardMapper balancedScorecardMapper;

	@Resource
	private ClassQuestionDescMapper classQuestionDescMapper;

	/**
	 * 查看平衡记分卡答题卡
	 * @author WangYao
	 * @date 2019年5月17日
	 * @param classId 班级id
	 * @param questionId 问题id
	 * @param teamId 团队id
	 * @return
	 */
	@Override
	public List<BalancedScorecard> getBalancedScList(int classId, int questionId, int teamId) {
		log.info("查看平衡记分卡答题卡  classId={},questionId={}, teamId={}", classId, questionId, teamId);
		BalancedScorecard balancedScorecard = new BalancedScorecard();
		balancedScorecard.setClassId(classId);
		balancedScorecard.setQuestionId(questionId);
		balancedScorecard.setTeamId(teamId);
		return balancedScorecardMapper.getBalancedScList(balancedScorecard);
	}

	/**
	 * 查询每年年底平衡记分卡列表
	 * @author WangYao
	 * @date 2019年5月30日
	 * @param classId
	 * @param teamId
	 * @param year
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getTeamList(int classId, int teamId, int year) {
		log.info("查询每年年底平衡记分卡列表  classId={},teamId={},year={}", classId, teamId, year);
		BalancedScorecard balancedScorecard = new BalancedScorecard();
		balancedScorecard.setClassId(classId);
		balancedScorecard.setTeamId(teamId);
		balancedScorecard.setYear(year);
		List<Map<String, Object>> map = balancedScorecardMapper.getTeamList(balancedScorecard);
		log.info("查询每年年底平衡记分卡列表  map={}", map);
		return map;
	}

	/**
	 * 获取年份
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getBalancedScorecardYear(Integer classId, Integer teamId) {
		log.info("获取年份  classId={},teamId={}", classId, teamId);
		if(null == classId) {
            Team team = UserSessionUtil.getTeam();
            classId = team.getClassId();
            teamId = team.getId();
        }
		Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
		BalancedScorecard balancedScorecard = new BalancedScorecard();
		balancedScorecard.setClassId(classId);
		balancedScorecard.setTeamId(teamId);
		balancedScorecard.setYear(Integer.valueOf(DateUtil.dateToStr("yyyy",currentMonth)));
		List<Map<String, Object>> map = balancedScorecardMapper.getBalancedScorecardYear(balancedScorecard);
		log.info("获取年份  map={}", map);
		return map;
	}

}