package com.tzcpa.service.question.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

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
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description: 4P营销H8</p>
 *
 * @author wangbj 4P营销H8
 * @date 2019年5月20日
 */
@SuppressWarnings("rawtypes")
@Service("marketing4PH8Service")
public class Marketing4PH8ServiceImpl extends AHseService {

    @Resource
    private OSMapper osMapper;

//    @Autowired
//    private InitTeamIntermediateService teamIntermediateService;
    
    @Resource
	private QuestionService questionService;

	@Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{

	    try {
            // 判断问题数量
            if (!NormalConstant.MARKETING4PH8_QUESTION_NUM.equals(hrList.size())) {
                return ResponseResult.fail(MessageConstant.MARKETING4PH8_QUESTION_NUM_FAIL);
            }
            String questionIds = "";
            // 存储变量信息的集合
            List<String[]> viList = new ArrayList<>();
            // 得分合计
            double scoreAmount = 0;
            //获取战略
            String sc = osMapper.getStrategic(hrList.get(0), NormalConstant.VEHICLE_MODEL_H8);
            // 多小题循环处理
            for (HseRequest hseRequest : hrList) {
            	questionIds += hseRequest.getQuestionId() + ",";
                AnswerScoreDO answerScoreDO = new AnswerScoreDO(hseRequest);
                // 战略影响赋值
                answerScoreDO.setStrategicChoice(sc);
                // 总分处理
                scoreAmount = ((Marketing4PH8ServiceImpl) AopContext.currentProxy()).doScore(hseRequest, answerScoreDO, scoreAmount);
                // 质量问题处理
                if (NormalConstant.FPKHMYDH8_MAPPING_QUESTION_ID.equals(answerScoreDO.getQuestionId())) {
                    Map<String, String> viValue = addVariableInfo(osMapper, viList, VariableConstant.FPKHMYDH8,
                            answerScoreDO.getAnswer(), answerScoreDO.getStrategicChoice(), NormalConstant.SUBTRACT_EFFECT,
                            hseRequest.getTimeLine().substring(0, 4), hseRequest.getClassId()
                    );
                    // 保存平衡记分卡变量
                    osMapper.addBalancedVariable(new BalanceVariableDO(
                            answerScoreDO.getTeamId(), answerScoreDO.getClassId(), VariableConstant.FPKHMYDH8,
                            String.valueOf(viValue.get("variableVal")), viValue.get("unit")
                    ));
                    ((AHseService) AopContext.currentProxy()).addVariableInfo(osMapper, viList, VariableConstant.FPSHFUFH8,
                            answerScoreDO.getAnswer(), null, NormalConstant.JUDGE_EFFECT_REPLACE,
                            hseRequest.getTimeLine().substring(0, 4), hseRequest.getClassId()
                    );
                    // 增加延迟任务 4PKHMYDH8 和 售后服务费
                    answerScoreDO.setAnswer(null);
                    answerScoreDO.setStrategicChoice(null);
                    ((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, answerScoreDO, viList);
                }

                // 最后一题
                if (NormalConstant.FPXLH8_MAPPING_QUESTION_ID.equals(answerScoreDO.getQuestionId())) {
                	//获取本题的满分
        			Double mf = osMapper.getYXMF(answerScoreDO.getClassId(), questionIds.substring(0, questionIds.length() - 1));
                    double FPXLH8 = (scoreAmount / mf) * 1;
                    viList.add(new String[]{
                            VariableConstant.FPXLH8, NormalConstant.REPLACEMENT_VALUE, String.valueOf(FPXLH8), null, null
                    });
                    // 增加延迟任务 4PXLH8
                    answerScoreDO.setAnswer(null);
                    answerScoreDO.setStrategicChoice(null);
                    ((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, answerScoreDO, viList);
                }
            }

            return ResponseResult.success();
        }catch (Exception e){
	        throw e;
        }
    }

    /**
     * 多小题分数处理
     *
     * @param hseRequest
     * @param answerScoreDO
     * @param scoreAmount
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public double doScore(HseRequest hseRequest, AnswerScoreDO answerScoreDO, double scoreAmount) throws Exception {
        try {
            // 查询分数
            List<Map<String, Object>> checkRes = osMapper.checkRes(answerScoreDO);
            if (checkRes != null && !checkRes.isEmpty()) {
                // 计算得分合计
                for (Map<String, Object> crMap : checkRes) {
                    scoreAmount += (Integer) crMap.get("score");
                }
                // 给角色加分并写库
                ((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hseRequest, checkRes);
            }
            return scoreAmount;
        }catch (Exception e){
            throw e;
        }
    }

}
