package com.tzcpa.service.question.impl;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.BalanceVariableDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:研发方向选择2 </p>
 * @author LRS
 * @date 2019年5月20日
 */
@Slf4j
@Service("ReseDevelopTServiceImpl")
@SuppressWarnings("rawtypes")
public class ReseDevelopTServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		log.info("传过来的问题数据集合为：{}", hrList);
		try {
			AnswerScoreDO scoreDO = null;
			//设计平衡积分卡
			List<BalanceVariableDO> bvList = new ArrayList<BalanceVariableDO>();
			BalanceVariableDO balanceVariableDO = null;
			//获取战略
			HseRequest hseRequest = hrList.get(0);
			String sc6 = osMapper.getStrategic(hseRequest, NormalConstant.H6);
			String sc8 = osMapper.getStrategic(hseRequest, NormalConstant.H8);
			for (HseRequest hse : hrList) {
				scoreDO = new AnswerScoreDO(hse);
				//设置战略
				scoreDO.setStrategicChoice(hse.getQuestionId().equals(NormalConstant.YFFXXZ2H6_QUESTION_ID) ? sc6 : sc8);
				//根据选择的答案去查找标准答案返回数据说明正确。
				List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
				log.info("根据选择的答案去查找标准答案返回数据说明正确 checkRes={}", JsonUtil.listToJson(checkRes));
				//选择正确后进行添加积分
				if (checkRes != null && !checkRes.isEmpty()) {
					((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
				}

				balanceVariableDO = osMapper.getVariableInfo(
						hse.getQuestionId().equals(NormalConstant.YFFXXZ2H6_QUESTION_ID) ? VariableConstant.YF14H6 : VariableConstant.YF14H8,
						scoreDO.getAnswer(), scoreDO.getStrategicChoice(), scoreDO.getClassId());
				balanceVariableDO.setTC(scoreDO.getClassId(), scoreDO.getTeamId());
				bvList.add(balanceVariableDO);
			}

			osMapper.batchIBalancedVariable(bvList);
			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}
	
	

}
