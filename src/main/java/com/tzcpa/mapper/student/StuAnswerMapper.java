package com.tzcpa.mapper.student;

import com.tzcpa.model.student.StuAnswerDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
/**
 * 学生答案mapper
 *
 * @Author hanxf
 * @Date 14:46 2019/5/28
**/
public interface StuAnswerMapper {

    /**
     *
     * @param record
     * @return
     */
    int insertStuAnswer(StuAnswerDO record);

    int insertStuAnswerBatch(List<StuAnswerDO>  records);


    /**
     * 获取已做答案的年份
     *
     * @Author hanxf
     * @Date 14:44 2019/5/28
     * @param classId 班级id
     * @param teamId 团队id
     * @return java.util.List<java.lang.Integer>
    **/
    List<Integer> getAnswerYear(@Param(value = "classId") int classId,@Param(value = "teamId") int teamId);

    Integer getAnswerMonth(@Param(value = "classId") int classId,@Param(value = "teamId") int teamId,@Param(value = "year") int year);

    StuAnswerDO getAnswer(@Param(value = "classId") int classId,@Param(value = "teamId") int teamId,@Param(value = "questionId") int questionId);

	/**
     * 查询年度分析表的年份
	 * @author WangYao
	 * @date 2019年6月12日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	List<Integer> getTeamAnnualWorthGatherYear(@Param(value = "classId") int classId,@Param(value = "teamId") int teamId);

	/**
     * 查询资产负债表的年份
	 * @author WangYao
	 * @date 2019年6月12日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	List<Integer> getTeamBalanceSheetYear(@Param(value = "classId") int classId,@Param(value = "teamId") int teamId);

	/**
     * 查询年度利润表的年份
	 * @author WangYao
	 * @date 2019年6月12日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	List<Integer> getTeamProfitStatementYear(@Param(value = "classId") int classId,@Param(value = "teamId") int teamId);

	/**
     * 查询月度利润表的年份
	 * @author WangYao
	 * @date 2019年6月12日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	List<Integer> getTeamMonthlyProfitStatementYear(@Param(value = "classId") int classId,@Param(value = "teamId") int teamId,@Param(value = "currentMonth")Date currentMonth);

	int getAnswerCount(@Param("classId") Integer classId);
}