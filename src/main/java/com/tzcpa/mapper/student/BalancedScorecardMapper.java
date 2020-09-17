package com.tzcpa.mapper.student;

import java.util.List;
import java.util.Map;

import com.tzcpa.model.student.BalancedScorecard;

/**
 * 平衡记分卡题目
 *
 * @author WangYao
 * 2019年5月17日
 */
public interface BalancedScorecardMapper {

    /**
     * 查看团队的平衡记分卡题目列表
     *
     * @param balancedScorecard
     * @return
     * @author WangYao
     * @date 2019年5月17日
     */
    List<BalancedScorecard> getBalancedScList(BalancedScorecard balancedScorecard);

    /**
     * 查询每年年底平衡记分卡列表
     *
     * @param balancedScorecard
     * @return
     * @author WangYao
     * @date 2019年5月30日
     */
    List<Map<String, Object>> getTeamList(BalancedScorecard balancedScorecard);

    /**
     * 查询团队平衡积分卡 为计算平衡记分卡实际值
     *
     * @param param
     * @return
     */
    List<BalancedScorecard> selectTeamBalancedScorecard(BalancedScorecard param);

    /**
     * 更新平衡计分卡 实际值和最终得分
     * @param param
     * @return
     */
    int updateTeamBalancedScorecard(BalancedScorecard param);

	/**
	 * 获取年份
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param balancedScorecard
	 * @return
	 */
	List<Map<String, Object>> getBalancedScorecardYear(BalancedScorecard balancedScorecard);
}