package com.tzcpa.service.question.impl;

import com.alibaba.fastjson.JSON;
import com.tzcpa.config.netty.InformationOperateMapMatching;
import com.tzcpa.constant.EventMethodEnum;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.constant.QuestionContractEnum;
import com.tzcpa.constant.VNCConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.mapper.student.StuAnswerMapper;
import com.tzcpa.mapper.student.TeamAnnualWorthGatherMapper;
import com.tzcpa.mapper.student.TeamMonthlyProfitStatementMapper;
import com.tzcpa.mapper.teacher.ClassMapper;
import com.tzcpa.mapper.treatment.*;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.model.student.StuAnswerDO;
import com.tzcpa.model.student.TeamAnnualWorthGather;
import com.tzcpa.model.student.TeamMonthlyProfitStatementDO;
import com.tzcpa.model.teacher.AutoAnswerDO;
import com.tzcpa.model.teacher.AutoAnswerDO2;
import com.tzcpa.model.teacher.BaseAutoAnswer;
import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.treatment.*;
import com.tzcpa.service.question.BalanceScoreSettlementService;
import com.tzcpa.service.question.CommonService;
import com.tzcpa.service.question.QuetionCommonService;
import com.tzcpa.service.student.AdaptationHseService;
import com.tzcpa.service.student.IBalanceSheetJSService;
import com.tzcpa.service.student.TeamStrategyMapService;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @ClassName QuetionCommonServiceImpl
 * @Description
 * @Author wangzhangju
 * @Date 2019/7/11 13:42
 * @Version 6.0
 **/
@Slf4j
@Service
public class QuetionCommonServiceImpl extends AdaptationHseService implements QuetionCommonService {

    @Resource
    private ClassQuestionDescMapper classQuestionDescMapper;

    @Resource
    private StuAnswerMapper stuAnswerMapper;

    @Resource
    private TeamBalanceSheetMapper teamBalanceSheetMapper;

    @Resource
    private TeamAnnualWorthGatherMapper teamAnnualWorthGatherMapper;

    @Resource
    private OSMapper osMapper;

    @Resource
    private ClassMapper classMapper;

    @Resource
    private InitTeamIntermediateService initTeamIntermediateService;

    @Resource
    private BalanceScoreSettlementService balanceScoreSettlementService;

    @Resource
    private CommonService commonService;

    @Resource
    private IBalanceSheetJSService bsService;

    @Resource
    private TeamStrategyMapService tsmService;

    @Resource
    private TeamMonthlyProfitStatementMapper teamMonthlyProfitStatementMapper;

    @Resource
    private TeamProfitStatementMapper teamProfitStatementMapper;

    @Resource
    private ClassQuestionOptionMapper classQuestionOptionMapper;

    @Override

