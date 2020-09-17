package com.tzcpa.service.question.impl;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:投资决策-设备更新 </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class EquipmentRenewalServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception {
		log.info("接收到的数据信息为：" + JSON.toJSONString(hrList));
		try {
			AnswerScoreDO scoreDO = null;
			for (HseRequest hse : hrList) {
				scoreDO = new AnswerScoreDO(hse);
				//根据选择的答案去查找标准答案返回数据说明正确。
				List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
				log.info("根据选择的答案去查找标准答案返回数据说明正确 checkRes={}", JsonUtil.listToJson(checkRes));
				//选择正确后进行添加积分
				if (checkRes != null && !checkRes.isEmpty()) {
					((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
				}

				//为非选中的时候进行处理（为第二道题所以不再复原）
				if (hse.getQuestionId().equals(NormalConstant.YZJC_SBGX_QUESTION_ID)) {
					scoreDO.setAnswer("'" + scoreDO.getAnswer() + "'");
					scoreDO.setIsSelect(1);
				}
				//正确管理费用-其他减少10%进行延迟影响处理
				((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
				//进行影响处理
				((AHseService) AopContext.currentProxy()).handleFinanceImpact(osMapper, scoreDO, hse, null);
			}

			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}
	

}
