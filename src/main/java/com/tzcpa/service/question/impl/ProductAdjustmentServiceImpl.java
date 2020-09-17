package com.tzcpa.service.question.impl;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
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
 * <p>Description:产品管理结构调整 </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class ProductAdjustmentServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		log.info("产品管理结构调整传过来的问题数据集合为：{}", JsonUtil.listToJson(hrList));
		try {
			AnswerScoreDO scoreDO = null;
			for (HseRequest hse : hrList) {
				scoreDO = new AnswerScoreDO(hse);
				scoreDO.setAnswer(null);
				//根据选择的答案去查找标准答案返回数据说明正确。
				List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
				if (checkRes != null){
					log.info("根据选择的答案去查找标准答案返回数据说明正确 checkRes={}", JsonUtil.listToJson(checkRes));
					//处理所得积分
					handleScore(checkRes, hse);
				}

				//选择正确后进行添加积分
				if (checkRes != null && !checkRes.isEmpty()) {
					((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
				}
				
				//重新赋值答案
				scoreDO.setAnswer(JSON.toJSONString(hse.getAnswer()));
				//进行影响处理
				scoreDO.setISAndUA();
				((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
			}
			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}
	
	private void handleScore(List<Map<String, Object>> checkRes, HseRequest hse){
		Double score = Double.valueOf((Integer)checkRes.get(0).get("score"));
		checkRes.get(0).put("score", 0);
		//标准答案
		List<String> sAn = JsonUtil.jsonToList(checkRes.get(0).get("answer").toString(), String.class);
		//答题答案
		List<String> answer = hse.getAnswer();
		for (int i = 0; i < answer.size(); i++) {
			//如果答案对得上(每道题里有三个答案这三个答案是按照顺序的)
			if (answer.get(i).equals(sAn.get(i))) {
				checkRes.get(0).put("score", Double.valueOf(checkRes.get(0).get("score").toString()) + score);
			}
		}
	}
	

}
