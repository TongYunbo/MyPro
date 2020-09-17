package com.tzcpa.service.question.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.MessageConstant;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.BalanceVariableDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description: 4P营销H6</p>
 * @author WTL
 * @date 2019年5月14日
 */
@Slf4j
@Service("marketing4PH6Service")
@SuppressWarnings("rawtypes")
public class Marketing4PH6ServiceImpl extends AHseService {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Resource
	private QuestionService questionService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{
		log.info("进入4P营销题目处理当前时间为：" + System.currentTimeMillis());
		log.info("传过来的问题数据集合为：{}", hrList);
		try {
			//判断问题数量是否正确
			if (hrList.size() != NormalConstant.MARKETING4PH6_QUESTION_NUM) {
				return ResponseResult.fail(MessageConstant.MARKETING4PH6_QUESTION_NUM_FAIL);
			}

			StringBuffer questionIds=new StringBuffer();
			AnswerScoreDO scoreDO = null;
			//添加变量纪录的集合
			List<BalanceVariableDO> bvList = new ArrayList<>();
			//存储变量信息的集合
			List<String[]> viList = new ArrayList<>();
			//得分合计，和权重计算前的分数，和入库的值不一样
			double scoreAmount = 0;
			//获取战略
			String sc = osMapper.getStrategic(hrList.get(0), NormalConstant.H6);
			//循环执行问题
			for (HseRequest hse : hrList) {
				questionIds.append(hse.getQuestionId() + ",");
				scoreDO = new AnswerScoreDO(hse);

				//受战略的影响，查询出选择的战略并赋值给对象
				scoreDO.setVMSC(NormalConstant.H6, sc);

				//做学生积分处理
				scoreAmount = ((Marketing4PH6ServiceImpl) AopContext.currentProxy()).doScore(scoreDO, scoreAmount, hse);

				// 获取需要变量信息并进行组装
				((Marketing4PH6ServiceImpl) AopContext.currentProxy()).analysisVariable(scoreDO, viList, hse, bvList);

				//如果影响是和本题无顺序关联的直接放入延迟影响
				if (scoreDO.getQuestionId() == NormalConstant.FPKHMYDH6_MAPPING_QUESTION_ID) {
					((Marketing4PH6ServiceImpl) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
				}

			}

			//由于有个影响需要总得分并且需要在其他的影响之前影响所以这个地方再次处理影响
			//获取所需变量值
			//获取本题的满分
			Double mf = osMapper.getYXMF(scoreDO.getClassId(), questionIds.substring(0, questionIds.length() - 1));
			double FPXLH6 = scoreAmount / mf;
			//修改变量结构值
			scoreDO.setIIQ(false, NormalConstant.FPJGMYDH6_MAPPING_QUESTION_ID);
			viList.add(new String[]{VariableConstant.FPXLH6, NormalConstant.REPLACEMENT_VALUE, String.valueOf(FPXLH6), null});
			log.info("变量信息的集合为：{}", JSON.toJSONString(viList));
			((Marketing4PH6ServiceImpl) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, viList);

			//添加4PXLH6变量到纪录表
			bvList.add(new BalanceVariableDO(scoreDO.getTeamId(), scoreDO.getClassId(),
					VariableConstant.FPXLH6, String.valueOf(FPXLH6), NormalConstant.UNIT_BFH));
			osMapper.batchIBalancedVariable(bvList);

			//@处理注释
			//题为在月底，所以执行延迟任务
//		handleDelayedUpdate(osMapper, hrList.get(0), itidService);
			//处理月度利润
//        questionService.autoAnswer(new UserInfo(scoreDO.getClassId(), scoreDO.getTeamId()));
			log.info("处理完成4P营销题目处理当前时间为：" + System.currentTimeMillis());
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
	public double doScore(AnswerScoreDO scoreDO, double scoreAmount, HseRequest hse) throws Exception{
		//根据选择的答案去查找标准答案返回数据说明正确
		List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
		//选择正确后进行添加积分（也有可能选择正确后有影响，目前没不兼容，只是组织结构）
		if (checkRes != null && !checkRes.isEmpty()) {
			//计算得分合计
			for (Map<String, Object> crMap : checkRes) {
				scoreAmount += (Integer) crMap.get("score");
			}
			((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
		}
		
		return scoreAmount;
	}
	
	/**
	 * 解析变量，并添加到变量集合
	 * @param scoreDO
	 * @param viList
	 * @param hse
	 */
	@Transactional(rollbackFor = Exception.class)
	public void analysisVariable(AnswerScoreDO scoreDO, List<String[]> viList,
								  HseRequest hse, List<BalanceVariableDO> bvList) throws Exception{
		log.info("scoreDO:{},viList:{},hse:{},bvList:{}", scoreDO, JsonUtil.listToJson(viList), hse, JsonUtil.listToJson(bvList));
		try {
			Map<String, String> variableInfo = null;
			String vName = null;
			if (hse.getQuestionId().equals(NormalConstant.FPJGMYDH6_MAPPING_QUESTION_ID)) {
				vName = VariableConstant.FPJGMYDH6;
				variableInfo = addVariableInfo(osMapper, viList, VariableConstant.FPJGMYDH6, scoreDO.getAnswer(), scoreDO.getStrategicChoice(),
						NormalConstant.SUBTRACT_EFFECT, hse.getTimeLine().substring(0, 4), hse.getClassId());
			}
			if (hse.getQuestionId().equals(NormalConstant.FPKHMYDH6_MAPPING_QUESTION_ID)) {
				vName = VariableConstant.FPKHMYDH6;
				variableInfo = addVariableInfo(osMapper, viList, VariableConstant.FPKHMYDH6, scoreDO.getAnswer(), scoreDO.getStrategicChoice(),
						NormalConstant.SUBTRACT_EFFECT, hse.getTimeLine().substring(0, 4), hse.getClassId());
			}

			if (variableInfo == null) {
				return;
			}
			bvList.add(new BalanceVariableDO(hse.getTeamId(), hse.getClassId(), vName, variableInfo.get("variableVal"), variableInfo.get("unit")));
		}catch (Exception e){
			throw e;
		}
		}
	
}

