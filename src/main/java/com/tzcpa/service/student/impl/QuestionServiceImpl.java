package com.tzcpa.service.student.impl;

import com.alibaba.fastjson.JSON;
import com.tzcpa.config.netty.BufferTimeTask;
import com.tzcpa.config.netty.InformationOperateMapMatching;
import com.tzcpa.config.netty.TimerAndTaskUsage;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.QuestionContractEnum;
import com.tzcpa.constant.VNCConstant;
import com.tzcpa.controller.result.CodeMessage;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.question.TeamBiddingMapper;
import com.tzcpa.mapper.student.StuAnswerMapper;
import com.tzcpa.mapper.student.TeamAnnualWorthGatherMapper;
import com.tzcpa.mapper.student.TeamMonthlyProfitStatementMapper;
import com.tzcpa.mapper.teacher.ClassMapper;
import com.tzcpa.mapper.teacher.TeamMapper;
import com.tzcpa.mapper.treatment.*;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.model.student.StuAnswerDO;
import com.tzcpa.model.student.TeamAnnualWorthGather;
import com.tzcpa.model.student.TeamMonthlyProfitStatementDO;
import com.tzcpa.model.teacher.*;
import com.tzcpa.model.treatment.*;
import com.tzcpa.service.question.BalanceScoreSettlementService;
import com.tzcpa.service.question.QuetionCommonService;
import com.tzcpa.service.student.*;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.*;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * @ClassName QuestionServiceImpl
 * @Description 班级题库初始化实现
 * @Author hanxf
 * @Date 2019/5/6 11:25
 * @Version 1.0
 **/
@Service("questionService")
@Order(2)
public class QuestionServiceImpl extends AdaptationHseService implements QuestionService {

    private Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Resource
    private ClassQuestionDescMapper classQuestionDescMapper;
    @Resource
    private StuAnswerMapper stuAnswerMapper;

    @Resource
    private ClassMapper classMapper;
    @Resource
    private ClassQuestionOptionMapper classQuestionOptionMapper;
    @Resource
    private TeamMonthlyProfitStatementMapper teamMonthlyProfitStatementMapper;
    @Resource
    private TeamProfitStatementMapper teamProfitStatementMapper;
    @Resource
    private TeamBalanceSheetMapper teamBalanceSheetMapper;

    @Resource
    private TeamAnnualWorthGatherMapper teamAnnualWorthGatherMapper;

    @Resource
    private TeamBiddingMapper teamBiddingMapper;
    
    @Resource
    private OSMapper osMapper;
    
    @Resource
    private IPickUpCarSettlementService pcService;

    @Resource
    private TeamStrategyMapService tsmService;

    @Resource
    private InitTeamIntermediateService initTeamIntermediateService;

    @Resource
    private BalanceScoreSettlementService balanceScoreSettlementService;
    
    @Resource
    private IBalanceSheetJSService bsService;
    
    @Resource
    private IQuestionProfitAnalysisService paService;

    @Resource
    private QuetionCommonService quetionCommonService;

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private  TeamIntermediateMapper teamIntermediateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object answerAndGetQuestion(QuestionAnswerDTO questionAnswerDTO) throws Exception{

        Team team = UserSessionUtil.getTeam();
        int initOrNot = questionAnswerDTO.getInitOrNot();
        try {
          //NO.1 执行答题操作 处理答题后的逻辑
        if(initOrNot<1){
            ResponseResult responseResult = quetionCommonService.answer(questionAnswerDTO,team.getId(),team.getClassId(),team.getAccount());
            CodeMessage codeMessage = responseResult.getMeta();
            if("-1".equals(codeMessage.getCode())){
                 throw new Exception("算分、评估影响失败");
            }
        }

          //NO.2-1 获取当前月份
            Date currentMonth = classQuestionDescMapper.getLocalMonth(team.getClassId(),1);
          //NO.2-2 查询当前月份未答问题
          QuestionAnswerDTO questionAnswerDTO1 = classQuestionDescMapper.getQuestions(team.getClassId(), team.getId(),currentMonth);
          //NO.2-3 维护班级答题状态-- 所有的学生都答完题 标识为已答完
          if(null == questionAnswerDTO1){

             //确定学员是否被禁用
              Team team1 = teamMapper.selectTeam(team);
              if(team1 == null || team1.getIsDel() == null || team1.getIsDel() > 0){
                  return "账号被禁用，不可答题！";
              }

             //返回当月经营已完成或经营已完成
              int teamUnAnswered = classQuestionDescMapper.getUnAnswerd(team.getClassId(),team.getId(),null);
              if(teamUnAnswered == 0){
                  return "感谢您的参与，经营已完成！";
              }
              return "当月经营已完成！";
          }
        // hanxf  空的option不显示
        questionAnswerDTO1.getThisChildrenItem().forEach(secondQuestionDTO->{
            secondQuestionDTO.getThisItemOptions().forEach(map->{
                if(map.get("opt") == null){
                    secondQuestionDTO.setThisItemOptions(new ArrayList<>());
                }
            });
        });

        //NO.3 处理通知类型题目、自动跳题题目
        questionAnswerDTO1 = this.noticeOfDisposition(questionAnswerDTO1,team);

        return questionAnswerDTO1;
        } catch (Exception e){
            logger.error("计算影响&答题失败",e);
            throw e;
        }
    }

