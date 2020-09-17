package com.tzcpa.controller.teacher;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.teacher.Mark;
import com.tzcpa.model.teacher.MarkScore;
import com.tzcpa.service.teacher.MarkService;
/**
 * 教师阅卷
 *
 * @author LRS
 * <p>
 * 2019年5月8日
 */
@RestController
@RequestMapping("teacher/mark")
public class MarkController {

    @Autowired
    private MarkService markService;

	/**
     * 查询题干信息、背景资料、学生答案信息
     *
     * @param mark 题目信息 questionId题目Id
     * @return
     */
    @RequestMapping("/listInternalControl")
    public ResponseResult<Map<String, Object>> getInternalControlAnswerList(@RequestBody Mark mark) {
    	HashMap<String, Object> map = new HashMap<>();
    	//查询主干题目信息
    	List<Mark> question = markService.findQuestion(mark);
    	//查询子题目
    	List<Mark> questionT = markService.findQuestionT(mark);
    	//查询答案
    	if(NormalConstant.InterSJ_MAPPING_QUESTION_ID.equals(mark.getQuestionId())){
    		//查询添加过评分
    		List<Mark> scoreList = markService.findScoreSJList(mark);
        	map.put("scoreList", scoreList);
    		//查询标准分
    		List<Mark> scoreStandList = markService.findScoreStandControList(mark);
    		map.put("scoreStandList", scoreStandList);
    		//查询答案 
    		List<Mark> answerL = markService.findAnswerListThree(mark);
        	 map.put("answerL", answerL);
    	}else if(NormalConstant.InterControl_MAPPING_QUESTION_ID.equals(mark.getQuestionId())){
    		//查询添加过的分
    		List<Mark> scoreList = markService.findScoreControlList(mark);
        	map.put("scoreList", scoreList);
    		//查询标准分
    		List<Mark> scoreStandList = markService.findScoreStandControList(mark);
    		map.put("scoreStandList", scoreStandList);
    		//查询答案
    		List<Mark> answerL = markService.findAnswerListThreeControl(mark);
       	    map.put("answerL", answerL);
    	}else if(NormalConstant.Allstrategy_MAPPING_QUESTION_ID.equals(mark.getQuestionId())){
    		//查询答案
    		List<Mark> answerL = markService.findAnswerList(mark);
    		map.put("answerL", answerL);
    		//标准分
    		List<Mark> scoreStandList = markService.findScoreStandList(mark);
    		map.put("scoreStandList", scoreStandList);
    		//查询添加过评分
        	List<Mark> scoreList = markService.findScoreList(mark);
        	map.put("scoreList", scoreList);
    	}else if(NormalConstant.YJPJ_MAPPING_QUESTION_ID.equals(mark.getQuestionId())){
    	   //添加标准分
    		List<Mark> scoreStandList = markService.findScoreStandList(mark);
    		map.put("scoreStandList", scoreStandList);
    		List<Mark> answerL = markService.findAnswerList(mark);
    		map.put("answerL", answerL);
    		//查询添加过评分
        	List<Mark> scoreList = markService.findScoreList(mark);
        	map.put("scoreList", scoreList);
    	}
        map.put("questionList", question);
        map.put("questionT", questionT);
        return new ResponseResult<Map<String, Object>>(map);
    }
    
    /**
     * 添加评分
     *
     * @param markScore 教师评分
     * @return 
     */
	@RequestMapping("/saveInternalControlScore")
    public ResponseResult<?> saveInternalControlScoreInfo(@RequestBody List<MarkScore> mkList) {
		for (MarkScore markScore : mkList) {
			//关键参数非空校验
			if(markScore.getClassId()!=null&&markScore.getTeamId()!=null
				&&markScore.getRoleId()!=null&&	markScore.getQuestionId()!=null
				&&markScore.getScore()!=null
				&&markScore.getWeight()!=null){
				markService.addBatchMarkInfo(markScore);
			}else{
				return  ResponseResult.fail("关键参数项为空");
			}	
		}
		return ResponseResult.success("评分成功");
	}
	
	
}
