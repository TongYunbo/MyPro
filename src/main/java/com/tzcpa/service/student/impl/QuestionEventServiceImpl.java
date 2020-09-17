package com.tzcpa.service.student.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.QuestionEventMapper;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;

/**
 * @author WangYao
 * 2019年5月10日
 */
@Service("qeService")
public class QuestionEventServiceImpl extends AHseService{

	@Resource
	private QuestionEventMapper questionEventMapper;

	/**
	 * 全线产品矩阵
	 */
	public void productMatrix(List<HseRequest> hseRequest) {
		/*List<ClassQuestionOption> list = new ArrayList<>();
		StuScore stuScore = new StuScore();
		//初始化分数
		int score = 0;
		for (int i = 0; i < hseRequest.size(); i++) {
			HseRequest hr = hseRequest.get(i);
			for (int j = 0; j < ; j++) {
				Integer questionId = classQuestionOption.getQuestionId();
				String questionOption = classQuestionOption.getQuestionOption();
				String[] split = questionOption.split(",");
				for (int k = 0; k < split.length; k++) {
					if(questionId==16 && questionOption=="B"){
						score+=10;
					}
					if(questionId==17 && questionOption=="C"){
						score+=10;
					}
					if(questionId==18 && questionOption=="A"){
						score+=10;
					}
					if(questionId==18 && questionOption=="D"){
						score+=10;
					}
				}
			}
			for (int j = 0; j < lists.size(); j++) {
                //添加t_stu_score
				stuScore.setScore(score);
				stuScore.setClassId(classId);
				stuScore.setQuestionId(id);
				stuScore.setRoleId(1);
				stuScore.setTeamId(teamId);
				//questionEventMapper.insertStuScore(stuScore);
				//添加t_stu_answer
				
			}
			
		}*/
	}

	@Override
	public ResponseResult checkOS(List<HseRequest> hrList) {
		return null;
	}
}
