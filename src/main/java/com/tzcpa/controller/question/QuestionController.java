package com.tzcpa.controller.question;

import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.teacher.Mark;
import com.tzcpa.model.treatment.*;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.teacher.MarkService;
import com.tzcpa.utils.UserSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 题目管理
 *
 * @author wangbj
 * <p>
 * 2019年5月5日
 */
@Slf4j
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Resource
    QuestionService questionService;
    @Autowired
    MarkService markService;

    /**
     * @author:     wangzhangju
     * @date:       2019/5/9 11:39
     * @param:      null
     * @return:
     * @exception:
     * @description: 初始化题目--答到哪个题展示哪一个; 处理答题。
     * @step:
     */
    @RequestMapping(value = "getQuestion", method = RequestMethod.POST)
    public ResponseResult getQuestion(@RequestBody QuestionAnswerDTO questionAnswerDTO) {
        log.info("QuestionController.getQuestion 进入 param = {}" + questionAnswerDTO.toString());

        //NO.1 处理答题 执行答题操作
        //NO.2 答题结束后 展示下一题
        Object questionAnswerDTO1 = null;
        try {
            questionAnswerDTO1 = questionService.answerAndGetQuestion(questionAnswerDTO);
        } catch (Exception e) {
            log.error("操作失败",e);
            return ResponseResult.fail("操作失败!");
        }

        log.info("QuestionController.getQuestion 结束");
        return ResponseResult.success("操作成功!",questionAnswerDTO1);
    }

    /**
     * @author:     wangzhangju
     * @date:       2019/5/14 9:15
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取已经答题的问题
     * @step:
     */
    @RequestMapping(value = "getAnswerdQuestion", method = RequestMethod.POST)
    public ResponseResult getAnswerdQuestion(@RequestBody QuestionAnswerdDTO questionAnswerdDTO) {
        log.info("QuestionController.getQuestion 进入 param = {}" + questionAnswerdDTO.toString());
       QuestionAnswerDTO questionAnswerDTO = null;
        try {
            questionAnswerDTO = questionService.getAnswerdQuestion(questionAnswerdDTO);
        } catch (Exception e) {
            log.error("获取已答题问题失败",e);
            return ResponseResult.fail("获取已答题问题失败!");
        }
        if(NormalConstant.Allstrategy_MAPPING_QUESTION_ID.equals(questionAnswerdDTO.getThisItemId())|| NormalConstant.YJPJ_MAPPING_QUESTION_ID.equals(questionAnswerdDTO.getThisItemId())){
        	//为传参赋值
        	Mark mark = new Mark();
        	mark.setQuestionId(questionAnswerdDTO.getThisItemId());
        	mark.setClassId(questionAnswerdDTO.getClassId());
        	mark.setTeamId(questionAnswerdDTO.getTeamId());
        	//查询标准分
        	List<Mark> scoreStandList= markService.findAnswerListAll(questionAnswerdDTO);
        	if(scoreStandList.size()!=0){
        		if(questionAnswerDTO!=null){
        			questionAnswerDTO.setScore(scoreStandList.get(0).getScore());
        		}
        	}
        	//查询已经得过的分
        	List<Mark> scoreList = markService.findScoreList(mark);
        	if(scoreList.size()!=0){
        		if(questionAnswerDTO!=null){
        			questionAnswerDTO.setScoreList(scoreList);
        		}
        	}
        }else if(NormalConstant.InterControl_MAPPING_QUESTION_ID.equals(questionAnswerdDTO.getThisItemId())){
        	Mark mark = new Mark();
        	mark.setQuestionId(questionAnswerdDTO.getThisItemId());
        	mark.setClassId(questionAnswerdDTO.getClassId());
        	mark.setTeamId(questionAnswerdDTO.getTeamId());
        	//查询标准分
        	List<Mark> scoreStandList = markService.findScoreStandControList(mark);
        	if(scoreStandList.size()!=0){
        		if(questionAnswerDTO!=null){
        			questionAnswerDTO.setScore(scoreStandList.get(0).getScore());
        		}
        	}
        	//查询得过的分
        	List<Mark> scoreList = markService.findScoreControlList(mark);
        	if(scoreList.size()!=0){
        		if(questionAnswerDTO!=null){
        			questionAnswerDTO.setScoreList(scoreList);
        		}
        	}
        }else if(NormalConstant.InterSJ_MAPPING_QUESTION_ID.equals(questionAnswerdDTO.getThisItemId())){
        	Mark mark = new Mark();
        	mark.setQuestionId(questionAnswerdDTO.getThisItemId());
        	mark.setClassId(questionAnswerdDTO.getClassId());
        	mark.setTeamId(questionAnswerdDTO.getTeamId());
        	//查询标准分
        	List<Mark> scoreStandList= markService.findAnswerStand(questionAnswerdDTO);
//        	if(scoreStandList.size()!=0){
//        		if(questionAnswerDTO!=null){
//        			questionAnswerDTO.setScore(scoreStandList.get(0).getScore());
//        		}
//        	}
            if(questionAnswerDTO!=null) {
                List<SecondQuestionDTO> thisChildrenItem = questionAnswerDTO.getThisChildrenItem();
                thisChildrenItem.forEach(secondQuestionDTO->{
                    scoreStandList.forEach(mark1->{
                        ThirdQuestionDTO thirdQuestionDTO = secondQuestionDTO.getThisChildrenItem().get(0);
                        if (thirdQuestionDTO.getThisItemId() == mark1.getQuestionId()) {
                            thirdQuestionDTO.setScore(mark1.getScore());
                        }
                    });
                });
            }

            //查询添加过的分
        	List<Mark> scoreList = markService.findScoreSJList(mark);
        	if(scoreList.size()!=0){
        		if(questionAnswerDTO!=null){
        			questionAnswerDTO.setScoreList(scoreList);
        		}
        	}
        }
        return ResponseResult.success("操作成功!",questionAnswerDTO);
    }

    /**
     * @author:     wangzhangju
     * @date:       2019/5/13 17:26
     * @param:      null
     * @return:
     * @exception:
     * @description: 查询小组答题记录(沙盘地图）
     * @step:
     */
    @RequestMapping(value = "getSandTable", method = RequestMethod.POST)
    public ResponseResult getSandTable(@RequestBody  SandTableDTO sandTableDTO) {
        log.info("QuestionController.getSandTable 进入" );

        List<SandTableVO> resultList = questionService.getSandTable(sandTableDTO.getClassId(),sandTableDTO.getTeamId());
        return ResponseResult.success("操作成功!",resultList);
    }


    /**
     *  根据题目id查询选项
     *
     * @Author hanxf
     * @Date 11:21 2019/5/21
     * @param questionIds
     * @return com.tzcpa.controller.result.ResponseResult
    **/
    @PostMapping(value = "getQuestionOptions")
    public ResponseResult getQuestionOptions(@RequestBody List<Integer> questionIds){
        log.info("根据questionIds查询选项 questionIds={}", JSON.toJSONString(questionIds));
        try {
            List<ClassQuestionOptionDO> questionOptions = questionService.getQuestionOptions(questionIds);
            return ResponseResult.success(questionOptions);
        } catch (Exception e) {
            log.error("查询题目选项失败",e);
            return ResponseResult.fail("查询题目选项失败");
        }

    }

    /**
     * @author:     wangzhangju
     * @date:       2019/6/5 17:41
     * @param:      null
     * @return:
     * @exception:
     * @description:  保存H6风险识别选项答案
     * @step:
     */
    @PostMapping(value = "insertH6Answer")
    public ResponseResult insertH6Answer(@RequestBody QuestionAnswerDTO questionAnswerDTO){

        log.info("保存H6风险识别选项大题答案 param={}", JSON.toJSONString(questionAnswerDTO));
        try {
            UserInfo userInfo = UserSessionUtil.getUserInfo();
            int i = questionService.insertH6Answer(questionAnswerDTO,userInfo);
            return ResponseResult.success();
        } catch (Exception e) {
            log.error("保存H6风险识别选项选项失败",e);
            return ResponseResult.fail("保存H6风险识别选项失败");
        }
    }

}
