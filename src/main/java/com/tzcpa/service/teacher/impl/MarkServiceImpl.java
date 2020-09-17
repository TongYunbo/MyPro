package com.tzcpa.service.teacher.impl;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.mapper.teacher.MarkMapper;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.teacher.Mark;
import com.tzcpa.model.teacher.MarkScore;
import com.tzcpa.model.treatment.QuestionAnswerdDTO;
import com.tzcpa.service.teacher.MarkService;

/**
 * @author LRS
 * <p>
 * 2019年4月28日
 */
@Service
public class MarkServiceImpl implements MarkService {

    @Resource
    private MarkMapper markMapper;
    @Resource
	private OSMapper osMapper;
    /**
     * 查询题干信息、答案
     *
     * @param mark
     * @return 题目、答案
     */
	@Override
	public List<Mark> findAnswer(Mark mark) {
		return markMapper.findAnswer(mark);
	}
	/**
     * 
     *添加分数
     * @param markScore
     */
	@Override
	public boolean addBatchMarkInfo(MarkScore markScore) {
		List<MarkScore> scoreList =new ArrayList<>();
		//获取学生角色Id
		int roleId=markScore.getRoleId();
		if(roleId !=NormalConstant.ALL_ROLE_ID){
			markScore.setScore(Double.valueOf((markScore.getScore()*markScore.getWeight())));
			markScore.setYear(Integer.valueOf(markScore.getTimeLine().substring(0, 4)));
			scoreList.add(markScore);
		}else{
		 	String[] split = NormalConstant.ALL_ROLE_IDS.split(",");
		 	for (int i = 0; i < split.length; i++) {
		 		MarkScore  markScore2  = new MarkScore ();
				int roleIds=Integer.valueOf(split[i]);
				markScore2.setRoleId(roleIds);
				markScore2.setClassId(markScore.getClassId());
				markScore2.setQuestionId(markScore.getQuestionId());
				markScore2.setScore(Double.valueOf((markScore.getScore()*markScore.getWeight())));
				markScore2.setRootId(markScore.getRootId());
				markScore2.setYear(Integer.valueOf(markScore.getTimeLine().substring(0, 4)));
				markScore2.setTeamId(markScore.getTeamId());
				scoreList.add(markScore2);
			}
		 	
		}
		return markMapper.addBatchMarkInfo(scoreList);
	}
	@Override
	public List<Mark> findScoreList(Mark mark) {
		return markMapper.findScoreList(mark);
	}
	@Override
	public List<Mark> findQuestion(Mark mark) {
		return markMapper.findQuestion(mark);
	}
	@Override
	public List<Mark> findQuestionT(Mark mark) {
		return markMapper.findQuestionT(mark);
	}
	@Override
	public List<Mark> findAnswerList(Mark mark) {
		return markMapper.findAnswerList(mark);
	}
	@Override
	public List<Mark> findScoreStandList(Mark mark) {
		return markMapper.findScoreStandList(mark);
	}
	@Override
	public List<Mark> findScoreStandControList(Mark mark) {
		return markMapper.findScoreStandControList(mark);
	}
	@Override
	public List<Mark> findAnswerListThree(Mark mark) {
		return markMapper.findAnswerListThree(mark);
	}
	@Override
	public List<Mark> findAnswerListThreeControl(Mark mark) {
		return markMapper.findAnswerListThreeControl(mark);
	}
	@Override
	public List<Mark> findScoreSJList(Mark mark) {
		return markMapper.findScoreSJList(mark);
	}
	@Override
	public List<Mark> findScoreControlList(Mark mark) {
		return markMapper.findScoreControlList(mark);
	}
	@Override
	public List<Mark> findAnswerListAll(QuestionAnswerdDTO questionAnswerdDTO) {
		return markMapper.findAnswerListAll(questionAnswerdDTO);
	}
	@Override
	public List<Mark> findAnswerStand(QuestionAnswerdDTO questionAnswerdDTO) {
		return markMapper.findAnswerStand(questionAnswerdDTO);
	}

}
