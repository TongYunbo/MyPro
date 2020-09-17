package com.tzcpa.service.student;

import java.math.BigDecimal;
import java.util.Map;

import com.tzcpa.model.teacher.Team;
import com.tzcpa.model.treatment.QuestionAnswerDTO;

/**
 * <p>Description: </p>
 * @author WTL
 * @date 2019年7月4日
 */
public interface IQuestionProfitAnalysisService {
	
	void handleQuestionData(QuestionAnswerDTO questionAnswerDTO1,Team team);
	
	Map<String, BigDecimal> assemblingData(Integer year, Integer classId, Integer teamId);

}

