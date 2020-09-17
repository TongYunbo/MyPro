package com.tzcpa.service.question.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tzcpa.constant.VNCConstant;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.VariableConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.mapper.treatment.TeamBalanceSheetMapper;
import com.tzcpa.mapper.treatment.TeamProfitStatementMapper;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.student.TeamStrategyMapService;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>Description: 存货周转率</p>
 *
 * @author wangbj 存货周转率
 * @date 2019年5月22日
 */
@Slf4j
@SuppressWarnings("rawtypes")
@Service("stockTurnoverService")
public class StockTurnoverServiceImpl extends AHseService {

    @Resource
    private OSMapper osMapper;

    @Resource
    private TeamBalanceSheetMapper teamBalanceSheetMapper;

    @Resource
    private TeamProfitStatementMapper teamProfitStatementMapper;

    //@处理注释
//    @Autowired
//    private InitTeamIntermediateService teamIntermediateService;

    @Resource
    private QuestionService questionService;

    @Resource
    private TeamStrategyMapService tsmService;

	@Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult checkOS(List<HseRequest> hrList) throws Exception {

	    try {
            HseRequest hseRequest = hrList.get(0);

            AnswerScoreDO answerScoreDO = new AnswerScoreDO(hseRequest);
            if (!answerScoreDO.isQualified()) {
                return ResponseResult.fail("参数不全");
            }

            // 当前年份
            // String currYear = hseRequest.getTimeLine().substring(0, 4);
            // 查询参数
            Map<String, Object> param = new HashMap<>();
            // param.put("year", Integer.parseInt(currYear) - 1);
            param.put("year", 2012);
            param.put("classId", hseRequest.getClassId());
            param.put("teamId", hseRequest.getTeamId());
            // 营业成本
//            Long totalOperatingCost = teamProfitStatementMapper.getOperatingCost(param);
            // 存货
//            Long inventory2012 = teamBalanceSheetMapper.getInventory(param);
            // param.put("year", Integer.parseInt(currYear) - 2);
//            param.put("year", 2011);
//            Long inventory2011 = teamBalanceSheetMapper.getInventory(param);
            Double chzzts = teamProfitStatementMapper.getCHZZTS(hseRequest.getClassId(), hseRequest.getTeamId());
            log.info("需要计算用的值为==存货周转天数：{}", chzzts);
            // 计算变量
            Double CHZZL = 360 / chzzts;


            List<Map<String, Object>> checkRes = osMapper.checkRes(answerScoreDO);
            // CHZZL>33 正确答案为B CHZZL≤33 正确答案为E
            param = new HashMap<>();
            param.put("vCode", VNCConstant.CHZZLS);
            String stockTurnoverRate = osMapper.selectTeamRdv(param);
            String answer = hseRequest.getAnswer().get(0);
            if (checkRes.size() != 0) {
                if (CHZZL > Double.parseDouble(stockTurnoverRate)) {
                    if ("B".equals(answer)) {
                        ((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hseRequest, checkRes);
                    }
                } else {
                    if ("E".equals(answer)) {
                        ((AHseService) AopContext.currentProxy()).handleAddScore(osMapper, hseRequest, checkRes);
                    }
                }
            }

            // 生成viList
            List<String[]> viList = new ArrayList<>();
            viList.add(new String[]{
                    VariableConstant.CHZZLB, NormalConstant.SUBTRACT_EFFECT, CHZZL.toString(), "gtStockTurnover248", null
            });
            viList.add(new String[]{
                    VariableConstant.CHZZLE, NormalConstant.SUBTRACT_EFFECT, CHZZL.toString(), "ltStockTurnover248", null
            });

            //重新设置答案
            answerScoreDO.setAnswer("'" + JsonUtil.listToJson(hseRequest.getAnswer()) + "'");
            answerScoreDO.setIsSelect(1);
            // 增加延迟任务及执行立即执行任务
            ((AHseService) AopContext.currentProxy()).addDelayedUpdateTask(osMapper, answerScoreDO, viList);
            ((AHseService) AopContext.currentProxy()).handleFinanceImpact(osMapper, answerScoreDO, hseRequest, viList);

            return ResponseResult.success();
        }catch (Exception e){
	        throw e;
        }
    }
	
}
