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
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:内部审计 </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class InternalAuditingServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		log.info("传过来的问题数据集合为：{}", JsonUtil.listToJson(hrList));
		try {
			AnswerScoreDO scoreDO = null;
			//得分合计
			double scoreAmount = 0;
			HseRequest hse = null;
			//循环执行问题
			for (int i = 0; i < hrList.size(); i++) {
				//如果为null或者空字符串不需要进行积分处理
				if (hrList.get(i).getAnswer() == null || hrList.get(i).getAnswer().get(0) == null || "".equals(hrList.get(i).getAnswer().get(0))) {
					continue;
				}
				hse = hrList.get(i);
				scoreDO = new AnswerScoreDO(hse);
				//做学生积分处理
				((InternalAuditingServiceImpl) AopContext.currentProxy()).doScore(scoreDO, scoreAmount, hse);
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
	public void doScore(AnswerScoreDO scoreDO, double scoreAmount, HseRequest hse) throws Exception{
		//根据选择的答案去查找标准答案返回数据说明正确
		try {
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			//选择正确后进行添加积分
			if (checkRes != null && !checkRes.isEmpty()) {
				//计算得分合计
				for (Map<String, Object> crMap : checkRes) {
					scoreAmount += (Integer) crMap.get("score");
				}
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
			}
		}catch (Exception e){
			throw e;
		}
	}

}
