package com.tzcpa.service.question;

import java.util.List;
import java.util.Map;


import com.tzcpa.constant.NormalConstant;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.student.TeamStrategyMapService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description: 战略抽象</p>
 * @author WTL
 * @date 2019年5月24日
 */
@Slf4j
public abstract class AStrategicChoicesService extends AHseService {
	
	@Transactional(rollbackFor = Exception.class)
	public void doStrategicChoices(OSMapper osMapper, HseRequest hse,
								   InitTeamIntermediateService itIDService, String vm, TeamStrategyMapService tsmService) throws Exception{
		try {
			AnswerScoreDO scoreDO = new AnswerScoreDO(hse);
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			//进行添加积分
            ((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);

			//进行添加选择的战略
			osMapper.addStrategic(hse.getClassId(), hse.getTeamId(),
					((NormalConstant.H8.equals(vm) && (!hse.getTimeLine().contains("-11")))
							|| NormalConstant.WEY.equals(vm)) ? getStrategicType(hse.getAnswer().get(0), vm)
							: hse.getAnswer().get(0),
					Integer.valueOf(hse.getTimeLine().substring(0, 4)), vm);

			//13年选择或修改战略的时候不进行战略地图和战略数据的初始化任务（13年在年初执行）
			if (NormalConstant.YEAR_2013.equals(hse.getTimeLine().substring(0, 4))) {
				return;
			}
			// 初始化数据
			itIDService.initTeamIntermediateOfStrategic(getSCInitParam(hse, osMapper, null));
			//处理战略地图
			tsmService.initTeamStrategyMap(getSCInitParam(hse, osMapper, vm.equals(NormalConstant.WEY) ? NormalConstant.WEY : osMapper.getStrategic(hse, vm)));
		}catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 获取战略选择类型
	 * @param answer
	 * @param vm
	 * @return
	 */
	private String getStrategicType(String answer, String vm){
		//本方法用的差异化战略的值和差异化无关，低成本战略同理
		
		//如果是H8的进来的话说明为判断是否为自建外购
		if (NormalConstant.H8.equals(vm)) {
			//选项为A返回自建的战略类型
			if (NormalConstant.CYHZL.equals(answer)) {
				return NormalConstant.ZJZL;
			}
			//选项为B返回外购的战略类型
			if (NormalConstant.DCBZL.equals(answer)) {
				return NormalConstant.WGZL;
			}
		}
		//如果是WEY的进来的话说明为判断是否是停产
		if (NormalConstant.WEY.equals(vm)) {
			//选项为A返回生产的战略类型
			if (NormalConstant.CYHZL.equals(answer)) {
				return NormalConstant.CWEYZL;
			}
			//选项为B返回停产的战略类型
			if (NormalConstant.DCBZL.equals(answer)) {
				return NormalConstant.TWEYZL;
			}
		}
		
		log.info("获取战略选择类型出现未判断的类型：{},答案为：{},默认返回为答案值", vm, answer);
		return answer;
	}

}

