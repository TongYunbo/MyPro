package com.tzcpa.service.question.impl;
import java.util.ArrayList;
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
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:WEY客户满意度-2 </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service("CustomerSatisfactionServiceImpl")
@SuppressWarnings("rawtypes")
public class CustomerSatisfactionServiceImpl extends AHseService  {
	
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
			//根据选择的答案去查找标准答案返回数据说明正确。
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			log.info("根据选择的答案去查找标准答案返回数据说明正确 checkRes={}", JsonUtil.listToJson(checkRes));
			//选择正确后进行添加积分
			if (checkRes != null && !checkRes.isEmpty()) {
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hseRequest, checkRes);
			}
			//销量加入延迟影响
			scoreDO.setISAndUA();
			((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
			//设计平衡积分卡
			List<BalanceVariableDO> bvList = new ArrayList<BalanceVariableDO>();
			BalanceVariableDO balanceVariableDO = osMapper.getVariableInfo(VariableConstant.HKMYDWEY, JSON.toJSONString(hseRequest.getAnswer()), scoreDO.getStrategicChoice(), scoreDO.getClassId());
			Map<String, Object> record = osMapper.getVariableRecord(scoreDO.getClassId(), scoreDO.getTeamId(), VariableConstant.HKMYDWEY);
			//根据答案对平衡积分卡重新赋值
			balanceVariableDO.setVariableVal(String.valueOf(Integer.valueOf(record.get("variableVal").toString()) + Integer.valueOf(balanceVariableDO.getVariableVal())));
			balanceVariableDO.setTC(scoreDO.getClassId(), scoreDO.getTeamId());
			bvList.add(balanceVariableDO);
			//设计平衡积分卡
			BalanceVariableDO balanceVariableDOp = osMapper.getVariableInfo(VariableConstant.PPMYDWEY, JSON.toJSONString(hseRequest.getAnswer()), scoreDO.getStrategicChoice(), scoreDO.getClassId());
			balanceVariableDOp.setTC(scoreDO.getClassId(), scoreDO.getTeamId());
			bvList.add(balanceVariableDOp);
			osMapper.batchIBalancedVariable(bvList);

			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}

	

}