    public QuestionAnswerDTO noticeOfDisposition(QuestionAnswerDTO questionAnswerDTO1,Team team){
        int thisItemId = questionAnswerDTO1.getThisItemId();
        //NO.3-1 处理questionId为 407 的并购结果告知 上题questionId 为 154

        if(thisItemId == 407 ){
            StuAnswerDO stuAnswer = stuAnswerMapper.getAnswer(team.getClassId(),team.getId(),133);
            if(stuAnswer!= null && stuAnswer.getAnswer().indexOf("C") > -1){
                questionAnswerDTO1.getThisItemOptions().remove(0);
                questionAnswerDTO1.getThisItemOptions().remove(0);
            }else if(stuAnswer!= null && stuAnswer.getAnswer().indexOf("B") > -1){
                questionAnswerDTO1.getThisItemOptions().remove(0);
                questionAnswerDTO1.getThisItemOptions().remove(1);
            }else if(stuAnswer!= null && stuAnswer.getAnswer().indexOf("A") > -1){
                questionAnswerDTO1.getThisItemOptions().remove(1);
                questionAnswerDTO1.getThisItemOptions().remove(1);
            }
        }
        //NO.3-3 处理questionId为 264 的背景补充提示罚单 上题questionId 为 35 --小题 41 42
        //   如果没有提示 自动跳过该题
        else if(thisItemId == 264){
            //
            StuAnswerDO stuAnswer5 = stuAnswerMapper.getAnswer(team.getClassId(),team.getId(),40);
            StuAnswerDO stuAnswer6 = stuAnswerMapper.getAnswer(team.getClassId(),team.getId(),41);
            //设置罚款金额、减少的销量
            Map<String,String> saleMap = teamIntermediateMapper.getH6Sale(team.getClassId(),team.getId());
            if(null != questionAnswerDTO1.getThisItemOptions().get(0)){
                String str = (String) questionAnswerDTO1.getThisItemOptions().get(0).get("optCon");
                str= str.replaceAll("xx",  saleMap.get("fine")+"");
                questionAnswerDTO1.getThisItemOptions().get(0).put("optCon",str);
            }
            if(null != questionAnswerDTO1.getThisItemOptions().get(1)) {
                String str = (String) questionAnswerDTO1.getThisItemOptions().get(1).get("optCon");
                str = str.replaceAll("xx",  saleMap.get("volume")+"");
                questionAnswerDTO1.getThisItemOptions().get(1).put("optCon",str);
            }
            if(stuAnswer5 != null && stuAnswer5.getAnswer().indexOf("C")>-1){
                questionAnswerDTO1.getThisItemOptions().remove(0);
            }
            if(stuAnswer6 != null && stuAnswer6.getAnswer().indexOf("C")>-1){
               if(questionAnswerDTO1.getThisItemOptions().size()>1) {
                   questionAnswerDTO1.getThisItemOptions().remove(1);
               }else{
                   questionAnswerDTO1.getThisItemOptions().remove(0);
               }
            }
            //处理没有提示 自动跳过该题
            if(questionAnswerDTO1.getThisItemOptions().size()==0){
                StuAnswerDO stuAnswerDO = new StuAnswerDO();
                stuAnswerDO.setClassId(team.getClassId());
                stuAnswerDO.setTeamId(team.getId());
                stuAnswerDO.setQuestionId(thisItemId);
                stuAnswerDO.setRootId(thisItemId);
                stuAnswerDO.setAnswer("["+'"'+"无提示"+'"'+"]");
                stuAnswerDO.setYear(Integer.valueOf(questionAnswerDTO1.getTimeLine().substring(0,4)));
                stuAnswerDO.setMonth(Integer.valueOf(questionAnswerDTO1.getTimeLine().substring(5,7)));
                stuAnswerMapper.insertStuAnswer(stuAnswerDO);
                Date currentMonth = classQuestionDescMapper.getLocalMonth(team.getClassId(),1);
                //NO.2-2 查询当前月份未答问题
                return classQuestionDescMapper.getQuestions(team.getClassId(), team.getId(),currentMonth);
            }
        }
        //NO.3-4 处理中标结果通知
        else if(thisItemId == 275){
         List<Map<String,Object>> bidResult = teamBiddingMapper.getBidResult(team.getClassId());
            questionAnswerDTO1.setThisItemOptions(bidResult);
        }
        //NO.3-5 处理皮卡销售预算考核实际值
        else if(thisItemId == 251){
        	pcService.handleYSActualValue(questionAnswerDTO1, team);
        }
        //NO.3-6 处理盈利能力分析我公司数据
        else if(thisItemId == 232){
        	paService.handleQuestionData(questionAnswerDTO1, team);
        }
        return questionAnswerDTO1;
    }

    @Override
    public List<SandTableVO> getSandTable(Integer classId , Integer teamId) {
        if(null == classId) {
            Team team = UserSessionUtil.getTeam();
            classId = team.getClassId();
            teamId = team.getId();
        }

        List<SandTableVO> sandTableVOS = classQuestionDescMapper.getSandTable(classId,teamId);
        for(SandTableVO sandTableVO : sandTableVOS){
            List<Map<String,Object>> buttonList = sandTableVO.getButtonList();
            for(Map<String,Object> map : buttonList){
                if( String.valueOf(map.get("questionType")).equals("9")){
                   int quesUnAnswerd = classQuestionDescMapper.getQuesUnAnswerd(classId,teamId);
                   if(quesUnAnswerd==0){
                       map.put("isScore",1);
                   }
                }else if( String.valueOf(map.get("questionType")).equals("16")){
                    int quesUnAnswerd = classQuestionDescMapper.getQues16UnAnswerd(classId,teamId);
                    if(quesUnAnswerd==0){
                        map.put("isScore",1);
                    }
                }
            }
        }
        return sandTableVOS;
    }

    @Override
    public QuestionAnswerDTO getAnswerdQuestion(QuestionAnswerdDTO questionAnswerdDTO) throws Exception{

        QuestionAnswerDTO questionAnswerDTO =
                classQuestionDescMapper.getResponsed(questionAnswerdDTO.getClassId(),questionAnswerdDTO.getTeamId(),questionAnswerdDTO.getThisItemId());
        Team team = new Team();
        team.setId(questionAnswerdDTO.getTeamId());
        team.setClassId(questionAnswerdDTO.getClassId());
        questionAnswerDTO = this.noticeOfDisposition(questionAnswerDTO,team);
        return questionAnswerDTO;
    }

    @Override
    public CountDownDO getCountDownTime(Integer classId, Integer teamId) {
        Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
        CountDownDO countDownDO = new CountDownDO();
        if(DateUtil.dateToStr("yyyy-MM-dd",currentMonth).equals("2016-10-01")){
            countDownDO = classQuestionDescMapper.getCountDownTimeTeam(classId,teamId,currentMonth);
        }else{
            countDownDO = classQuestionDescMapper.getCountDownTime(classId,currentMonth);
        }
        if(countDownDO == null){
            countDownDO = new CountDownDO();
        }else{
            countDownDO.setTimeLine(DateUtil.dateToStr("yyyy-MM-dd",currentMonth));
        }
        return countDownDO;
    }

    @Override
    public List<ClassQuestionOptionDO> getQuestionOptions(List<Integer> questionIds) {
        Team team = UserSessionUtil.getTeam();
        return classQuestionOptionMapper.getQuestionOptions(questionIds,team.getClassId());
    }

