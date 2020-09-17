package com.tzcpa.service.question.impl;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

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
 * <p>Description:4p广告追加判断 </p>
 * @author LRS
 * @date 2019年5月20日
 */
@Slf4j
@Service("Advertisement4pServiceImpl")
@SuppressWarnings("rawtypes")
public class Advertisement4pServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Resource
	private QuestionService questionService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception {
		log.info("传过来的问题数据集合为：{}", hrList);
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
			}
			//销量添加团队的延迟执行任务
			((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
			//}
			((AHseService) AopContext.currentProxy()).handleFinanceImpact(osMapper, scoreDO, scoreDO.newHseInstance(), null);

			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}


	

}
