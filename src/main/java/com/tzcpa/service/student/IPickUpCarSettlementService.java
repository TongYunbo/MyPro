package com.tzcpa.service.student;

import com.tzcpa.model.teacher.Team;
import com.tzcpa.model.treatment.QuestionAnswerDTO;

/**
 * <p>Description: </p>
 * @author WTL
 * @date 2019年6月20日
 */
public interface IPickUpCarSettlementService {
	
	public void handleYSActualValue(QuestionAnswerDTO questionAnswerDTO1,Team team);

}

