package com.tzcpa.controller.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.IHseService;

/**
 * <p>Description: 学生考试题</p>
 * @author WTL
 * @date 2019年5月8日
 */
@RestController
@RequestMapping("hse")
@SuppressWarnings("rawtypes")
public class HseController {
	
	@Autowired
	@Qualifier("saleBudgetPickupService")
	private IHseService hseService;
	
	@RequestMapping(value = "submitHse", method = RequestMethod.POST)
	public ResponseResult submitHse(@RequestBody List<HseRequest> hrList) throws Exception{
		return hseService.checkOS(hrList);
	}

}

