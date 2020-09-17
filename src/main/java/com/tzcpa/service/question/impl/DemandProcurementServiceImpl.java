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
 * <p>Description:需求采购组合决策 </p>
 * @author LRS
 * @date 2019年5月22日
 */
@Slf4j
@Service("DemandProcurementServiceImpl")
@SuppressWarnings("rawtypes")
public class DemandProcurementServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		try {
			HseRequest hseRequest = hrList.get(0);
			double XQCGH8 = 0;
			AnswerScoreDO scoreDO = new AnswerScoreDO(hseRequest);
			log.info("接收到的数据信息为：" + JSON.toJSONString(scoreDO));
			if (!scoreDO.isQualified()) {
				log.info("参数不全：" + hseRequest);
				//返回参数不全
				return ResponseResult.fail("参数不全");
			}
			//受战略的影响，查询出选择的战略并赋值给对象
			scoreDO.setStrategicChoice(osMapper.getStrategic(hseRequest, NormalConstant.VEHICLE_MODEL_H8));
			//查询除此战略的正确答案
			scoreDO.setAnswer(null);
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			log.info("根据选择的答案去查找标准答案返回数据说明正确 checkRes={}", JsonUtil.listToJson(checkRes));
			//选择正确后进行添加积分
			if (checkRes != null && !checkRes.isEmpty()) {
				int scoreT = 0;
				//标准答案集合
				String sAnswer = checkRes.get(0).get("answer").toString();
				for (int i = 0; i < hseRequest.getAnswer().size(); i++) {
					//做对一道题得十分
					if (sAnswer.contains(hseRequest.getAnswer().get(i))) {
						scoreT += Integer.valueOf(checkRes.get(0).get("score").toString());
					}
				}
				checkRes.get(0).put("score", scoreT);
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hseRequest, checkRes);
			}

			//获取变量值
			for (String ans : hseRequest.getAnswer()) {
				XQCGH8 += Double.valueOf(osMapper.getVariableInfo(VariableConstant.XQCGH8, "[\"" + ans + "\"]",
						scoreDO.getStrategicChoice(), scoreDO.getClassId()).getVariableVal());
			}

			if (Judge(XQCGH8)) {
				//销量和成本添加团队的延迟执行任务
				((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
			}

			//添加变量
			osMapper.addBalancedVariable(new BalanceVariableDO(scoreDO.getTeamId(), scoreDO.getClassId(),
					VariableConstant.XQCGH8, String.valueOf(XQCGH8), NormalConstant.UNIT_BFH));
			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}

	private boolean Judge(double xQCGH8) {
		if(xQCGH8<60){
			return true;
		}
		return false;
	}

	
	

}
