package com.tzcpa.service.student.impl;

import com.tzcpa.mapper.student.StuAnswerMapper;
import com.tzcpa.mapper.treatment.ClassQuestionDescMapper;
import com.tzcpa.service.student.StuAnswerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName StuAnswerServiceImpl
 * @Description
 * @Author hanxf
 * @Date 2019/5/28 14:56
 * @Version 1.0
 **/
@Service("stuAnswerService")
public class StuAnswerServiceImpl implements StuAnswerService {

    @Resource
    private StuAnswerMapper stuAnswerMapper;

	@Resource
	private ClassQuestionDescMapper classQuestionDescMapper;

    @Override
    public List<Integer> getAnswerYear(int classId, int teamId){
        List<Integer> answerYearList = stuAnswerMapper.getAnswerYear(classId, teamId);
        return answerYearList;
    }

    /**
     * 查询年度分析表的年份
	 * @author WangYao
	 * @date 2019年6月12日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public List<Integer> getTeamAnnualWorthGatherYear(int classId, int teamId) {
		List<Integer> teamAnnualWorthGatherYearList = stuAnswerMapper.getTeamAnnualWorthGatherYear(classId, teamId);
        return teamAnnualWorthGatherYearList;
	}

	/**
     * 查询资产负债表的年份
	 * @author WangYao
	 * @date 2019年6月12日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public List<Integer> getTeamBalanceSheetYear(int classId, int teamId) {
		List<Integer> teamBalanceSheetYearList = stuAnswerMapper.getTeamBalanceSheetYear(classId, teamId);
        return teamBalanceSheetYearList;
	}

	/**
     * 查询年度利润表的年份
	 * @author WangYao
	 * @date 2019年6月12日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public List<Integer> getTeamProfitStatementYear(int classId, int teamId) {
		List<Integer> teamProfitStatementYearList = stuAnswerMapper.getTeamProfitStatementYear(classId, teamId);
        return teamProfitStatementYearList;
	}

	/**
     * 查询月度利润表的年份
	 * @author WangYao
	 * @date 2019年6月12日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	@Override
	public List<Integer> getTeamMonthlyProfitStatementYear(int classId, int teamId) {
		Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
		List<Integer> teamMonthlyProfitStatementYearList = stuAnswerMapper.getTeamMonthlyProfitStatementYear(classId, teamId,currentMonth);
        return teamMonthlyProfitStatementYearList;
	}

}
