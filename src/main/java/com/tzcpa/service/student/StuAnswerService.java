package com.tzcpa.service.student;

import java.util.List;

/**
 * @ClassName StuAnswerService
 * @Description
 * @Author hanxf
 * @Date 2019/5/28 14:55
 * @Version 1.0
 **/
public interface StuAnswerService {

    /**
     * 获取已经答题的年份列表
     *
     * @Author hanxf
     * @Date 14:59 2019/5/28
     * @param classId 班级id
     * @param teamId 团队id
     * @return java.util.List<java.lang.Integer>
    **/
    List<Integer> getAnswerYear(int classId, int teamId);

	/**
     * 查询年度分析表的年份
	 * @author WangYao
	 * @date 2019年6月12日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	List<Integer> getTeamAnnualWorthGatherYear(int classId, int teamId);

	/**
     * 查询资产负债表的年份
	 * @author WangYao
	 * @date 2019年6月12日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	List<Integer> getTeamBalanceSheetYear(int classId, int teamId);

	/**
     * 查询年度利润表的年份
	 * @author WangYao
	 * @date 2019年6月12日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	List<Integer> getTeamProfitStatementYear(int classId, int teamId);

	/**
     * 查询月度利润表的年份
	 * @author WangYao
	 * @date 2019年6月12日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	List<Integer> getTeamMonthlyProfitStatementYear(int classId, int teamId);
}
