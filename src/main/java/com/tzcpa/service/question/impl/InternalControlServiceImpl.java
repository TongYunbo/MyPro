package com.tzcpa.service.question.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.tzcpa.constant.InternalControlEnum;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.BalanceVariableDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description:内部控制WEY </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
@Service("InternalControlServiceImpl")
@SuppressWarnings("rawtypes")
public class InternalControlServiceImpl extends AHseService  {
	
	@Resource
	private OSMapper osMapper;
	
	@Resource
	private InitTeamIntermediateService itidService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult checkOS(List<HseRequest> hrList) throws Exception {
		log.info("传过来的问题数据集合为：{}", JsonUtil.listToJson(hrList));
		try {
			AnswerScoreDO scoreDO = null;
			HseRequest hse = null;

			//问题类型
			int qtType = 0;
			//结果map
			Map<String, Object> rMap = new HashMap<>();
			//isR判断类型是否正确(用来判定是给判断是否恰当积分),赋值初始值为true
			rMap.put("isR-" + hrList.get(0).getQuestionId(), true);
			//循环执行问题
			for (int i = 0; i < hrList.size(); i++) {
				hse = hrList.get(i);
				scoreDO = new AnswerScoreDO(hse);
				//执行影响
				((InternalControlServiceImpl) AopContext.currentProxy()).doImpact(hse.getQuestionId(), hse, scoreDO);
				//获取问题类型
				qtType = InternalControlEnum.getQTType(hse.getQuestionId() % 3);
				//如果说类型为3说明为老师判卷在此不执行加分,或者类型为2但是类型判断错误不执行
				if (qtType == InternalControlEnum.QT3.getType()
						|| (qtType == InternalControlEnum.QT2.getType() && (rMap.get("isR-" + (hse.getQuestionId() - 1)) == null || !Boolean.valueOf(rMap.get("isR-" + (hse.getQuestionId() - 1)).toString())))) {
					//isR判断类型是否正确(用来判定是给判断是否恰当积分),在这里重新初始化值
					rMap.put("isR-" + hse.getQuestionId(), false);
					continue;
				}
				//做学生积分处理
				((InternalControlServiceImpl) AopContext.currentProxy()).doScore(scoreDO, hse, rMap, qtType);
			}
			//设计平衡积分卡
			osMapper.addBalancedVariable(new BalanceVariableDO(scoreDO.getTeamId(), scoreDO.getClassId(),
					VariableConstant.NBKZWEY, rMap.get("NBKZWEY") == null ? "0" : rMap.get("NBKZWEY").toString(), NormalConstant.UNIT_BFH));

			return ResponseResult.success();
		}catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 处理影响
	 * @param n
	 * @param hse
	 */
	@Transactional(rollbackFor = Exception.class)
	public void doImpact(Integer n, HseRequest hse, AnswerScoreDO scoreDO) throws Exception {
		//第三题，第十一题立即执行影响
		try {
			if (NormalConstant.NBKZ_YXQT_3.equals(n) || NormalConstant.NBKZ_YXQT_11.equals(NormalConstant.NBKZ_YXQT_11)) {
				((AHseService) AopContext.currentProxy()).handleFinanceImpact(osMapper, scoreDO, hse, null);
			}

			//第十二题添加延迟数据影响
			if (NormalConstant.NBKZ_YXQT_12.equals(NormalConstant.NBKZ_YXQT_12)) {
				((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, scoreDO, null);
			}
		}catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 做答案处理
	 * @param scoreDO
	 * @param
	 * @param hse
	 */
	@Transactional(rollbackFor = Exception.class)
	public void doScore(AnswerScoreDO scoreDO, HseRequest hse, Map<String, Object> rMap, int qtType) throws Exception{
		//根据选择的答案去查找标准答案返回数据说明正确
		try {
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			//选择正确后进行添加积分
			if (checkRes != null && !checkRes.isEmpty()) {
				//如果此题为类型判断题正确了则添加变量值
				if (qtType == InternalControlEnum.QT1.getType()) {
					//平衡计分卡变量值计算
					rMap.put("NBKZWEY", Integer.valueOf(rMap.get("NBKZWEY") == null ? "0" : rMap.get("NBKZWEY").toString()) + 10);
				}
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
			} else {
				//如果分类题没有做正确
				if (qtType == InternalControlEnum.QT1.getType()) {
					rMap.put("isR-" + hse.getQuestionId(), false);
				}
			}
		}catch (Exception e){
			throw e;
		}
	}

	

}
