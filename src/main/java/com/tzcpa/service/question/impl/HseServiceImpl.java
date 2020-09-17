package com.tzcpa.service.question.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.BalanceVariableDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description:组织结构选择 </p>
 * @author WTL
 * @date 2019年5月8日
 */
@Slf4j
@Service("hseService")
@SuppressWarnings("rawtypes")
public class HseServiceImpl extends AHseService {
	
	@Resource
	private OSMapper osMapper;
	
	/**
	 * 组织结构（目前仅仅为组织结构的）
	 */
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
			//根据选择的答案去查找标准答案返回数据说明正确。
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			log.info("根据选择的答案去查找标准答案返回数据说明正确 checkRes={}", JsonUtil.listToJson(checkRes));
			//选择正确后进行添加积分（也有可能选择正确后有影响，目前没不兼容，只是组织结构）
			if (checkRes != null && !checkRes.isEmpty()) {
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hseRequest, checkRes);
			}
			//销量加入延迟影响
			((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
			//设计平衡积分卡
			BalanceVariableDO balanceVariableDO = osMapper.getVariableInfo(VariableConstant.ZZJGH6, JSON.toJSONString(hseRequest.getAnswer()), scoreDO.getStrategicChoice(), hseRequest.getClassId());
			log.info("根据变量名称查找变量信息 balanceVariableDO={}", JSON.toJSONString(balanceVariableDO));
			balanceVariableDO.setTC(scoreDO.getClassId(), scoreDO.getTeamId());
			osMapper.addBalancedVariable(balanceVariableDO);
			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}

}

