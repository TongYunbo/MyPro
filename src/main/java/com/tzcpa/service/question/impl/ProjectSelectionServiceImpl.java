package com.tzcpa.service.question.impl;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.Pattern.Flag;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:投资决策-项目选择 </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class ProjectSelectionServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Resource
    private QuestionService questionService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		log.info("传过来的问题数据集合为：{}", hrList);
		try {
			HseRequest hse = hrList.get(0);
			AnswerScoreDO scoreDO = new AnswerScoreDO(hse);
			//设定每道题添加分数初始值
			int scoreAmount = 0;

			//添加积分，返回是否为最优答案，非最优答案的时候添加影响
			if (((ProjectSelectionServiceImpl) AopContext.currentProxy()).doScore(scoreDO, scoreAmount, hse)) {
				//将答案值为空
				scoreDO.setAnswer(null);
				((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
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
	public Boolean doScore(AnswerScoreDO scoreDO, int scoreAmount, HseRequest hse) throws Exception{
		try {
			//将传入答案置空去除筛选
			scoreDO.setAnswer(null);
			//根据选择的答案去查找标准答案返回数据说明正确
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			//最优分数
			int standardScore = 0;
			//选择正确后进行添加积分（也有可能选择正确后有影响，目前没不兼容，只是组织结构）
			if (checkRes != null && !checkRes.isEmpty()) {
				Boolean flag = true;
				//计算得分合计
				for (Map<String, Object> crMap : checkRes) {
					//后台获取到答案
					List<String> jsonToList = JsonUtil.jsonToList(crMap.get("answer").toString(), String.class);
					//赋值最优分数(因为只有一条数据)
					standardScore = Integer.valueOf(crMap.get("score").toString()) * jsonToList.size();
					//前台传入答案
					List<String> answerPar = hse.getAnswer();
					for (int i = 0; i < answerPar.size(); i++) {
						if (!crMap.get("answer").toString().contains(answerPar.get(i))) {
							flag = false;
						}
					}
				}
				checkRes.get(0).put("score", flag ? (int)checkRes.get(0).get("score") * 2 : 0);
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
				//相等说明为最优答案不需要加数据影响处理
				if (standardScore == Integer.valueOf(checkRes.get(0).get("score").toString())) {
					return false;
				}
			}

			return true;
		}catch (Exception e){
			throw e;
		}
	}

}
