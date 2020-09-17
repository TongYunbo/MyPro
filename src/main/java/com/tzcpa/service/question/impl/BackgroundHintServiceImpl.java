package com.tzcpa.service.question.impl;
import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.BalanceVariableDO;
import com.tzcpa.model.student.HseRequest;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
/**
 * <p>Description:背景补充提示</p>
 * @author LRS
 * @date 2019年5月27日
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class BackgroundHintServiceImpl{
	
	@Resource
	private OSMapper osMapper;
	/**
	 * <p>Description:罚单提示</p>
	 * @author LRS
	 * @date 2019年5月27日
	 */
	public ResponseResult InfringementNotice(List<HseRequest> hrList) {
		HseRequest hse = hrList.get(0);
		//获取变量信息
		BalanceVariableDO variableInfo = osMapper.getVariableInfo(VariableConstant.FADAN,null, null,hse.getClassId());
		log.info("根据变量名称查找变量信息variableInfo={}",JSON.toJSONString(variableInfo));
		return ResponseResult.success(variableInfo.getVariableVal());
	}
	/**
	 * <p>Description:损失提示</p>
	 * @author LRS
	 * @date 2019年5月27日
	 */
	public ResponseResult Loss(List<HseRequest> hrList) {
		HseRequest hse = hrList.get(0);
		//获取变量信息
		BalanceVariableDO variableInfo = osMapper.getVariableInfo(VariableConstant.SUNSHI,null, null,hse.getClassId());
		log.info("根据变量名称查找变量信息variableInfo={}",JSON.toJSONString(variableInfo));
		return ResponseResult.success(variableInfo.getVariableVal());
	}


}
