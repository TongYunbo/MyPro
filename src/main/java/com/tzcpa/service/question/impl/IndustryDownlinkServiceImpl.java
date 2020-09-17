package com.tzcpa.service.question.impl;
import com.alibaba.fastjson.JSON;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * <p>Description:行业下行经营策略调整 </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service
@SuppressWarnings("rawtypes")
public class IndustryDownlinkServiceImpl extends AHseService  {

	@Resource
	private OSMapper osMapper;

	@Resource
	private InitTeamIntermediateService itidService;

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
				((IndustryDownlinkServiceImpl) AopContext.currentProxy()).doScore(scoreDO, hse);
			}

			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}
	/**
	 * 做答案处理
	 * @param scoreDO
	 * @param hse
	 */
	@Transactional(rollbackFor = Exception.class)
	public void doScore(AnswerScoreDO scoreDO, HseRequest hse) throws Exception{
		log.info("IndustryDownlinkServiceImpl doScore  scoreDO={},hse={}", JSON.toJSONString(scoreDO),JSON.toJSONString(hse));
		try {
			//将传入答案置空去除筛选
			scoreDO.setAnswer(null);
			//根据选择的答案去查找标准答案返回数据说明正确
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			log.info("checkRes={}",JSON.toJSONString(checkRes));
			//由于非空判断，将答案重新赋值
			scoreDO.setAnswer(String.valueOf(hse.getAnswer()));
			//选择正确后进行添加积分（也有可能选择正确后有影响，目前没不兼容，只是组织结构）
			if (checkRes != null && !checkRes.isEmpty() && checkRes.size()>0) {
				double scoreAmount = 0;
				//计算得分合计
				for (Map<String, Object> crMap : checkRes) {
					//后台获取到答案
					List<String> jsonToList = JsonUtil.jsonToList(crMap.get("answer").toString(), String.class);
					//前台传入答案
					List<String> answerPar = hse.getAnswer();
					for (int i = 0; i < answerPar.size(); i++) {
						if (jsonToList.contains(answerPar.get(i))) {
							scoreAmount += Double.valueOf(crMap.get("score").toString());
						}
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
