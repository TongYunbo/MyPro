package com.tzcpa.service.question.impl;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.BalanceVariableDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:企业文化塑造度 </p>
 * @author LRS
 * @date 2019年5月22日
 */
@Slf4j
@Service("CorporateCultureServiceImpl")
@SuppressWarnings("rawtypes")
public class CorporateCultureServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		try {
			HseRequest hseRequest = hrList.get(0);
			AnswerScoreDO scoreDO = new AnswerScoreDO(hseRequest);
			log.info("接收到的数据信息为：" + JSON.toJSONString(scoreDO));
			if (!scoreDO.isQualified()) {
				log.info("参数不全：" + hseRequest);
				//返回参数不全
				return ResponseResult.fail("参数不全");
			}
			//受战略的影响，查询出选择的战略并赋值给对象
			scoreDO.setStrategicChoice(osMapper.getStrategic(hseRequest, NormalConstant.VEHICLE_MODEL_H8));
			//根据选择的答案去查找标准答案返回数据说明正确。
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			log.info("根据选择的答案去查找标准答案返回数据说明正确 checkRes={}", JsonUtil.listToJson(checkRes));
			//选择正确后进行添加积分
			if (checkRes != null && !checkRes.isEmpty()) {
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hseRequest, checkRes);
			}
			//设计平衡积分卡
			BalanceVariableDO balanceVariableDO = osMapper.getVariableInfo(VariableConstant.QYWHH8, JSON.toJSONString(hseRequest.getAnswer()), scoreDO.getStrategicChoice(), scoreDO.getClassId());
			balanceVariableDO.setTC(hseRequest.getClassId(), hseRequest.getTeamId());
			log.info("查询到变量数据信息为：" + JSON.toJSONString(balanceVariableDO));
			osMapper.addBalancedVariable(balanceVariableDO);
			boolean Jude = Judge(Double.valueOf(balanceVariableDO.getVariableVal()));
			if (Jude) {
				//添加延迟影响
				scoreDO.setAnswer(null);
				((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
			}

			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}

	private boolean Judge(Double valueOf) {
		if(valueOf<100){
			return true;
		}
		return false;
	}

}
