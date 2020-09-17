package com.tzcpa.service.student.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tzcpa.mapper.student.RelativePlotMapper;
import com.tzcpa.model.treatment.StuScore;
import com.tzcpa.service.student.RelativePlotService;

import lombok.extern.slf4j.Slf4j;

/**
 * 雷达图
 * @author WangYao
 * 2019年5月21日
 */
@Service
@Slf4j
public class RelativePlotServiceImpl implements RelativePlotService{

	@Resource
	private RelativePlotMapper relativePlotMapper;
	
	/**
	 * 根据年限查看所有团队的总分数以及列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public HashMap<String, Object> getRelativePlotList(int classId, int teamId) {
		log.info("根据年限查看所有团队的总分数以及列表信息 classId={},teamId={}",classId,teamId);
		HashMap<String, Object> map = new HashMap<>();
		StuScore stuScore = new StuScore();
		stuScore.setClassId(classId);
		stuScore.setTeamId(teamId);
		//1.查看列表
		List<StuScore> roleList = relativePlotMapper.selectList(stuScore);
		//2.查询总分
		Map<String, Object> totalScore = relativePlotMapper.selectTotalScore(stuScore);
		map.put("roleList", roleList);
		map.put("totalScore", totalScore);
		log.info("根据年限查看所有团队的总分数以及列表信息 map={}",map);
		return map;
	}

	/**
	 * 查看雷达图和团队信息
	 * @author WangYao
	 * @date 2019年6月14日
	 * @param classId
	 * @param id
	 * @return
	 */
	@Override
	public HashMap<String, Object> getTeamInfo(Integer classId, Integer teamId) {
		log.info("查看雷达图和团队信息 classId={},teamId={}",classId,teamId);
		HashMap<String, Object> map = new HashMap<>();
		StuScore stuScore = new StuScore();
		stuScore.setClassId(classId);
		stuScore.setTeamId(teamId);
		//1.查看列表
		List<StuScore> roleList = relativePlotMapper.selectList(stuScore);
		//2.查询总分
		Map<String, Object> totalScore = relativePlotMapper.selectTotalScore(stuScore);
		//3.查询团队信息
		Map<String, Object> teamInfo = relativePlotMapper.getTeamInfo(stuScore);
		map.put("teamInfo", teamInfo);
		map.put("roleList", roleList);
		map.put("totalScore", totalScore);
		log.info("查看雷达图和团队信息 map={}",map);
		return map;
	}

}
