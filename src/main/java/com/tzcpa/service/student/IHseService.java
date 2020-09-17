package com.tzcpa.service.student;

import java.util.List;

import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.student.HseRequest;

/**
 * <p>Description: </p>
 * @author WTL
 * @date 2019年5月8日
 */
@SuppressWarnings("rawtypes")
public interface IHseService {
	
	ResponseResult checkOS(List<HseRequest> hrList) throws Exception;
}