    @Override
    public int insertQuestionMonth(Integer classId,String account) throws Exception{

        try {
            int unaAnswered = classQuestionDescMapper.getUnAnswerd(classId, null, null);
            if (unaAnswered == 0) {
                Clazz clazz = new Clazz();
                clazz.setId(classId);
                clazz.setAnswerState(4);
                clazz.setModifyTime(new Date());
                clazz.setModifyPerson(account);
                clazz.setTimeRemain(0L);
                classMapper.updateClassNameById(clazz);
            }
            return classQuestionOptionMapper.insertQuestionMonth(classId, account);
        }catch (Exception e){
            logger.error("保存月份失敗",e);
            throw e;
        }
    }

    @Override
    public String getAccountByClassId(Integer classId) {
        return classMapper.getAccountByClassId(classId);
    }

    @Override
    public int updateClassStatus(int classId, int answerStatus, String userName,Long remainTime,Long bufferTime) throws Exception{
        Clazz clazz = new Clazz();
        clazz.setId(classId);
        if(0 != answerStatus) {
            clazz.setAnswerState(answerStatus);
        }
        clazz.setModifyTime(new Date());
        clazz.setModifyPerson(userName);
        if(null != remainTime){
            clazz.setTimeRemain(remainTime);
        }
        if(null != bufferTime) {
            clazz.setBufferRemain(bufferTime);
        }
        return classMapper.updateClassNameById(clazz);
    }

    @Override
    public Clazz getClazz(int classId) {
        return classMapper.getClassByid(classId);
    }

    /**
     * @author:     wangzhangju
     * @date:       2019/5/27 9:51
     * @param:      null
     * @return:
     * @exception:
     * @description: 月份倒计时结束/老师点击下一月 自动维护答案
     * @step:
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoAnswer(UserInfo userInfo) throws Exception{
        Date currentMonth = classQuestionDescMapper.getLocalMonth(userInfo.getClassId(),1);
            quetionCommonService.autoAnswer(userInfo, currentMonth);
    }

    List<HseRequest> deepCopyHse( List<HseRequest> hseRequestList) throws IOException, ClassNotFoundException {
        List<HseRequest> hseRequests  = BeanUtil.deepCopy(hseRequestList);
        return hseRequests;
    }

    /**
     * @author:     wangzhangju
     * @date:       2019/7/5 15:00
     * @param:      null
     * @return:
     * @exception:
     * @description: 10年最后一道题添加研发费用
     * @step:
     */
    public void addDevExpand(List<HseRequest> hrList) throws Exception{
        String temp = osMapper.selectConfVariable(VNCConstant.YFFY2010);
        Double developmentCost = Double.parseDouble(temp) / 1000000d;
        HseRequest hseRequest = hrList.get(0);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("classId", hseRequest.getClassId());
        map.put("teamId", hseRequest.getTeamId());
        map.put("year", hseRequest.getTimeLine().substring(0, 4));
        map.put("inDevelopmentCost", developmentCost.toString());
        initTeamIntermediateService.updateInitInDevelopmentCost(map);
    }

    /**
     * @author:     wangzhangju
     * @date:       2019/6/21 20:14
     * @param:      null
     * @return:
     * @exception:
     * @description: 自动维护答案 处理不需要维护随机答案的大题逻辑
     * @step:
     */
    ResponseResult treatmentEffect( HseRequest hseRequest) throws Exception {
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
                handleDelayedUpdate(osMapper, hseRequest, initTeamIntermediateService);
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
            this.setMonthlyProfit(hseRequest.getClassId(), hseRequest.getTeamId(), thisTimeLine, monthNextYear, mongthNextMonth,dateStrs);
        }

        //NO.4-4 处理年末逻辑
        if(endYear ==1 ){
            //维护年度利润
            this.setYearlyProfit(hseRequest.getClassId(),hseRequest.getTeamId(),DateUtil.toDate("yyyy-MM-dd",thisTimeLine));

        }
        //NO.4-5 年末最后一道题处理积分卡 初始化下一年数据 调用月末方法
        if(ifLastQuestion==1){
            //平衡积分卡 实际值 最终得分 计算及入库
            hseRequest.setTimeLine(thisTimeLine);
            balanceScoreSettlementService.BalanceScoreSettlement(hseRequest);
            // 年末最后一道题 处理下一年数据
            this.setNextYearData(thisTimeLine,nextYear,nextMonth,hseRequest);

        }