    public int autoAnswer(UserInfo userInfo, Date currentMonth) throws Exception{

        try {
            //NO.1 获取当前年月
            //NO.2 获取要维护答案的问题id
            List<AutoAnswerDO> autoAnswerDOS = classQuestionDescMapper.getAutoQuestion(userInfo.getClassId(), currentMonth);
            //NO.3 自动维护答案
            List<StuAnswerDO> stuAnswerDOS = new ArrayList<>();
            List<StuAnswerDO> stuAnswerAll = new ArrayList<>();
            for (AutoAnswerDO autoAnswerDO : autoAnswerDOS) {

                //NO.3-1 维护答案
                //NO.3-1-1 获取答案
                stuAnswerDOS = this.getAutoAnswer(autoAnswerDO);
                //NO.3-1-2 保存答案
                if (!stuAnswerDOS.isEmpty()) {
                    stuAnswerMapper.insertStuAnswerBatch(stuAnswerDOS);
                }
                //NO.3-1-3 获取所有的答案 用于调用计算影响方法
                stuAnswerAll.addAll(stuAnswerDOS);
            }



            //NO.4 执行自动答题后的算分逻辑 根据team_id 和
            List<HseRequest> answers = new ArrayList<>();
            Integer nextRootId = null;
            Integer nextTeamId = null;
            int i = 0;
            for (StuAnswerDO questionAnswerDTO : stuAnswerAll) {
                HseRequest hseRequest = new HseRequest();
                BeanUtils.copyProperties(questionAnswerDTO, hseRequest);
                hseRequest.setAnswer(JsonUtil.jsonToList(questionAnswerDTO.getAnswer(), String.class));
                String month = String.valueOf(questionAnswerDTO.getMonth());
                if (month.length() == 1) {
                    month = "0" + month;
                }
                hseRequest.setTimeLine(questionAnswerDTO.getTimeLine());
                answers.add(hseRequest);
                i++;
                if (stuAnswerAll.size() > i) {
                    StuAnswerDO nextQuestionAnswerDTO = stuAnswerAll.get(i);
                    nextRootId = nextQuestionAnswerDTO.getRootId();
                    nextTeamId = nextQuestionAnswerDTO.getTeamId();
                }

                // 处理大题调用逻辑 -- 当前题走到最后一题 （当前题和下一题的大题id不一样
                if (((stuAnswerAll.size() <= i) || ((nextRootId != null && !nextRootId.equals(questionAnswerDTO.getRootId()))
                        || (nextTeamId != null && !nextTeamId.equals(questionAnswerDTO.getTeamId())))) && answers.size() > 0) {

                    if (answers.size() > 1) {
                        answers.remove(0);
                    }
                    //大题不调用
                    if (answers.size() == 1 && answers.get(0).getAnswer().get(0).equals(" ") && answers.get(0).getRootId()!=160 ) {
                        //2010-12题目 总体战略选择-增加研发费用
                        if (answers.get(0).getRootId() == 2) {
                            this.addDevExpand(answers);
                        }
                        this.treatmentEffect(answers.get(0));
                    }

                    if(answers.size() > 1 || !answers.get(0).getAnswer().get(0).equals(" ") || answers.get(0).getRootId()==160){

                        ResponseResult responseResult = commonService.calling(questionAnswerDTO.getEventCode(), answers);
                        //NO.4  融资决策题目根据答案为A、B、C 跳过下一题 生成月度利润 112 下题 113
                        if (answers.get(0).getRootId() == 112) {
                            //NO.4-1 给下一题保存答案为空格
                            List<StuAnswerDO> answerDOS = new ArrayList<>();
                            StuAnswerDO stuAnswerDO = new StuAnswerDO();
                            stuAnswerDO.setClassId(userInfo.getClassId());
                            stuAnswerDO.setTeamId(questionAnswerDTO.getTeamId());
                            stuAnswerDO.setAnswer("[" + '"' + "跳过" + '"' + "]");
                            if ("D".equals(answers.get(0).getAnswer().get(0))) {
                                stuAnswerDO.setQuestionId(393);
                                stuAnswerDO.setRootId(393);
                                stuAnswerDO.setYear(2016);
                                stuAnswerDO.setMonth(10);
                            } else {
                                stuAnswerDO.setQuestionId(113);
                                stuAnswerDO.setRootId(113);
                                stuAnswerDO.setYear(2016);
                                stuAnswerDO.setMonth(10);
                            }
                            answerDOS.add(stuAnswerDO);
                            stuAnswerMapper.insertStuAnswerBatch(answerDOS);
                        }
                    }
                    
                    answers = new ArrayList<>();

                }
            }
        }catch (Exception e){
            throw  e;
        }
            return 0;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult answer(QuestionAnswerDTO questionAnswerDTO, int teamId, int classId ,String Account) throws Exception {
        //NO.1 回答当前题目 - 处理多个小题的情况
        //NO.1-1 只获取小题
        try {
            List<HseRequest> answers = this.getAswers( questionAnswerDTO,teamId,classId);
            if(!answers.isEmpty()){
                //NO.1-2 获取小题和大题
                List<StuAnswerDO> stuAnswerDOS = this.converteStu(questionAnswerDTO,answers, teamId, classId);
                stuAnswerMapper.insertStuAnswerBatch(stuAnswerDOS);
            }
          //NO.1-2 所有人都答完题 维护班级答题状态
            int unaAnswered = classQuestionDescMapper.getUnAnswerd(classId,null,null);
            if(unaAnswered == 0){
                Clazz clazz = new Clazz();
                clazz.setId(classId);
                clazz.setAnswerState(4);
                clazz.setModifyTime(new Date());
                clazz.setModifyPerson(Account);
                clazz.setTimeRemain(0L);
                classMapper.updateClassNameById(clazz);
                //停止所有的定时任务
                //停止所有的定时任务
                if(null !=  InformationOperateMapMatching.matchingMap.get(classId+"")
                &&null !=  InformationOperateMapMatching.matchingMap.get(classId+"").get("teacher"+classId)
                &&null !=  InformationOperateMapMatching.matchingMap.get(classId+"").get("teacher"+classId)
                        .getTimerAndTaskUsage()) {
                    InformationOperateMapMatching.matchingMap.get(classId+"").get("teacher" + classId)
                            .getTimerAndTaskUsage().stopTime();
                }
                if(null != InformationOperateMapMatching.matchingMap.get(classId+"")&&
                        null != InformationOperateMapMatching.matchingMap.get(classId+"").get("teacher"+classId) &&
                        null !=  InformationOperateMapMatching.matchingMap.get(classId+"").get("teacher"+classId)
                        .getBufferTimeTask()) {
                    InformationOperateMapMatching.matchingMap.get(classId+"").get("teacher" + classId)
                            .getBufferTimeTask().stopTime();
                }
            }

            //NO.2 把皮卡车的销售预存答案（questionId为 160）维护到下一题 （questionId 为 251）
            if(questionAnswerDTO.getThisItemId() == 160){
                List<SecondQuestionDTO> thisChildrenItem = questionAnswerDTO.getThisChildrenItem();
                List<ClassQuestionOptionDO> optionDOS = classQuestionOptionMapper.getQuestionOptionsById(251,classId,"3");
                int i = 0;
                for(ClassQuestionOptionDO classQuestionOptionDO:optionDOS){
                    if(classQuestionOptionDO.getLastId() == null) {
                        classQuestionOptionDO.setQuesionOption("2");
                        classQuestionOptionDO.setQuesionOptionDesc(thisChildrenItem.get(i).getThisItemAnswer().get(0));
                        classQuestionOptionDO.setTeamId(teamId);
                        classQuestionOptionMapper.insert(classQuestionOptionDO);
                    }
                    i++;
                }
            }

            //NO.3 执行答题后的 计算影响逻辑
            String eventCode = questionAnswerDTO.getEventCode();

            ResponseResult responseResult = commonService.calling(eventCode, answers);

            //NO.4  融资决策题目根据答案为A、B、C 跳过下一题 生成月度利润 112 下题 113
//            if(questionAnswerDTO.getThisItemId() == 112 && !questionAnswerDTO.getThisItemAnswer().get(0).equals("D")){
            if(questionAnswerDTO.getThisItemId() == 112){
                //NO.4-1 给下一题保存答案为空格
                List<StuAnswerDO> answerDOS = new ArrayList<>();
                StuAnswerDO stuAnswerDO = new StuAnswerDO();
                stuAnswerDO.setClassId(classId);
                stuAnswerDO.setTeamId(teamId);
                stuAnswerDO.setAnswer("["+'"'+"跳过"+'"'+"]");
                if ("D".equals(questionAnswerDTO.getThisItemAnswer().get(0))) {
                    stuAnswerDO.setQuestionId(393);
                    stuAnswerDO.setRootId(393);
                    stuAnswerDO.setYear(2016);
                    stuAnswerDO.setMonth(10);
                }else{
                    stuAnswerDO.setQuestionId(113);
                    stuAnswerDO.setRootId(113);
                    stuAnswerDO.setYear(2016);
                    stuAnswerDO.setMonth(10);
                }
                answerDOS.add(stuAnswerDO);
                stuAnswerMapper.insertStuAnswerBatch(answerDOS);

            }
            return responseResult;
        }catch (Exception e){
            throw e;
        }
    }


    public  void setMonthlyProfit(Integer classId,Integer teamId,String currentDate,Long nextYear,Long nextMonth,List<String> dateStrs) throws Exception{
        log.info("设置月度利润表数据 初始化参数 classId={}  teamId={}  currentDate={} nextYear={} nextMonth={}",classId,teamId,currentDate,nextYear,nextMonth);
        List<TeamMonthlyProfitStatementDO> teamMonthlyProfitStatementDOs =teamMonthlyProfitStatementMapper.selectFromIntermediate
                (classId,teamId,Integer.parseInt(currentDate.substring(0,4)),
                        Integer.parseInt(currentDate.substring(5,7)),nextYear,nextMonth);
        log.info("设置月度利润表数据 teamMonthlyProfitStatementDOs={}", JSON.toJSONString(teamMonthlyProfitStatementDOs));
//        for(TeamMonthlyProfitStatementDO teamMonthlyProfitStatementDO : teamMonthlyProfitStatementDOs) {
//            teamMonthlyProfitStatementMapper.insertSelective(teamMonthlyProfitStatementDO);
//        }
        // hanxf 20190717
        teamMonthlyProfitStatementMapper.insertBanchMonthlyProfitStatement(teamMonthlyProfitStatementDOs);
        //执行影响月度利润数据的影响
        handleDelayedUpdate(osMapper, new HseRequest(classId, teamId, currentDate), NormalConstant.DELAYED_TYPE_CWFY.toString(), bsService, dateStrs);
    }

    List<StuAnswerDO> getAutoAnswer(AutoAnswerDO autoAnswerDO){
        List<StuAnswerDO> stuAnswerDOS = new ArrayList<>() ;
        //NO.1 维护投标竞争活动大题固定答案 或 自动答案
        if(autoAnswerDO.getChildren().get(0).getRootId().equals(274)){
        	stuAnswerDOS.add(this.setStuAnswerTH(autoAnswerDO,null));
        }else if(autoAnswerDO.getChildren().get(0).getRootId().equals(35) ||autoAnswerDO.getChildren().get(0).getRootId().equals(93)){
        	Integer count=osMapper.getcountDateAnswer(autoAnswerDO.getTeamId(),autoAnswerDO.getChildren().get(0).getRootId());
        	if(count.equals(0)){
        		//NO.1 维护大题固定答案 或 自动答案
                stuAnswerDOS.add(this.setStuAnswer(autoAnswerDO,"["+'"' +" "+'"'+"]"));
        		List<AutoAnswerDO2> secondQuestions = autoAnswerDO.getChildren().subList(0, 6);
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
        	}else if(count.equals(1)){
        		List<AutoAnswerDO2> secondQuestions = autoAnswerDO.getChildren().subList(0, 6);
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
        }else{
        	//NO.1 维护大题固定答案 或 自动答案
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
                /*if(secondQuestions.get(0).getRootId().equals(35) || secondQuestions.get(0).getRootId().equals(93)){
                    secondQuestions = autoAnswerDO.getChildren().subList(0, 6);
                }*/
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

    public void addDevExpand(List<HseRequest> hrList) throws Exception{
        String temp = osMapper.selectConfVariable(VNCConstant.YFFY2010);
        log.info(temp);
        Double developmentCost = Double.parseDouble(temp) / 1000000d;
        log.info(developmentCost.toString());
        HseRequest hseRequest = hrList.get(0);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("classId", hseRequest.getClassId());
        map.put("teamId", hseRequest.getTeamId());
        map.put("year", hseRequest.getTimeLine().substring(0, 4));
        map.put("inDevelopmentCost", developmentCost.toString());
        initTeamIntermediateService.updateInitInDevelopmentCost(map);
    }

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


    public void setYearlyProfit(Integer classId,Integer teamId,Date currentDate) throws Exception{
        log.info("设置年度利润数据 入口参数 classId={} teamId={} currentDate={}",classId,teamId,currentDate);
        Integer lastYear = classQuestionDescMapper.getLastYear(currentDate,classId);
        log.info("设置年度利润数据 lastYear={}",lastYear);
        //NO.5-2 如果题目的下一月份跨到下一年 维护当前团队的年度利润
        List<TeamProfitStatementDO> doList = teamProfitStatementMapper.getDataFromMonthly(classId,teamId, currentDate,lastYear);
        log.info("设置年度利润数据 doList={}",JSON.toJSONString(doList));
        if (doList.size() > 0) {
            teamProfitStatementMapper.insertForBatch(doList);
        }

        //NO.6 计算当前团队的 资产负债
        //NO.6-1 获取资产负债数据
        List<TeamBalanceSheetDO> listBalance = teamBalanceSheetMapper.getBalanceSheetSource(classId,teamId,lastYear, currentDate);
        log.info("设置资产负债表的数据 listBalance={}",JSON.toJSONString(listBalance));
        //NO.6-2 保存资产负债数据
        for (TeamBalanceSheetDO teamBalanceSheetDO : listBalance) {
            teamBalanceSheetMapper.insert(teamBalanceSheetDO);
        }

        // 执行影响资产负债表数据的影响
//		handleDelayedUpdate(osMapper,
//				new HseRequest(classId, teamId, DateUtil.dateToStr(DateUtil.FORMART_5, currentDate)),
//				NormalConstant.DELAYED_TYPE_ZCFZ_TABLE, bsService);

        //NO.7 获取当前团队的 年度分析数据
        List<TeamAnnualWorthGather> teamAnnualWorthGathers = teamAnnualWorthGatherMapper.getAnnual(classId,teamId,lastYear,currentDate);
        log.info("设置年度分析表的数据 teamAnnualWorthGathers={}",JSON.toJSONString(teamAnnualWorthGathers));
        for(TeamAnnualWorthGather teamAnnualWorthGather:teamAnnualWorthGathers){
            teamAnnualWorthGatherMapper.insert(teamAnnualWorthGather);
        }
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

    List<StuAnswerDO> converteStu(QuestionAnswerDTO questionAnswerDTO , List<HseRequest> answers, int teamId, int classId){

        List<StuAnswerDO> stuAnswerDOS = new ArrayList<>();
        //NO.1 如果有小题 把大题记录维护到答案表
        if(!questionAnswerDTO.getThisChildrenItem().isEmpty() && questionAnswerDTO.getThisItemId() != 35
                && questionAnswerDTO.getThisItemId() != 93){
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

}
