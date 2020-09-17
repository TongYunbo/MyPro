package com.tzcpa.service.question.impl;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:全线产品矩阵</p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class FullProductMatrixServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		log.info("传过来的问题数据集合为：{}", hrList);
		try {
			AnswerScoreDO scoreDO = null;
			//循环执行问题
			for (HseRequest hse : hrList) {
				scoreDO = new AnswerScoreDO(hse);
				//做学生积分处理
				((FullProductMatrixServiceImpl) AopContext.currentProxy()).doScore(scoreDO, hse);
			}
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
			//将传入答案置空去除筛选
			scoreDO.setAnswer(null);
			//根据选择的答案去查找标准答案返回数据说明正确
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			//选择正确后进行添加积分
			if (checkRes != null && !checkRes.isEmpty()) {
				Map<String, Object> crMap = checkRes.get(0);
				//后台获取到答案
				String jsonAnswer = crMap.get("answer").toString();
				//前台传入答案
				List<String> answerPar = hse.getAnswer();
				//分数
				double scoreAmount = 0;
				//String[] array=answerPar.toArray(new String[answerPar.size()]);
				for (int i = 0; i < answerPar.size(); i++) {
					String ans = answerPar.get(i);
					if (jsonAnswer.contains(ans)) {
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
