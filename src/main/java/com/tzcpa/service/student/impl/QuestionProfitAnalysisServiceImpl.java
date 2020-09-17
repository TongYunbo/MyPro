package com.tzcpa.service.student.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tzcpa.mapper.student.QuestionProfitAnalysisMapper;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.model.treatment.QuestionAnswerDTO;
import com.tzcpa.model.treatment.SecondQuestionDTO;
import com.tzcpa.service.student.IQuestionProfitAnalysisService;

/**
 * <p>Description: 查询盈利能力分析题目处理查询题返回数据</p>
 * @author WTL
 * @date 2019年7月4日
 */
@Service
public class QuestionProfitAnalysisServiceImpl implements IQuestionProfitAnalysisService {
	
	@Resource
	private QuestionProfitAnalysisMapper qpaMapper;

	@Override
	public void handleQuestionData(QuestionAnswerDTO questionAnswerDTO1, Team team) {
		//获取需要替换的数据
		Map<String, BigDecimal> assemblingData = assemblingData(
				Integer.valueOf(questionAnswerDTO1.getTimeLine().substring(0, 4)), team.getClassId(), team.getId());
		List<SecondQuestionDTO> thisChildrenItem = questionAnswerDTO1.getThisChildrenItem();
		SecondQuestionDTO sqd = null;
		for (int i = 0; i < thisChildrenItem.size(); i++) {
			sqd = thisChildrenItem.get(i);
			//不为null，说明是要替换的题
			if (assemblingData.get(String.valueOf(sqd.getThisItemId())) != null) {
				questionAnswerDTO1.getThisChildrenItem().get(i).getThisChildrenItem().get(1).getThisItemOptions().get(0).put("optCon", assemblingData.get(String.valueOf(sqd.getThisItemId())).toString());
			}
		}
	}
	
	/**
	 * 组装需要的数据
	 * @param year
	 * @param classId
	 * @param teamId
	 * @return
	 */
	public Map<String, BigDecimal> assemblingData(Integer year, Integer classId, Integer teamId){
		//查询月度利润表中需要的数据
		Map<String, BigDecimal> mData = qpaMapper.getMonthPData(year, teamId, classId);
		//查询中间表中需要的数据
		Map<String, BigDecimal> tiData = qpaMapper.getTIData(year, teamId, classId);
		
		//合并两个查询出来的数据
		tiData.putAll(mData);
		
		return tiData;
	}

}

