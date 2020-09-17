package com.tzcpa.service.question.impl;

import com.tzcpa.constant.MessageConstant;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.BalanceVariableDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 4P营销WEY</p>
 *
 * @author wangbj 4P营销WEY
 * @date 2019年5月22日
 */
@SuppressWarnings("rawtypes")
@Service("marketing4PWEYService")
public class Marketing4PWEYServiceImpl extends AHseService {

    @Resource
    private OSMapper osMapper;

	@Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult checkOS(List<HseRequest> hrList) throws Exception{

	    try {
            // 判断问题数量
            if (hrList.size() != NormalConstant.MARKETING4PWEY_QUESTION_NUM) {
                return ResponseResult.fail(MessageConstant.MARKETING4PWEY_QUESTION_NUM_FAIL);
            }

            // 多小题循环处理
            for (HseRequest hseRequest : hrList) {

                AnswerScoreDO answerScoreDO = new AnswerScoreDO(hseRequest);

                // 单题分数处理
                List<Map<String, Object>> checkRes = osMapper.checkRes(answerScoreDO);
                if (checkRes != null && !checkRes.isEmpty()) {
                    ((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hseRequest, checkRes);
                }

                // 增加延迟任务
                answerScoreDO.setISAndUA();
                ((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, answerScoreDO, null);

                // 问题5
                if (NormalConstant.FPWEY_MAPPING_QUESTION_ID.equals(answerScoreDO.getQuestionId())) {
                    answerScoreDO.setAnswer(answerScoreDO.getAnswer().replace("'", ""));
                    // 获取变量信息
                    BalanceVariableDO variableInfo = osMapper.getVariableInfo(
                            VariableConstant.FPKHXYLWEY, answerScoreDO.getAnswer(), null, hseRequest.getClassId()
                    );
                    // 保存平衡记分卡变量
                    osMapper.addBalancedVariable(new BalanceVariableDO(
                            answerScoreDO.getTeamId(), answerScoreDO.getClassId(), VariableConstant.FPKHXYLWEY,
                            variableInfo.getVariableVal(), variableInfo.getUnit()
                    ));
                }
            }

            return ResponseResult.success();
        }catch (Exception e){
	        throw e;
        }
    }

}
