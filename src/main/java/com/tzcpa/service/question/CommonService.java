package com.tzcpa.service.question;

import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.student.HseRequest;

import java.util.List;

public interface CommonService {

    public ResponseResult callingMethod(String eventCode, List<HseRequest> answers) throws Exception;

    public ResponseResult calling(String eventCode, List<HseRequest> answers) throws Exception;
}
