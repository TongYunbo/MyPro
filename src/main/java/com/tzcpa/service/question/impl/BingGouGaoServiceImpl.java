package com.tzcpa.service.question.impl;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


/**
 * <p>Description:并购结果告知 </p>
 * @author LRS
 * @date 2019年6月27日
 */
@Service
@SuppressWarnings("rawtypes")
public class BingGouGaoServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Override
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		
	return ResponseResult.success();
	}


}
