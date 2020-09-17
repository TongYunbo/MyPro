package com.tzcpa.mapper.teacher;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tzcpa.model.teacher.Mark;
import com.tzcpa.model.teacher.MarkScore;
import com.tzcpa.model.treatment.QuestionAnswerdDTO;
/**
 *
 * @author LRS
 * <p>
 * 2019年5月8日
 */
public interface MarkMapper {
    /**
     * 查询题干信息、背景资料、学生答案信息
     *
     * @return
     */
	List<Mark> findAnswer(Mark mark);
	/**
     * 给学生评分
     *
     * @return
     */
	boolean addBatchMarkInfo(@Param("scoreList")List<MarkScore> scoreList );
	/**
     * 查询学生分数
     *
     * @return
     */
	List<Mark> findScoreList(Mark mark);
	/**
     * 查询题干信息
     *
     * @return
     */
	List<Mark> findQuestion(Mark mark);
	/**
     * 查询题干子信息
     *
     * @return
     */
	List<Mark> findQuestionT(Mark mark);
	/**
     * 查询答案信息
     *
     * @return
     */
	List<Mark> findAnswerList(Mark mark);
	/**
     * 查询标准信息
     *
     * @return mark
     */
	List<Mark> findScoreStandList(Mark mark);
	/**
     * 查询标准分数信息
     *
     * @return mark
     */
	List<Mark> findScoreStandControList(Mark mark);
	/**
     * 查询内部审计答案
     *
     * @return mark
     */
	List<Mark> findAnswerListThree(Mark mark);
	/**
     * 查询内部控制答案
     *
     * @return mark
     */
	List<Mark> findAnswerListThreeControl(Mark mark);
	/**
     * 查询内部审计得分
     *
     * @return mark
     */
	List<Mark> findScoreSJList(Mark mark);
	/**
     * 查询内部控制得分
     *
     * @return mark
     */
	List<Mark> findScoreControlList(Mark mark);
	/**
     * 查询总体战略得分
     *
     * @return mark
     */
	List<Mark> findScoreStandControList(QuestionAnswerdDTO questionAnswerdDTO);
	/**
     * 查询内部审计标准得分
     *
     * @return mark
     */
	List<Mark> findAnswerStand(QuestionAnswerdDTO questionAnswerdDTO);
	/**
     * 查询内部控制得分
     *
     * @return mark
     */
	List<Mark> findAnswerListAll(QuestionAnswerdDTO questionAnswerdDTO);

}
