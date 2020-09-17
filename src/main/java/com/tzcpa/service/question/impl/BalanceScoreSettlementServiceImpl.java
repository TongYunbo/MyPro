package com.tzcpa.service.question.impl;

import com.tzcpa.mapper.student.BalancedScorecardMapper;
import com.tzcpa.mapper.treatment.*;
import com.tzcpa.model.question.BalanceScoreSettlementDO;
import com.tzcpa.model.student.BalancedScorecard;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.question.BalanceScoreSettlementService;
import com.tzcpa.utils.FormatNumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 平衡积分卡 实际值 最终得分 计算及入库
 *
 * @author wangbj 平衡积分卡 实际值 最终得分 计算及入库
 * @date 2019年5月30日
 */
@Service
@Slf4j
public class BalanceScoreSettlementServiceImpl implements BalanceScoreSettlementService {

    @Resource
    private BalancedScorecardMapper balancedScorecardMapper;

    @Resource
    private BalanceJsMapper baJsMapper;

    @Resource
    private OSMapper osMapper;
    // 中间表
    @Resource
    private TeamIntermediateMapper teamIntermediateMapper;
    // 资产负债表
    @Resource
    private TeamBalanceSheetMapper teamBalanceSheetMapper;
    // 利润表
    @Resource
    private TeamProfitStatementMapper teamProfitStatementMapper;

    /**
     * 平衡积分卡 实际值 最终得分 计算及入库
     *
     * @param hseRequest
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void BalanceScoreSettlement(HseRequest hseRequest) throws Exception {

        try{
        // 根据 班级 团队 年 查询平衡计分卡
        BalancedScorecard scorecardParam = new BalancedScorecard();
        scorecardParam.setClassId(hseRequest.getClassId());
        scorecardParam.setTeamId(hseRequest.getTeamId());
        String currYear = hseRequest.getTimeLine().substring(0, 4);
        scorecardParam.setYear(Integer.parseInt(currYear));
        List<BalancedScorecard> listBalancedScorecard = balancedScorecardMapper.selectTeamBalancedScorecard(scorecardParam);

        // 循环平衡记分卡
        for (BalancedScorecard balancedScorecard : listBalancedScorecard) {

            String methodName = balancedScorecard.getBalanceScoreFunc();
            if (methodName == null || methodName.isEmpty()) {
                continue;
            }

            BalanceScoreSettlementDO balanceScoreSettlementDO = new BalanceScoreSettlementDO(baJsMapper, osMapper, teamIntermediateMapper, teamBalanceSheetMapper, teamProfitStatementMapper);
            balanceScoreSettlementDO.setClassId(balancedScorecard.getClassId());
            balanceScoreSettlementDO.setTeamId(balancedScorecard.getTeamId());
            balanceScoreSettlementDO.setTimeLine(hseRequest.getTimeLine());

            log.info("平衡积分卡算实际值：班级ID为：{},团队ID为：{},方法名称为：{},所传参数为：{}", balancedScorecard.getClassId(), balancedScorecard.getTeamId(), methodName, balanceScoreSettlementDO);
            // 计算实际值
            double actualValueTemp = doSettiementFunc(methodName, balanceScoreSettlementDO);
            if (balancedScorecard.getUnit() != null && "亿".equals(balancedScorecard.getUnit())) {
                actualValueTemp = actualValueTemp / 10000000000d;
            }
            String actualValue = FormatNumberUtils.formatDouble(actualValueTemp, 2);

            //计算标准实际值
            String standardMethodName = balancedScorecard.getBalanceScoreFuncRight();
            Double standardActualValue = doSettiementFunc(standardMethodName, balanceScoreSettlementDO);
            if (balancedScorecard.getUnitRight() != null && "亿".equals(balancedScorecard.getUnitRight())) {
            	standardActualValue = standardActualValue / 10000000000d;
            }
            String actualValueRight = FormatNumberUtils.formatDouble(standardActualValue, 2);

            // 根据实际值计算最终得分
            Float targetValue = Float.parseFloat(balancedScorecard.getTargetValue());
            Integer standardScore = balancedScorecard.getStandardScore();
            Float weightCoefficient = balancedScorecard.getWeightCoefficient();

            Float finalScore = (Float.valueOf(actualValue) / targetValue) * standardScore * weightCoefficient;
            String temp = FormatNumberUtils.formatDouble(finalScore, 2);
            finalScore = Float.parseFloat(temp);
            if (finalScore > (standardScore * weightCoefficient)) {
                finalScore = standardScore * weightCoefficient;
            }
            //如果最终的分小于0，设置最终得分为0
            if (finalScore < 0) {
                finalScore = 0f;
            }
            // 实际值和最终得分入库
            scorecardParam = new BalancedScorecard();
            scorecardParam.setActualValue(Float.valueOf(actualValue) == 0 ? "0" : actualValue);
            scorecardParam.setFinalScore(finalScore);
            scorecardParam.setId(balancedScorecard.getId());
            scorecardParam.setActualValueRight(actualValueRight);

            float checkScore = Float.parseFloat(actualValueRight) / Float.parseFloat(balancedScorecard.getTargetValueRight()) * balancedScorecard.getMatchingDegree() * balancedScorecard.getWeightCoefficientRight();
            checkScore = (float) Math.round(checkScore * 100) / 100;
            scorecardParam.setCheckScore(Float.parseFloat(FormatNumberUtils.formatDouble(checkScore, 2)));

            balancedScorecardMapper.updateTeamBalancedScorecard(scorecardParam);
        }
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 根据方法名调用方法计算实际值
     *
     * @param methodName
     * @param param
     * @return
     */
    private Double doSettiementFunc(String methodName, BalanceScoreSettlementDO param) throws Exception {
        Double result = 0.0;
        try {
            Class<BalanceScoreSettlementFuncServiceImpl> settlementService = BalanceScoreSettlementFuncServiceImpl.class;
            Method method = settlementService.getMethod(methodName, BalanceScoreSettlementDO.class);
            result = (Double) method.invoke(settlementService.newInstance(), param);
        } catch (Exception e) {
            log.error("" + methodName, e);
            throw e;
        }
        return result;
    }

}
