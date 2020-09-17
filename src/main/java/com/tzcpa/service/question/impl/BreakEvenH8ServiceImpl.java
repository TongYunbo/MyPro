package com.tzcpa.service.question.impl;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:盈亏平衡-H8销量测算</p>
 * @author LRS
 * @date 2019年5月27日
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class BreakEvenH8ServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		log.info("传过来的问题数据集合为：{}", hrList);
		try {
			HseRequest hse = hrList.get(0);
			AnswerScoreDO scoreDO = new AnswerScoreDO(hse);
			((BreakEvenH8ServiceImpl) AopContext.currentProxy()).doScore(scoreDO, hse);
			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}

	/**
	 * 做答案处理
	 * @param scoreDO
	 * @param scoreAmount
	 * @param hse
	 */
	@Transactional(rollbackFor = Exception.class)
	public void doScore(AnswerScoreDO scoreDO, HseRequest hse) throws Exception{
		try {
			double scoreAmount = 0;
			//将传入答案置空去除筛选
			scoreDO.setAnswer(null);
			//根据选择的答案去查找标准答案返回数据说明正确
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			//由于非空判断，将答案重新赋值
			scoreDO.setAnswer(JsonUtil.listToJson(hse.getAnswer()));
			if (checkRes != null && !checkRes.isEmpty()) {
				//由于只有一条答案则直接取第一条数据
				Map<String, Object> crMap = checkRes.get(0);
				//后台获取到答案
				List<String> jsonToList = JsonUtil.jsonToList(crMap.get("answer").toString(), String.class);
				for (int i = 0; i < hse.getAnswer().size(); i++) {
					//由于两个空需要对应所以按照下标进行比对值
					if (jsonToList.get(i).equals(hse.getAnswer().get(i))) {
						scoreAmount += Double.valueOf(crMap.get("score").toString());
					}
				}
				checkRes.get(0).put("score", scoreAmount);
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
			}
		}catch (Exception e){
			throw e;
		}
	}


}
