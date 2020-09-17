package com.tzcpa.service.question.impl;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:发行股份决议 </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class IssueGaoServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception {
		try {
			HseRequest hse = hrList.get(0);
			log.info("发股份决议做题传参为：{}", hse);

			AnswerScoreDO scoreDO = new AnswerScoreDO(hse);
			String fgAnswer = osMapper.getAnswerByQID(hse, NormalConstant.RZFGJY_QUESTION_ID);
			scoreDO.setAnswer(fgAnswer + scoreDO.getAnswer());
			//处理立即处理的影响
			((AHseService) AopContext.currentProxy()).handleFinanceImpact(osMapper, scoreDO, hse, null);
			//添加需要进行延迟处理的任务
			((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}


}
