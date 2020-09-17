package com.tzcpa.service.teacher;
import java.util.List;
import com.tzcpa.model.teacher.Mark;
import com.tzcpa.model.teacher.MarkScore;
import com.tzcpa.model.treatment.QuestionAnswerdDTO;


/**
 * 查询题干信息、学生答案
 *
 * @author LRS
 * <p>
 * 2019年4月28日
 */
public interface MarkService {

    /**
     * 查询题干信息、学生答案
     *
     * @param 
     * @return
     */
	List<Mark> findAnswer(Mark mark);
	/**
     * 给学生评分
     *
     * @param markScore
     * @return
     */
	boolean addBatchMarkInfo(MarkScore markScore );
	/**
     * 查询学生分数
     *
     * @param Mark mark
     * @return
     */
	List<Mark> findScoreList(Mark mark);
	/**
     * 查询问题
     *
     * @param Mark mark
     * @return
     */
	List<Mark> findQuestion(Mark mark);
	/**
     * 查询子问题
     *
     * @param Mark mark
     * @return
     */
	List<Mark> findQuestionT(Mark mark);
	/**
     * 查询答案
     *
     * @param Mark mark
     * @return
     */
	List<Mark> findAnswerList(Mark mark);
	/**
     * 查询标准答案
     *
     * @param Mark mark
     * @return
     */
	List<Mark> findScoreStandList(Mark mark);
	/**
     * 查询标准分
     *
     * @param Mark mark
     * @return
     */
	List<Mark> findScoreStandControList(Mark mark);
	/**
     * 查询答案
     *
     * @param Mark mark
     * @return
     */
	List<Mark> findAnswerListThree(Mark mark);
	/**
     * 查询内部控制答案
     *
     * @param Mark mark
     * @return
     */
	List<Mark> findAnswerListThreeControl(Mark mark);
	/**
     * 查询内部审计得过的分
     *
     * @param Mark mark
     * @return
     */
	List<Mark> findScoreSJList(Mark mark);
	/**
     * 查询内部控制得过的分
     *
     * @param Mark mark
     * @return
     */
	List<Mark> findScoreControlList(Mark mark);
	/**
     * 查询总体战略标准分
     *
     * @param Mark mark
     * @return
     */
	List<Mark> findAnswerListAll(QuestionAnswerdDTO questionAnswerdDTO);
	/**
     * 查询内部审计标准分
     *
     * @param Mark mark
     * @return
     */
	List<Mark> findAnswerStand(QuestionAnswerdDTO questionAnswerdDTO);

}
