package com.tzcpa.service.teacher.impl;

import com.alibaba.fastjson.JSON;
import com.tzcpa.mapper.student.ReportCardMapper;
import com.tzcpa.model.teacher.RoleQuestionScore;
import com.tzcpa.service.teacher.ReportCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学生成绩单列表
 * @author WangYao
 * 2019年5月27日
 */
@Service
@Slf4j
public class ReportCardServiceImpl implements ReportCardService{

	@Resource
	private ReportCardMapper reportCardMapper;

	/**
	 * 查看学生成绩单列表
	 * @author WangYao
	 * @date 2019年5月27日
	 * @param classId
	 * @return
	 */
	@Override
	public Map<String, Object> getReportCardList(int classId) {
		log.info("查看学生成绩单列表 classId={}", classId);

		//返回结果
		Map<String, Object> resultMap = new HashMap<>();


		List<Map<String, Object>> title = reportCardMapper.getTitle(classId);
		log.info("查询学生成绩单 标题 title={}", JSON.toJSONString(title));
		resultMap.put("title", title);

		List<Map<String,Object>> scoreList = new ArrayList<Map<String,Object>>();
		List<RoleQuestionScore> roleScoreList = reportCardMapper.getRoleScoreList(classId);

		Map<String, List<RoleQuestionScore>> roleScoreListMap =
				roleScoreList
						.stream()
						.collect(Collectors.groupingBy(RoleQuestionScore::getRoleName,LinkedHashMap::new,Collectors.toList()));
		log.info("对角色分数进行分组 roleScoreListMap={}",JSON.toJSONString(roleScoreListMap));

		this.packagData(scoreList, roleScoreListMap);
		//查看每个团队的总分
		List<RoleQuestionScore> totalScoreList = reportCardMapper.getTotalScore(classId);
		
		Map<String, List<RoleQuestionScore>> totalScoreListMap =
				totalScoreList
						.stream()
						.collect(Collectors.groupingBy(RoleQuestionScore::getRoleName,LinkedHashMap::new,Collectors.toList()));
		log.info("对角色分数进行分组 totalScoreListMap={}",JSON.toJSONString(totalScoreListMap));

		this.packagData(scoreList, totalScoreListMap);

		log.info("查询学生成绩单 角色列表 scoreList={}",JSON.toJSONString(scoreList));
		resultMap.put("scoreList",scoreList);


		List<Map<String,Object>> questionList = new ArrayList<Map<String,Object>>();
		List<RoleQuestionScore> questionScoreList = reportCardMapper.getQuestionScoreList(classId);

		Map<String, List<RoleQuestionScore>> questionScoreListMap =
				questionScoreList
						.stream()
						.collect(Collectors.groupingBy(RoleQuestionScore::getQuestionName,LinkedHashMap::new,Collectors.toList()));
		log.info("对题目分数进行分组 questionScoreListMap={}",JSON.toJSONString(questionScoreListMap));


		questionScoreListMap.forEach((K,V)->{
			Map<String,Object> questionScoreMap = new HashMap<String,Object>();
			questionScoreMap.put("questionName",K);
			V.stream().forEach(roleQuestionScore ->{
				questionScoreMap.put(roleQuestionScore.getTeamId().toString(), roleQuestionScore.getScore());
			});
			questionList.add(questionScoreMap);
		});
		log.info("查询学生成绩单 题目列表 questionList={}",JSON.toJSONString(questionList));
		resultMap.put("questionList",questionList);


		return resultMap;
	}

	private void packagData(List<Map<String, Object>> scoreList, Map<String, List<RoleQuestionScore>> roleScoreListMap) {
		roleScoreListMap.forEach((K,V)->{
			Map<String,Object> roleScoreMap = new HashMap<String,Object>();
			roleScoreMap.put("roleName",K);
			V.stream().forEach(roleQuestionScore ->{
				roleScoreMap.put(roleQuestionScore.getTeamId().toString(), roleQuestionScore.getScore());
			});
			scoreList.add(roleScoreMap);
		});
	}
}