        return ResponseResult.success();
    }

    void setNextYearData(String thisTimeLine ,Long nextYear ,Long nextMonth,HseRequest hseRequest) throws Exception {

        List<String> dateStrs = DateUtil.getMonthBetween(nextYear+"-01",nextYear,nextMonth);

        //时间线设置为下一年的一月一号
        String nextMonthStr = nextMonth.toString();
        if(nextMonthStr.length() == 1){
            nextMonthStr = "0"+nextMonthStr;
        }
        hseRequest.setTimeLine(nextYear+"-"+nextMonthStr+"-01");
        //初始化年初数据
        String year = nextYear.toString();
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
        map.put("year", year);
        initTeamIntermediateService.updateInitFinancialCost(map);


        for(String datestr : dateStrs) {
            //月末 修改最终销售额和最终材料成本
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("classId", hseRequest.getClassId());
            paramMap.put("teamId", hseRequest.getTeamId());
            paramMap.put("timeLine", datestr);
            initTeamIntermediateService.updatePackagingSales(paramMap);

            //处理月末延迟任务
            hseRequest.setTimeLine(datestr);
            handleDelayedUpdate(osMapper, hseRequest, initTeamIntermediateService);

        }

        if(!dateStrs.isEmpty()){
            //维护月度利润
            Long monthNextYear = Long.valueOf(dateStrs.get(dateStrs.size()-1).substring(0,4));
            Long mongthNextMonth =  Long.valueOf(dateStrs.get(dateStrs.size()-1).substring(5,7));
            this.setMonthlyProfit(hseRequest.getClassId(), hseRequest.getTeamId(), thisTimeLine, monthNextYear, mongthNextMonth,dateStrs);
        }

    }



    List<StuAnswerDO> getAutoAnswer(AutoAnswerDO autoAnswerDO){
        List<StuAnswerDO> stuAnswerDOS = new ArrayList<>() ;
        //NO.1 维护大题固定答案 或 自动答案
        if(autoAnswerDO.getChildren().get(0).getRootId().equals(274)){
        	stuAnswerDOS.add(this.setStuAnswerTH(autoAnswerDO,null));
        }else{
        	stuAnswerDOS.add(this.setStuAnswer(autoAnswerDO,"["+'"' +" "+'"'+"]"));
            //NO.2 维护二级或三级小题自动答案
              List<AutoAnswerDO2> secondQuestions = autoAnswerDO.getChildren();
              //四条产品车型
              List<AutoAnswerDO2> secondQuestions4 = new ArrayList<AutoAnswerDO2>();
              //处理2018/01/01产品管理结构调整自动维护答案，否则按照正常情况运行。
              if(secondQuestions.get(0).getRootId().equals(188)){
    	        	  for (AutoAnswerDO2 autoAnswerDO2 : secondQuestions) {
    		            	if(autoAnswerDO2.getIsRandom()!=1){
    		            		secondQuestions4.add(autoAnswerDO2);
    		            	}
    				  }
    	              for(AutoAnswerDO2 autoAnswerDO2:secondQuestions4){
    	        		  stuAnswerDOS.add(this.setStuAnswerT(autoAnswerDO2,null));
    	        	  }
        	  }else{
    	    		 //处理风险识别答案问题
        			 if(secondQuestions.get(0).getRootId().equals(35) || secondQuestions.get(0).getRootId().equals(93)){
      	        	  secondQuestions = autoAnswerDO.getChildren().subList(0, 6);
      	             }
      	          for(AutoAnswerDO2 autoAnswerDO2:secondQuestions){
                          //获取三级小题
                          List<BaseAutoAnswer> thirdQuestions = autoAnswerDO2.getChildren();
                          //NO.2-1 维护二级需要自动维护答案的小题
                          if(autoAnswerDO2.getQuestionId() != null && autoAnswerDO2.getIsRandom() ==1 ){
                              stuAnswerDOS.add(this.setStuAnswer(autoAnswerDO2,null));
                          }
                          //NO.2-2 维护三级需要维护答案的小题
                          for(BaseAutoAnswer baseAutoAnswer : thirdQuestions){

                              if(baseAutoAnswer.getQuestionId() != null && baseAutoAnswer.getIsRandom() ==1 ) {
                                  stuAnswerDOS.add(this.setStuAnswer(baseAutoAnswer,null));
                              }
                          }

                    }
        		}
        	
        }

        return stuAnswerDOS;
    };

    StuAnswerDO setStuAnswer(BaseAutoAnswer baseAutoAnswer,String themeName){
        StuAnswerDO stuAnswerDO = new StuAnswerDO();
        stuAnswerDO.setClassId(baseAutoAnswer.getClassId());
        stuAnswerDO.setTeamId(baseAutoAnswer.getTeamId());
        stuAnswerDO.setQuestionId(baseAutoAnswer.getQuestionId());
        stuAnswerDO.setRootId(baseAutoAnswer.getRootId());
        stuAnswerDO.setRoleId(baseAutoAnswer.getRoleId());
        stuAnswerDO.setWeight(baseAutoAnswer.getWeight());
        stuAnswerDO.setIsStrategic(baseAutoAnswer.getIsStrategic());
        stuAnswerDO.setTimeLine(baseAutoAnswer.getTimeLine());
        if(null != themeName) {
            if (baseAutoAnswer.getIsRandom() == 1) {
                stuAnswerDO.setAnswer(QuestionUtil.getRandomAnswer(baseAutoAnswer.getSeveralOptions(), baseAutoAnswer.getChooseFew()));
            } else {
                if(baseAutoAnswer.getEventName().indexOf("研发预算")>-1){
                    stuAnswerDO.setAnswer("["+'"' +"0"+'"'+"]");
                }else {
                    stuAnswerDO.setAnswer(themeName);
                }
            }
        }else {
            stuAnswerDO.setAnswer(QuestionUtil.getRandomAnswer(baseAutoAnswer.getSeveralOptions(), baseAutoAnswer.getChooseFew()));
        }
        stuAnswerDO.setYear(baseAutoAnswer.getYear());
        stuAnswerDO.setMonth(baseAutoAnswer.getMonth());
        stuAnswerDO.setEventCode(baseAutoAnswer.getEventCode());
        return stuAnswerDO;
    }
    //答案赋值（拼接答案）
    StuAnswerDO setStuAnswerT(BaseAutoAnswer baseAutoAnswer,String themeName){
        StuAnswerDO stuAnswerDO = new StuAnswerDO();
        stuAnswerDO.setClassId(baseAutoAnswer.getClassId());
        stuAnswerDO.setTeamId(baseAutoAnswer.getTeamId());
        stuAnswerDO.setQuestionId(baseAutoAnswer.getQuestionId());
        stuAnswerDO.setRootId(baseAutoAnswer.getRootId());
        stuAnswerDO.setRoleId(baseAutoAnswer.getRoleId());
        stuAnswerDO.setWeight(baseAutoAnswer.getWeight());
        stuAnswerDO.setIsStrategic(baseAutoAnswer.getIsStrategic());
        stuAnswerDO.setTimeLine(baseAutoAnswer.getTimeLine());
        stuAnswerDO.setAnswer(QuestionUtil.getRandomAnswerT(4, 3));
        stuAnswerDO.setYear(baseAutoAnswer.getYear());
        stuAnswerDO.setMonth(baseAutoAnswer.getMonth());
        stuAnswerDO.setEventCode(baseAutoAnswer.getEventCode());
        return stuAnswerDO;
    }
    //答案赋值（投标竞争）
    StuAnswerDO setStuAnswerTH(BaseAutoAnswer baseAutoAnswer,String themeName){
        StuAnswerDO stuAnswerDO = new StuAnswerDO();
        stuAnswerDO.setClassId(baseAutoAnswer.getClassId());
        stuAnswerDO.setTeamId(baseAutoAnswer.getTeamId());
        stuAnswerDO.setQuestionId(baseAutoAnswer.getQuestionId());
        stuAnswerDO.setRootId(baseAutoAnswer.getRootId());
        stuAnswerDO.setRoleId(baseAutoAnswer.getRoleId());
        stuAnswerDO.setWeight(baseAutoAnswer.getWeight());
        stuAnswerDO.setIsStrategic(baseAutoAnswer.getIsStrategic());
        stuAnswerDO.setTimeLine(baseAutoAnswer.getTimeLine());
        stuAnswerDO.setAnswer("[\"S60\",\"0\",\"0\",\"0\"]");
        stuAnswerDO.setYear(baseAutoAnswer.getYear());
        stuAnswerDO.setMonth(baseAutoAnswer.getMonth());
        stuAnswerDO.setEventCode(baseAutoAnswer.getEventCode());
        return stuAnswerDO;
    }

        /**
         * @author:     wangzhangju
         * @date:       2019/6/3 17:31
         * @param:      classId-班级id teamId-团队id currentDate-当前答题年月
         * @return:
         * @exception:
         * @description: 计算每个团队的月度利润
         * @step:
         */
    @Override
    @Transactional
    public  void setMonthlyProfit(Integer classId,Integer teamId,String currentDate,Long nextYear,Long nextMonth,List<String> dateStrs) throws Exception{
        logger.info("设置月度利润表数据 初始化参数 classId={}  teamId={}  currentDate={} nextYear={} nextMonth={}",classId,teamId,currentDate,nextYear,nextMonth);
       try {
           List<TeamMonthlyProfitStatementDO> teamMonthlyProfitStatementDOs = teamMonthlyProfitStatementMapper.selectFromIntermediate
                   (classId, teamId, Integer.parseInt(currentDate.substring(0, 4)),
                           Integer.parseInt(currentDate.substring(5, 7)), nextYear, nextMonth);
           logger.info("设置月度利润表数据 teamMonthlyProfitStatementDOs={}", JSON.toJSONString(teamMonthlyProfitStatementDOs));
           teamMonthlyProfitStatementMapper.insertBanchMonthlyProfitStatement(teamMonthlyProfitStatementDOs);

           //执行影响月度利润数据的影响
           ((AHseService) AopContext.currentProxy()).handleDelayedUpdate(osMapper,
                   new HseRequest(classId, teamId, currentDate), NormalConstant.DELAYED_TYPE_CWFY.toString(), bsService, dateStrs);
       }catch (Exception e){
           throw  e;
       }
    }

    /**
     * @author:     wangzhangju
     * @date:       2019/6/3 17:38
     * @param:      classId-班级id teamId-团队id currentDate-当前答题年月
     * @return:
     * @throws Exception 
     * @exception:
     * @description: 设置年度利润数据
     * @step:
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setYearlyProfit(Integer classId,Integer teamId,Date currentDate) throws Exception{
        logger.info("设置年度利润数据 入口参数 classId={} teamId={} currentDate={}",classId,teamId,currentDate);
        try {
            Integer lastYear = classQuestionDescMapper.getLastYear(currentDate, classId);
            logger.info("设置年度利润数据 lastYear={}", lastYear);
            //NO.5-2 如果题目的下一月份跨到下一年 维护当前团队的年度利润
            List<TeamProfitStatementDO> doList = teamProfitStatementMapper.getDataFromMonthly(classId, teamId, currentDate, lastYear);
            logger.info("设置年度利润数据 doList={}", JSON.toJSONString(doList));
            if (doList.size() > 0) {
                teamProfitStatementMapper.insertForBatch(doList);
            }

            //NO.6 计算当前团队的 资产负债
            //NO.6-1 获取资产负债数据
            List<TeamBalanceSheetDO> listBalance = teamBalanceSheetMapper.getBalanceSheetSource(classId, teamId, lastYear, currentDate);
            logger.info("设置资产负债表的数据 listBalance={}", JSON.toJSONString(listBalance));
            //NO.6-2 保存资产负债数据
            for (TeamBalanceSheetDO teamBalanceSheetDO : listBalance) {
                teamBalanceSheetMapper.insert(teamBalanceSheetDO);
            }

            // 执行影响资产负债表数据的影响
//		handleDelayedUpdate(osMapper,
//				new HseRequest(classId, teamId, DateUtil.dateToStr(DateUtil.FORMART_5, currentDate)),
//				NormalConstant.DELAYED_TYPE_ZCFZ_TABLE, bsService);

            //NO.7 获取当前团队的 年度分析数据
            List<TeamAnnualWorthGather> teamAnnualWorthGathers = teamAnnualWorthGatherMapper.getAnnual(classId, teamId, lastYear, currentDate);
            logger.info("设置年度分析表的数据 teamAnnualWorthGathers={}", JSON.toJSONString(teamAnnualWorthGathers));
            for (TeamAnnualWorthGather teamAnnualWorthGather : teamAnnualWorthGathers) {
                teamAnnualWorthGatherMapper.insert(teamAnnualWorthGather);
            }
        }catch (Exception e){
            throw e;
        }
	  }

    @Override
    public int insertH6Answer(QuestionAnswerDTO questionAnswerDTO,UserInfo userInfo) {
        StuAnswerDO stuAnswerDO = new StuAnswerDO();
        stuAnswerDO.setRootId(questionAnswerDTO.getThisItemId());
        stuAnswerDO.setQuestionId(questionAnswerDTO.getThisItemId());
        stuAnswerDO.setClassId(userInfo.getClassId());
        stuAnswerDO.setTeamId(userInfo.getTeamId());
        stuAnswerDO.setYear(Integer.valueOf(questionAnswerDTO.getTimeLine().substring(0,4)));
        stuAnswerDO.setMonth(Integer.valueOf(questionAnswerDTO.getTimeLine().substring(5,7)));
        stuAnswerDO.setAnswer(JsonUtil.listToJson(questionAnswerDTO.getThisItemAnswer()));
        List<StuAnswerDO> stuAnswerDOList = new ArrayList<>();
        stuAnswerDOList.add(stuAnswerDO);
        return stuAnswerMapper.insertStuAnswerBatch(stuAnswerDOList);
    }

    @Override
    public int getUnAnswerd(int classId) {
        return classQuestionDescMapper.getUnAnswerd(classId,null,null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void nextMonth(UserInfo userInfo, ChannelHandlerContext ctx ) throws Exception {
        //NO.1 获取老师管道 及保存信息
        try {
            //Cmmon
            Clazz clazzExit = classMapper.getClassByid(userInfo.getClassId());
            //NO.1-1 老师点击下一月 检验当前月份的题目是否答完

            InformationOperateMapMatching teacherMatching =
                    this.getMatching(userInfo.getTable(),userInfo.getId());

            CountDownDO countDownThis = this.getCountDownTime(userInfo.getClassId(), userInfo.getTeamId());
            //状态置为暂停

            //NO.2 校验学生是否都答完题
            Date currentMonth = classQuestionDescMapper.getLocalMonth(userInfo.getClassId(),1);
                    int unaAnswered = classQuestionDescMapper.getUnAnswerd(Integer.parseInt(userInfo.getTable()), null, currentMonth);
                    if (unaAnswered > 0) {
                        countDownThis.setWhetherStart(clazzExit.getAnswerState());
                        if (null != teacherMatching) {
                            countDownThis.setRecidueMillSecond(teacherMatching.getTimerAndTaskUsage().getMillTime());
                            countDownThis.setRecidueBufferTime(teacherMatching.getBufferTimeTask().getMillTime());
                            countDownThis.setMothMillisecond(countDownThis.getMothMillisecond());

                            userInfo.setCountDownDO(countDownThis);
                            userInfo.setWhetherEnd(false);
                            teacherMatching.sead(userInfo);
                            return;
                        }
                    }

            //停止倒计时
            if(null != teacherMatching.getTimerAndTaskUsage()) {
                teacherMatching.getTimerAndTaskUsage().stopTime();
                teacherMatching.getBufferTimeTask().stopTime();
            }

                // 维护答题状态 问题跳到下一个月
                int unaAnswered2 = classQuestionDescMapper.getUnAnswerd(userInfo.getClassId(), null, null);
                if (unaAnswered2 == 0) {
                    Clazz clazz = new Clazz();
                    clazz.setId(userInfo.getClassId());
                    clazz.setAnswerState(4);
                    clazz.setModifyTime(new Date());
                    clazz.setModifyPerson(userInfo.getAccount());
                    clazz.setTimeRemain(0L);
                    clazz.setBufferRemain(0L);
                    classMapper.updateClassNameById(clazz);
                }
                 classQuestionOptionMapper.insertQuestionMonth(userInfo.getClassId(), userInfo.getAccount());
                // 获取下一月时间
                CountDownDO countDownDO = this.getCountDownTime(userInfo.getClassId(), userInfo.getTeamId());
                if(unaAnswered2 == 0){
                    countDownDO.setWhetherStart(4);
                }else {
                        countDownDO.setWhetherStart(clazzExit.getAnswerState());

                    //如果原来是开始状态 默认开始
                    if (clazzExit.getAnswerState() == 2) {
                        //停止老师倒计时
                        InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                                .getTimerAndTaskUsage().stopTime();
                        InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                                .getBufferTimeTask().stopTime();
                        //设置倒计时任务时间
                        InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                                .getTimerAndTaskUsage().setTime(countDownDO.getRecidueMillSecond());
                        InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                                .getBufferTimeTask().setTime(countDownDO.getRecidueBufferTime());
                        //设置跳转下一月状态
                        countDownDO.setWhetherNextMonth(1);
                        //启动答题倒计时
                        InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                                .getTimerAndTaskUsage().startTime();
                        //修改班级绑定的时间
                        this.updateClassStatus(userInfo.getClassId(),
                                2, userInfo.getAccount(), countDownDO.getRecidueMillSecond(), countDownDO.getRecidueBufferTime());
                    }
                    // 如果原来是暂停状态 要维护到班级表当前时间
                    else if (clazzExit.getAnswerState() == 3) {
                        //判断老师内存是否有倒计时
                        //有
                        if (null != teacherMatching) {
                            //给定时任务设置倒计时时间
                            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                                    .getTimerAndTaskUsage().setTime(countDownDO.getRecidueMillSecond());
                            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                                    .getBufferTimeTask().setTime(countDownDO.getRecidueBufferTime());
                            //通知前端是否跳到下一个月
                            countDownDO.setWhetherNextMonth(1);
                        }
                        //没有 创建老师内存
                        else {
                            TimerAndTaskUsage timerAndTaskUsage = new TimerAndTaskUsage(countDownDO.getRecidueMillSecond(), userInfo, this, 0);
                            BufferTimeTask bufferTimeTask = new BufferTimeTask(countDownDO.getRecidueBufferTime(),
                                    countDownDO.getRecidueBufferTime(), userInfo, this, 0);
                            //通知前端是否跳到下一个月
                            countDownDO.setWhetherNextMonth(1);
                            //设置loginMap、channelUserMap
                            userInfo.setCountDownDO(countDownDO);
                            InformationOperateMapMatching.setUserRelationInfo(ctx, userInfo);
                            //设置上下文信息
                            InformationOperateMapMatching.add(ctx, userInfo);
                            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                                    .setTimerAndTaskUsage(timerAndTaskUsage);
                            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                                    .setBufferTimeTask(bufferTimeTask);
                        }
                        //维护到班级表时间。
                        this.updateClassStatus(userInfo.getClassId(),
                                3, userInfo.getAccount(), countDownDO.getRecidueMillSecond(), countDownDO.getRecidueBufferTime());
                    }
                }

                //更新老师上下文信息
                userInfo.setCountDownDO(countDownDO);
                InformationOperateMapMatching.add(ctx, userInfo);
                //发送消息
                sendAll(userInfo.getTable(), countDownDO);

        }catch (Exception e){
            logger.error("调用下一月失败",e);
            throw  e;
        }
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMsg(UserInfo userInfo) throws Exception{


        //NO.1 所有人都答完题 维护班级答题状态 自动跳转到下一个月
        int unaAnswered = classQuestionDescMapper.getUnAnswerd(userInfo.getClassId(), null, null);
        if (unaAnswered == 0) {
            Clazz clazz = new Clazz();
            clazz.setId(userInfo.getClassId());
            clazz.setAnswerState(4);
            clazz.setModifyTime(new Date());
            clazz.setModifyPerson("AUTO");
            clazz.setTimeRemain(0L);
            classMapper.updateClassNameById(clazz);
            //停止所有的定时任务
            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                    .getTimerAndTaskUsage().stopTime();
            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                    .getBufferTimeTask().stopTime();
        }
        classQuestionOptionMapper.insertQuestionMonth(userInfo.getClassId(), userInfo.getAccount());

        //NO.2 发送通知消息
        InformationOperateMapMatching teacherMatching =
                this.getMatching(userInfo.getTable(),userInfo.getId());
        //停止倒计时
        if(null != teacherMatching.timerAndTaskUsage) {
            teacherMatching.timerAndTaskUsage.stopTime();
        }
        UserInfo teacher = teacherMatching.getUserInfo();
        CountDownDO countDownTeacher = null;
        int wheatherStart = 0;
        if (teacher != null) {
            countDownTeacher = teacher.getCountDownDO();
            wheatherStart = countDownTeacher.getWhetherStart();
        }
        // 获取下一月时间
        CountDownDO countDownDO = this.getCountDownTime(userInfo.getClassId(), userInfo.getTeamId());
        if(countDownDO.getRecidueMillSecond() == 0L){
            countDownDO.setWhetherStart(4);
        }else {
            if (wheatherStart > 0) {
                countDownDO.setWhetherStart(wheatherStart);
            }

            //如果原来是开始状态 默认开始
            if (wheatherStart == 2) {
                //停止老师倒计时
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                        .getTimerAndTaskUsage().stopTime();
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                        .getBufferTimeTask().stopTime();
                //设置倒计时任务时间
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                        .getTimerAndTaskUsage().setTime(countDownDO.getRecidueMillSecond());
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                        .getBufferTimeTask().setTime(countDownDO.getRecidueBufferTime());
                //设置跳转下一月状态
                countDownDO.setWhetherNextMonth(1);
                //启动答题倒计时
                InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                        .getTimerAndTaskUsage().startTime();
                //修改班级绑定的时间
                this.updateClassStatus(userInfo.getClassId(),
                        2, userInfo.getAccount(), countDownDO.getRecidueMillSecond(), countDownDO.getRecidueBufferTime());
            }
            // 如果原来是暂停状态 要维护到班级表当前时间
            else if (wheatherStart == 3) {
                //判断老师内存是否有倒计时
                //有
                if (null != teacherMatching) {
                    //给定时任务设置倒计时时间
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .getTimerAndTaskUsage().setTime(countDownDO.getRecidueMillSecond());
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .getBufferTimeTask().setTime(countDownDO.getRecidueBufferTime());
                    //通知前端是否跳到下一个月
                    countDownDO.setWhetherNextMonth(1);
                }
                //没有 创建老师内存
                else {
                    TimerAndTaskUsage timerAndTaskUsage = new TimerAndTaskUsage(countDownDO.getRecidueMillSecond(), userInfo, this, 0);
                    BufferTimeTask bufferTimeTask = new BufferTimeTask(countDownDO.getRecidueBufferTime(),
                            countDownDO.getRecidueBufferTime(), userInfo, this, 0);
                    //通知前端是否跳到下一个月
                    countDownDO.setWhetherNextMonth(1);
                    //设置loginMap、channelUserMap
                    userInfo.setCountDownDO(countDownDO);
                    //设置上下文信息
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .setTimerAndTaskUsage(timerAndTaskUsage);
                    InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).get(userInfo.getId())
                            .setBufferTimeTask(bufferTimeTask);
                }
                //维护到班级表时间。
                this.updateClassStatus(userInfo.getClassId(),
                        3, userInfo.getAccount(), countDownDO.getRecidueMillSecond(), countDownDO.getRecidueBufferTime());
            }
        }

        //更新老师上下文信息
        userInfo.setCountDownDO(countDownDO);
        //发送消息
        sendAll(userInfo.getTable(), countDownDO);
    }

    @Override
    public void errorPause(UserInfo userInfo)  {

        CountDownDO countDownDO = this.getCountDownTime(userInfo.getClassId(), userInfo.getTeamId());
        Clazz clazz =  classMapper.getClassByid(userInfo.getClassId());
        //NO.1 班级状态置为暂停
        try {
            this.updateClassStatus(userInfo.getClassId(),
                    3, userInfo.getAccount(), null, null);

            //NO.2 倒计时任务置为暂停 重新设置时间
            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).
                    get("teacher" + userInfo.getClassId()).getBufferTimeTask().stopTime();
            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).
                    get("teacher" + userInfo.getClassId()).getTimerAndTaskUsage().stopTime();
            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).
                    get("teacher" + userInfo.getClassId()).getBufferTimeTask().setTime(clazz.getBufferRemain());
            InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).
                    get("teacher" + userInfo.getClassId()).getTimerAndTaskUsage().setTime(clazz.getTimeRemain());

            //NO.3 向已登录老师发送错误信息
            countDownDO.setWhetherStart(3);
            countDownDO.setRecidueBufferTime(clazz.getBufferRemain());
            countDownDO.setRecidueMillSecond(clazz.getTimeRemain());
            countDownDO.setErrorMsg("自动维护随机答案失败 请稍后重试");
            //发送消息
            userInfo.setCountDownDO(countDownDO);
            InformationOperateMapMatching.add(InformationOperateMapMatching.matchingMap.get(userInfo.getTable()).
                    get("teacher" + userInfo.getClassId()).getChannelHandlerContext(), userInfo);
            sendAll(userInfo.getTable(), countDownDO);
        }catch (Exception e){
            logger.error("回滚班级状态异常",e);
        }
    }


    public InformationOperateMapMatching getMatching(String table,String userId){
        ConcurrentMap<String, InformationOperateMapMatching>
                concurrentMap = InformationOperateMapMatching.matchingMap.get(table);
        if(null == concurrentMap){
            return null;
        }else {
            return concurrentMap.get(userId);
        }
    }

    public void sendAll(String table , CountDownDO countDownDO) throws Exception{
        InformationOperateMapMatching.matchingMap.get(table).forEach((key, iom) -> {
            try {
                UserInfo localUserInfo = iom.getUserInfo();
                localUserInfo.setCountDownDO(countDownDO);
                iom.sead(localUserInfo);
            } catch (Exception e) {
                logger.error("发送消息异常 ===" +table, e);
            }
        });
    }


    @Override
    public void addTime(int classId,long totalTime, long addTime,String account) {
        Date currentMonth = classQuestionDescMapper.getLocalMonth(classId,1);
        Map<String ,Object> paramMap = new HashMap<>();
        paramMap.put("classId" ,classId);
        paramMap.put("addTime",addTime);
        paramMap.put("totalTime",totalTime);
        paramMap.put("currentMonth",currentMonth);
        paramMap.put("account",account);
        classQuestionDescMapper.addTime(paramMap);

        classMapper.addTime(paramMap);
    }


    List<StuAnswerDO> converteStu(QuestionAnswerDTO questionAnswerDTO , List<HseRequest> answers, int teamId, int classId){

        List<StuAnswerDO> stuAnswerDOS = new ArrayList<>();
        //NO.1 如果有小题 把大题记录维护到答案表
        if(!questionAnswerDTO.getThisChildrenItem().isEmpty() && questionAnswerDTO.getThisItemId() != 35){
            StuAnswerDO stuAnswerDO = new StuAnswerDO();
            stuAnswerDO.setQuestionId(questionAnswerDTO.getThisItemId());
            if(questionAnswerDTO.getThisItemAnswer().size()>0){
                stuAnswerDO.setAnswer(JSON.toJSONString(questionAnswerDTO.getThisItemAnswer()));
            }else {
                stuAnswerDO.setAnswer("["+'"' +"大题"+'"'+"]");
            }
            stuAnswerDO.setTeamId(teamId);
            stuAnswerDO.setClassId(classId);
            stuAnswerDO.setYear(questionAnswerDTO.getQuestionYear());
            stuAnswerDO.setMonth(questionAnswerDTO.getQuestionMonth());
            stuAnswerDO.setRootId(questionAnswerDTO.getThisItemId());
            stuAnswerDOS.add(stuAnswerDO);
        }
        //NO.2 复制实体数据
        for(HseRequest hseRequest : answers){
            StuAnswerDO stuAnswerDO = new StuAnswerDO();
            BeanUtils.copyProperties(hseRequest, stuAnswerDO);
            stuAnswerDO.setAnswer(JSON.toJSONString(hseRequest.getAnswer()));
            stuAnswerDO.setYear(questionAnswerDTO.getQuestionYear());
            stuAnswerDO.setMonth(questionAnswerDTO.getQuestionMonth());
            stuAnswerDO.setRootId(questionAnswerDTO.getThisItemId());
            stuAnswerDOS.add(stuAnswerDO);
        }
        return stuAnswerDOS;
    }

    List<HseRequest> getAswers(QuestionAnswerDTO questionAnswerDTO,int teamId,int classId){

        List<HseRequest> returnAnswer = new ArrayList<>();
         List<SecondQuestionDTO> secondQuestionDTOS = questionAnswerDTO.getThisChildrenItem();
         if(secondQuestionDTOS.isEmpty() || questionAnswerDTO.getThisItemId() == 251){
             HseRequest hseRequest = new HseRequest();
             BeanUtils.copyProperties(questionAnswerDTO,hseRequest);
             hseRequest.setClassId(classId);
             hseRequest.setTeamId(teamId);
             hseRequest.setAnswer(questionAnswerDTO.getThisItemAnswer());
             hseRequest.setQuestionId(questionAnswerDTO.getThisItemId());
             hseRequest.setWeight(questionAnswerDTO.getWeight());
             hseRequest.setRootId(questionAnswerDTO.getThisItemId());
             returnAnswer.add(hseRequest);
         }else{
             for(SecondQuestionDTO secondQuestionDTO : secondQuestionDTOS){
                 List<ThirdQuestionDTO> thirdQuestionDTOS = secondQuestionDTO.getThisChildrenItem();
                 if( secondQuestionDTO.getThisItemAnswer() != null&& !secondQuestionDTO.getThisItemAnswer().isEmpty()){
                     HseRequest hseRequest = new HseRequest();
                     BeanUtils.copyProperties(secondQuestionDTO,hseRequest);
                     hseRequest.setClassId(classId);
                     hseRequest.setTeamId(teamId);
                     hseRequest.setAnswer(secondQuestionDTO.getThisItemAnswer());
                     hseRequest.setQuestionId(secondQuestionDTO.getThisItemId());
                     hseRequest.setWeight(questionAnswerDTO.getWeight());
                     hseRequest.setRootId(questionAnswerDTO.getThisItemId());
                     returnAnswer.add(hseRequest);
                 }
                 if(null != thirdQuestionDTOS) {
                     for (ThirdQuestionDTO thirdQuestionDTO : thirdQuestionDTOS) {
                         if (thirdQuestionDTO.getThisItemAnswer() != null && !thirdQuestionDTO.getThisItemAnswer().isEmpty()) {
                             HseRequest hseRequest = new HseRequest();
                             BeanUtils.copyProperties(thirdQuestionDTO, hseRequest);
                             hseRequest.setClassId(classId);
                             hseRequest.setTeamId(teamId);
                             hseRequest.setAnswer(thirdQuestionDTO.getThisItemAnswer());
                             hseRequest.setQuestionId(thirdQuestionDTO.getThisItemId());
                             hseRequest.setWeight(questionAnswerDTO.getWeight());
                             hseRequest.setRootId(questionAnswerDTO.getThisItemId());
                             returnAnswer.add(hseRequest);
                         }
                     }
                 }

             }

         }
        return returnAnswer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
   public void setNextYearDataAop(String thisTimeLine ,Long nextYear ,Long nextMonth,HseRequest hseRequest) throws Exception {

        try {
            List<String> dateStrs = DateUtil.getMonthBetween(nextYear + "-01", nextYear, nextMonth);

            //时间线设置为下一年的一月一号
            String nextMonthStr = nextMonth.toString();
            if (nextMonthStr.length() == 1) {
                nextMonthStr = "0" + nextMonthStr;
            }
            hseRequest.setTimeLine(nextYear + "-" + nextMonthStr + "-01");
            //初始化年初数据
            String year = nextYear.toString();
            if (ArrayUtils.contains(NormalConstant.STAGR_YEAR, year)) {
                String sc = NormalConstant.WEY;
                if (ArrayUtils.contains(NormalConstant.STAGR_YEAR_H6, year)) {
                    sc = NormalConstant.H6;
                } else if (ArrayUtils.contains(NormalConstant.STAGR_YEAR_H8, year)) {
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
            Map<String, Object> map = new HashMap<>();
            map.put("classId", hseRequest.getClassId());
            map.put("teamId", hseRequest.getTeamId());
            map.put("year", year);
            initTeamIntermediateService.updateInitFinancialCost(map);


            for (String datestr : dateStrs) {
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

            if (!dateStrs.isEmpty()) {
                //维护月度利润
                Long monthNextYear = Long.valueOf(dateStrs.get(dateStrs.size() - 1).substring(0, 4));
                Long mongthNextMonth = Long.valueOf(dateStrs.get(dateStrs.size() - 1).substring(5, 7));
                ((QuestionService) AopContext.currentProxy()).setMonthlyProfit(
                        hseRequest.getClassId(), hseRequest.getTeamId(), dateStrs.get(0) + "-01", monthNextYear, mongthNextMonth, dateStrs);
            }
        }catch (Exception e){
            throw e;
        }

    }

}
