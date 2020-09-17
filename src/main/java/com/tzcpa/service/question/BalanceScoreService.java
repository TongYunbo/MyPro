package com.tzcpa.service.question;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;

import com.tzcpa.constant.BalanceOptionEnum;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.mapper.treatment.BalanceMapper;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.model.student.OptionDto;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description: </p>
 * @author LRS
 * @date 2019年5月14日
 */
@Slf4j
public abstract class BalanceScoreService extends AHseService{
	
	@Async
	@Transactional(rollbackFor = Exception.class)
	public void BalanceAdd(BalanceMapper bcMapper,List<HseRequest> hrList,String beYear,
						   String sc, Map<String, Double> ppdMap, String questionIds) throws Exception {
		log.info("传过来的问题数据集合为：{}", hrList);
		List<OptionDto> list = new ArrayList<>();
		try {
			//查询除所有题的option
			List<OptionDto> balanceList = bcMapper.findOptionByQuestionIds(questionIds, hrList.get(0).getClassId(), beYear);
			List<OptionDto> optionList = null;
			String[] splitY = beYear.split(",");
			for (int i = 0; i < splitY.length; i++) {
				for (HseRequest hse : hrList) {
					optionList = getOptionList(balanceList, hse, Integer.valueOf(splitY[i]));
					for (OptionDto optionDto : optionList) {
						optionDto.setTeamId(hse.getTeamId());
						optionDto.setOption(BalanceOptionEnum.getOptionNum(optionDto.getQuesionOption()));
						optionDto.setStrategy(sc);
						optionDto.setMatchingDegree(ppdMap.get(hse.getQuestionId() + "-" + optionDto.getQuesionOption()));
						list.add(optionDto);
					}
				}
			}

			//批量修改平衡计分卡数据
			bcMapper.updateBatchBalanceScoreCard(list);
		}catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 获取选中答案的option
	 * @param balanceList
	 * @param hse
	 * @return
	 */
	private List<OptionDto> getOptionList(List<OptionDto> balanceList, HseRequest hse, Integer year){
		List<OptionDto> list = new ArrayList<>();
		for (OptionDto op : balanceList) {
			for (String an : hse.getAnswer()) {
				//如果答案相等并且问题ID和年份一致添加到返回list
				if (op.getQuesionOption().equals(an) && op.getQuestionId().equals(hse.getQuestionId()) && op.getYear().equals(year)) {
					list.add(op);
				}
			}
		}
		return list;
	}
	
	/**
	 * 做答案处理
	 * @param scoreDO
	 * @param scoreAmount
	 * @param hse
	 */
	@Transactional(rollbackFor = Exception.class)
	public void doScore(AnswerScoreDO scoreDO, int scoreAmount, HseRequest hse,
						OSMapper osMapper, String sc, Map<String, Double> ppdMap) throws Exception{
		try {
			//将传入答案置空去除筛选
			scoreDO.setAnswer(null);
			//如果不受战略影响将战略改为null
			if (judgingIsMatchComplete(NormalConstant.ZLMBLH_BSZLYX_ID, hse.getQuestionId())) {
				scoreDO.setStrategicChoice(null);
			}
			//根据选择的答案去查找标准答案返回数据说明正确
			List<Map<String, Object>> checkRes = osMapper.checkRes(scoreDO);
			//选择正确后进行添加积分（也有可能选择正确后有影响，目前没不兼容，只是组织结构）
			if (checkRes != null && !checkRes.isEmpty()) {
				//计算得分合计
				for (Map<String, Object> crMap : checkRes) {
					//处理不是本战略的答案
					if (crMap.get("strategicChoice") != null
							&& StringUtils.isNotBlank(crMap.get("strategicChoice").toString())
							&& (!crMap.get("strategicChoice").toString().equals(sc))) {
						continue;
					}

					//后台获取到答案
					List<String> jsonToList = JsonUtil.jsonToList(crMap.get("answer").toString(), null);
					//前台传入答案
					List<String> answerPar = hse.getAnswer();
					//List<String> jsonParList = JsonUtil.jsonToList(answerPar, null);
					for (String ans : answerPar) {
						if (jsonToList.contains(ans)) {
							scoreAmount += (int) crMap.get("score");
							//为正确答案的话匹配度为1
							ppdMap.put(hse.getQuestionId() + "-" + ans, 1d);
						} else { //不是正确答案匹配度设为0.5
							ppdMap.put(hse.getQuestionId() + "-" + ans, 0.5);
						}
					}
				}

				if (scoreAmount == 0) {
					return;
				}

				//如果为多套答案的话需要减去一个
				if (checkRes.size() > 1) {
					checkRes.remove(0);
				}
				checkRes.get(0).put("score", scoreAmount);
				((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hse, checkRes);
			}
		}catch (Exception e){
			throw e;
		}
		
	}
	
	/**
	 * 判断是否全匹配纪录ID
	 * @param record
	 * @param id
	 * @return
	 */
	private Boolean judgingIsMatchComplete(Integer[] records, Integer id){
		for (Integer rs : records) {
			if (rs.equals(id)) {
				return true;
			}
		}
		return false;
	}
	

}
