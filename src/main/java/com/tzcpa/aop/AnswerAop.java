package com.tzcpa.aop;

import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.QuestionContractEnum;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.treatment.ClassQuestionDescMapper;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.question.BalanceScoreSettlementService;
import com.tzcpa.service.student.AHseService;
import com.tzcpa.service.student.QuestionService;
import com.tzcpa.service.student.TeamStrategyMapService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName AnswerAop
 * @Description
 * @Author wangzhangju
 * @Date 2019/6/4 9:37
 * @Version 6.0
 **/
@Order(1)
@Aspect
@Component
@Slf4j
public class AnswerAop extends AHseService {

    @Resource
    private QuestionService questionService;

    @Resource
    private TeamStrategyMapService tsmService;


    @Resource
    private BalanceScoreSettlementService balanceScoreSettlementService;

    @Resource
    private ClassQuestionDescMapper classQuestionDescMapper;

    @Resource
    private OSMapper osMapper;

    @Autowired
    private InitTeamIntermediateService initTeamIntermediateService;

    @Transactional(rollbackFor = Exception.class)
    @Around("execution(public * com.tzcpa.service.question.impl.*.checkOS(..))")
    public ResponseResult doAround(ProceedingJoinPoint joinPoint) throws Exception{


        String name = this.getClassMethodName(joinPoint);
        log.info("进入切点："+name);

        //NO.1 执行目标方法
        Object obj = null;
        try {

            obj = joinPoint.proceed();


        //NO.2 获取方法参数
        List<HseRequest> requests = (List<HseRequest>) joinPoint.getArgs()[0];
            //NO.2-1 获取大题id
        HseRequest hseRequest = requests.get(0);
        String thisTimeLine = hseRequest.getTimeLine();
        Integer questionId = hseRequest.getRootId();

        //NO.3 判断是否 月末、年初、年末
       Map<String,Long> thisMonth = classQuestionDescMapper.checkThisMonth(hseRequest.getClassId(),questionId);
        //NO.3-1 获取是否月末 1-是 0-否
        Long endMonth = thisMonth.get("ifendMonth");
        //NO.3-2 获取是否年初 1-是 0-否
        Long startYear = thisMonth.get("ifstartYear");
        //NO.3-2 获取是否年末 1-是 0-否
        Long endYear = thisMonth.get("ifendYear");
        //NO.3-3 获取上月月数
        Long lastMonth = thisMonth.get("lastMonth");
        //NO.3-4 获取上一年份
        Long lastYear = thisMonth.get("lastYear");
        //NO.3-5 获取下一年份
            Long nextYear = thisMonth.get("nextYear");
         //NO.3-6 获取下一月份
            Long nextMonth = thisMonth.get("nextMonth");
         //NO.3-7 获取是否今年的最后一道题
         Long ifLastQuestion =  thisMonth.get("ifLastQuestion");

         if( (lastYear == 2016l && lastMonth == 6l)){
             nextYear = 2017l;
             nextMonth = 1l;
             endYear = 1l;
//             ifLastQuestion = 1l;
         }

         if(lastMonth == 2l && lastYear == 2016l){
             nextMonth = 10l;
             nextYear = 2016l;
             endYear = 0l;
         }


        //NO.4 处理 月末、年初、年末 逻辑
         //NO.4-1 处理年初逻辑
              if(startYear == 1 && Integer.parseInt(String.valueOf(lastYear))==2010){
              String year = hseRequest.getTimeLine().substring(0,4);
              if(ArrayUtils.contains(NormalConstant.STAGR_YEAR,year)) {
                  String sc = NormalConstant.WEY;
                  if(ArrayUtils.contains(NormalConstant.STAGR_YEAR_H6,year)){
                       sc = NormalConstant.H6;
                  }else if(ArrayUtils.contains(NormalConstant.STAGR_YEAR_H8,year)){
                      sc = NormalConstant.H8;
                  }
                  
                  //当前车型战略（为 null的时候说明为wey车型，到了这个时间初始化战略地图战略不需要传）(WTL 2019-06-14)
                  String strategic = osMapper.getStrategic(hseRequest, sc);
                  //处理战略地图
                  tsmService.initTeamStrategyMap(getSCInitParam(hseRequest, osMapper, strategic == null ? NormalConstant.WEY : strategic));
                  //处理战略数据
                  initTeamIntermediateService.initTeamIntermediateOfStrategic(getSCInitParam(hseRequest, osMapper, null));
              }
              //年初处理财务费用
                  Map<String,Object> map = new HashMap<>();
                  map.put("classId", hseRequest.getClassId());
                  map.put("teamId", hseRequest.getTeamId());
                  map.put("year", hseRequest.getTimeLine().substring(0,4));
                  initTeamIntermediateService.updateInitFinancialCost(map);

          }

            //NO.4 月末处理 无题月份 逻辑 -- 向上处理 && 下一年不同 向下到十二月份
            List<String> dateStrs = DateUtil.getMonthBetween(thisTimeLine.substring(0,7),nextYear,nextMonth);
         //NO.4-2 处理月末逻辑
        if(endMonth == 1){

            for(String datestr : dateStrs) {
                //月末 修改最终销售额和最终材料成本
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("classId", hseRequest.getClassId());
                paramMap.put("teamId", hseRequest.getTeamId());
                paramMap.put("timeLine", datestr);
                initTeamIntermediateService.updatePackagingSales(paramMap);

                //处理月末延迟任务
                hseRequest.setTimeLine(datestr);
                ((AHseService) AopContext.currentProxy()).handleDelayedUpdate(osMapper, hseRequest, initTeamIntermediateService);
            }

        }

        //NO.5 处理数据影响
            Integer rootId = hseRequest.getRootId();
          //NO.5-1 处理最终生产折旧影响
            Map<String,Object> map = new HashMap<>();
            map.put("classId",String.valueOf(hseRequest.getClassId()));
            map.put("teamId",String.valueOf(hseRequest.getTeamId()));
            map= QuestionContractEnum.getMap("ID"+rootId,map);
               if(ArrayUtils.contains(NormalConstant.DEPRECIATION_QUESIDS,rootId)){
                 initTeamIntermediateService.updateFinalOperatingCostPlus(map);
               }
            //NO.5-2 维护最终宣传推广费
            if(ArrayUtils.contains(NormalConstant.POPULARIZING_QUESIDS,rootId)){
                initTeamIntermediateService.updateFinalSalesTotal(map);
            }

            //NO.5-3 维护 最终资产减值损失-坏账
            if(ArrayUtils.contains(NormalConstant.LOSS_QUESIDS,rootId)){
                initTeamIntermediateService.updateFinalAssetsImpairmentLossTotal(map);
            }

            //NO.6 月末维护报表
            if(endMonth ==1) {
                Long monthNextYear = Long.valueOf(dateStrs.get(dateStrs.size()-1).substring(0,4));
                Long mongthNextMonth =  Long.valueOf(dateStrs.get(dateStrs.size()-1).substring(5,7));
                questionService.setMonthlyProfit(hseRequest.getClassId(), hseRequest.getTeamId(), thisTimeLine, monthNextYear, mongthNextMonth,dateStrs);
            }

            //NO.4-4 处理年末逻辑
            if(endYear ==1 ){
                //维护年度利润
                questionService.setYearlyProfit(hseRequest.getClassId(),hseRequest.getTeamId(),DateUtil.toDate("yyyy-MM-dd",thisTimeLine));

            }
            //NO.4-5 年末最后一道题处理积分卡 初始化下一年数据 调用月末方法
            if(ifLastQuestion==1l){
                //平衡积分卡 实际值 最终得分 计算及入库
                hseRequest.setTimeLine(thisTimeLine);
                balanceScoreSettlementService.BalanceScoreSettlement(hseRequest);
                // 年末最后一道题 处理下一年数据
                questionService.setNextYearDataAop(thisTimeLine,nextYear,nextMonth,hseRequest);

            }

        } catch (Throwable e) {
//            log.error("执行方法异常："+name,e);
            throw new Exception(e);
        }
        if(null == obj){
            return ResponseResult.fail();
        }else {
            return (ResponseResult) obj;
        }
    }


    public String getClassMethodName(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Object target = joinPoint.getTarget();
        Signature sig = joinPoint.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        return target.getClass().getName() +"--"+joinPoint.getSignature().getName();
    }

    @Override
    public ResponseResult checkOS(List<HseRequest> hrList) {
        return null;
    }
}
