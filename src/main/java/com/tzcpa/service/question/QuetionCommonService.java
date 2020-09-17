package com.tzcpa.service.question;

import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.model.treatment.QuestionAnswerDTO;
import com.tzcpa.model.treatment.UserInfo;

import java.util.Date;

public interface QuetionCommonService {

    public int autoAnswer(UserInfo userInfo, Date currentMonth) throws Exception;

    public ResponseResult answer(QuestionAnswerDTO questionAnswerDTO , int teamId, int classId,String Account) throws Exception;

}
