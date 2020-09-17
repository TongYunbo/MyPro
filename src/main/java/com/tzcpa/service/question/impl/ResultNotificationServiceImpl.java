package com.tzcpa.service.question.impl;
import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.BalanceVariableDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


/**
 * <p>Description:并购结果告知 </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class ResultNotificationServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Override
	public ResponseResult checkOS(List<HseRequest> hrList) {
		HseRequest hseRequest = hrList.get(0);
		AnswerScoreDO scoreDO = new AnswerScoreDO(hseRequest);
		if (!scoreDO.isQualified()) {
			log.info("参数不全：" + hseRequest);
			//返回参数不全
			return ResponseResult.fail("参数不全");
		}
		//获取变量信息
		BalanceVariableDO variableInfo = osMapper.getVariableInfo(VariableConstant.BGJGGZ,JSON.toJSONString(hseRequest.getAnswer()), null,scoreDO.getClassId());
		return ResponseResult.success(variableInfo.getVariableVal());
	}

	

}
