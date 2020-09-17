package com.tzcpa.service.student;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.ExceptionCodeEnumConsts;
import com.tzcpa.constant.MessageConstant;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.exceptions.MissingParametersException;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.question.ImpactJudgeMethodParam;
import com.tzcpa.model.question.TBalanceSheetImpactDO;
import com.tzcpa.model.student.AnswerScoreDO;
import com.tzcpa.model.student.BalanceVariableDO;
import com.tzcpa.model.student.DelayedUpdateDO;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.model.student.StuScoreDTO;
import com.tzcpa.model.student.UacDO;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.CompareResultUtils;
import com.tzcpa.utils.DateUtil;
import com.tzcpa.utils.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>Description: 做题抽象</p>
 *
 * @author WTL
 * @date 2019年5月10日
 */
@Slf4j
public abstract class AHseService implements IHseService {

    @Autowired
    private InitTeamIntermediateService initTeamIntermediateService;
    /**
     * 进行数据影响处理
     *
     * @param osMapper
     * @param scoreDO
     * @param hseRequest
     * @param viList
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> handleFinanceImpact(OSMapper osMapper, AnswerScoreDO scoreDO,
                                                   HseRequest hseRequest, List<String[]> viList) throws Exception {
        try {

        //检查必备参数
        if (!hseRequest.checkCTTQ() || !scoreDO.checkACQ()) {
            log.info(scoreDO + "\n" + hseRequest);
            throw new MissingParametersException(ExceptionCodeEnumConsts.ERROR_IMPACT_HANDLE.getMsg());
        }

        //获取影响的任务
        Map<String, Object> impactTask = findImpactTask(osMapper, scoreDO);
        if (impactTask == null || impactTask.get("normal") == null) {
            log.info("没有对指标项立即产生产生影响的影响");
            return null;
        }

        //做影响处理
            Map<String ,Object> map = ((AHseService) AopContext.currentProxy()).doFinanceImpact(osMapper, viList, scoreDO, hseRequest, impactTask.get("normal").toString());
        return map;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 获取影响的任务
     *
     * @param osMapper
     * @param scoreDO
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> findImpactTask(OSMapper osMapper, AnswerScoreDO scoreDO) throws Exception {
        try {
            //获取会产生影响的财务指标项
            List<Map<String, Object>> impactTask = osMapper.getImpactCodes(scoreDO);
            //不会对任何指标产生影响
            if (impactTask == null || impactTask.isEmpty()) {
                log.info("没有对指标项产生影响" + scoreDO);
                return null;
            }

            //需要延迟的任务
            List<String[]> dList = new ArrayList<>();
            //不延迟的
            StringBuffer sb = new StringBuffer();
            for (Map<String, Object> ti : impactTask) {
                //需要延迟的
                if (ti.get("isDelayed") != null && Integer.valueOf(ti.get("isDelayed").toString()) != NormalConstant.NO_DELAYED_IMPACT_TYPE) {
                    //添加需要延迟的任务ID-延迟类型-执行次数-开始执行时间-所影响车型
                    dList.add(new String[]{ti.get("taskId").toString(), ti.get("isDelayed").toString(),
                            ti.get("executionTimes").toString(),
                            ti.get("bTime") == null ? null : ti.get("bTime").toString(),
                            ti.get("vehicleModel") == null ? null : ti.get("vehicleModel").toString(), ti.get("sortNum").toString()});
                } else { //不需要延迟的
                    sb.append(ti.get("taskId") + ",");
                }
            }

            HashMap<String, Object> map = new HashMap<>();
            map.put("delayed", dList);
            map.put("normal", sb.length() == 0 ? null : sb.toString().substring(0, sb.toString().length() - 1));
            return map;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 做影响处理
     * (返回当前时间线，延迟任务那里用)
     *
     * @param osMapper
     * @param viList
     * @param scoreDO
     * @param hseRequest
     * @param sb
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> doFinanceImpact(OSMapper osMapper, List<String[]> viList, AnswerScoreDO scoreDO, HseRequest hseRequest,
                                               String sb) throws Exception {
        try {
            //判断是否需要对变量进行解读
            Boolean hasVI = viList == null || viList.isEmpty() ? false : true;
            //判断是否单独配置了时间线
            Boolean hasBDate = false;
            //判断是否为执行的延迟任务
            Boolean hasDelayed = false;
            //影响的时长
            int impactNum = 0;
            //是否有任务执行
            Boolean isDo = false;

            //需要修改处理的调整数据集合
            List<UacDO> uacList = new ArrayList<>();
            //需要添加到资产负债表表处理的调整数据集合
            List<TBalanceSheetImpactDO> zcList = new ArrayList<>();

            //通过影响任务IDS查询出有影响的数据及规则
            List<UacDO> codesImpactData = osMapper.findCodesImpactData(sb, hseRequest.getClassId());
            for (UacDO uac : codesImpactData) {
                //重新初始化变量值
                hasBDate = false;
                hasDelayed = false;
                impactNum = 0;
                //如果有需要解析的变量则进入
                if (hasVI) {
                    if (!analysisVariableInfo(uac, viList, osMapper, hseRequest)) {
                        log.info("此影响因不符合判断条件减去：{}", uac);
                        continue;
                    }
                }

                //如果这个对象里面的这个属性有值说明是在执行延迟任务,重新赋值开始时间
                if (StringUtils.isNotBlank(scoreDO.getBImplementTime())) {
                    uac.setBDate(scoreDO.getBImplementTime());
                    hasDelayed = true;
                }

                //开始时间不为空的时候更改
                if (StringUtils.isNotBlank(uac.getBDate())) {
                    hasBDate = true;
                }

                //影响的时长
                impactNum = uac.getImpactNum();
                //如果需要战略字段则传入否则传入为null(现在都不需要战略直接传入为空)
                uac.setCTS(hseRequest, null);
                if (uac.getIsYear() != null && uac.getIsYear() == 1) {
                    //为年度影响
                    uac.setBYear(Integer.valueOf(hasBDate ? uac.getBDate().substring(0, 4) : hseRequest.getTimeLine().substring(0, 4)));
                    //如果为延迟的话，需要根据当前时间线和开始执行时间进行计算
                    if (hasDelayed) {
                        impactNum = getNumberOfImpact(hseRequest, uac, DateUtil.FORMART_2, Calendar.YEAR);
                    }
                    uac.setEYear(Integer.valueOf(DateUtil.getNextOrPrevDate(DateUtil.FORMART_2, uac.getBYear().toString(),
                            Calendar.YEAR, impactNum)));
                } else {
                    //为月度影响
                    uac.setYmBDate(DateUtil.getNextOrPrevDate(DateUtil.FORMART_4,
                            (hasBDate ? uac.getBDate().substring(0, 7) : hseRequest.getTimeLine().substring(0, 7)), 0, 0));
                    //如果为延迟的话，需要根据当前时间线和开始执行时间进行计算
                    if (hasDelayed) {
                        impactNum = getNumberOfImpact(hseRequest, uac, DateUtil.FORMART_4, Calendar.MONTH);
                    }
                    uac.setYmEDate(DateUtil.getNextOrPrevDate(DateUtil.FORMART_4, uac.getYmBDate(), Calendar.MONTH, impactNum));
                }

                //批处理添加
                //如果是资产负债表的则添加到资产负债表影响表的集合中,否则按照正常的处理
                if (uac.getTargetTable().equals("t_team_balance_sheet_impact")) {
                    JudgeAndAdd(zcList, uac);
                } else {
                    uacList.add(uac);
                }

                isDo = true;
            }

            //一条的时候单个处理
            if (uacList.size() == 1) {
                UacDO uacDO = uacList.get(0);
                if (hasDelayed) {
                    log.info("执行单个延迟任务===团队为：{},班级为：{},本月时间为：{},执行的数据对象为：{},调整后的值为：{}", uacDO.getTeamId(),
                            uacDO.getClassId(), hseRequest.getTimeLine(), uacDO,
                            JsonUtil.listToJson(osMapper.updateAdjustedColumnVAL(uacDO)));
                }
                osMapper.updateAdjustedColumn(uacDO);
            }
            //多个的时候批处理
            if (uacList.size() > 1) {
                osMapper.batchUpdateAdjustedColumn(uacList);
            }

            //有对资产负债表的影响的时候进行添加到表中
            if (zcList.size() > 0) {
                osMapper.addBatchTBSI(zcList);
            }

            //返回的值是在执行延迟任务时需要的开始时间和影响数，执行延迟任务的时候此list中只有一条数据所以可以直接取下标为0的数据
            Map<String, Object> map = new HashMap<>();
            map.put("impactNum", impactNum);
            //按照月度执行影响的开始时间
            map.put("bDate", codesImpactData.get(0).getYmBDate() == null ? codesImpactData.get(0).getBYear() : codesImpactData.get(0).getYmBDate());
            //按照月度执行影响的结束时间
            map.put("eDate", codesImpactData.get(0).getYmEDate() == null ? codesImpactData.get(0).getEYear() : codesImpactData.get(0).getYmEDate());
            //是否执行过影响
            map.put("isDo", isDo);
            return map;
        }catch (Exception e){
            throw e;
        }
    }
    
    /**
     * 判断并添加到对象和集合中
     * @param zcList
     * @param uac
     * @throws Exception
     */
    private void JudgeAndAdd(List<TBalanceSheetImpactDO> zcList, UacDO uac) throws Exception{
    	TBalanceSheetImpactDO zc  =null;
    	try {
			Field field = TBalanceSheetImpactDO.class.getDeclaredField(uac.getTargetColumn());
			field.setAccessible(true);
			for (int i = 0; i < zcList.size(); i++) {
				zc =zcList.get(i);
				//如果此影响的年在此集合中存在相对应的对象则进行添加并返回,否则新建一个进行添加
				if (zc.getYear().equals(uac.getBYear())) {
					field.set(zcList.get(i), Long.valueOf(uac.getOperation()));
					return;
				}
			}
			
			//如果此集合中没有相应的对象则进行添加一个对象
			zc = new TBalanceSheetImpactDO(uac);
			field.set(zc, Long.valueOf(uac.getOperation()));
			zcList.add(zc);
		} catch (Exception e) {
			log.info("影响的数据为：", uac);
			throw e;
		}
    }
    
    /**
     * 获取延迟影响的次数
     * @param hseRequest
     * @param uac
     * @return
     */
    private int getNumberOfImpact(HseRequest hseRequest, UacDO uac, String fm, int type){
    	return DateUtil.getDateAttr(DateUtil.strToDate(fm, hseRequest.getTimeLine()),type)
                - DateUtil.getDateAttr(DateUtil.strToDate(fm, uac.getBDate()),type)
                + 1;
    }

    /**
     * 返回true说明可以继续往下走了，false减去此影响继续下一个循环
     *
     * @param uac
     * @param viList
     * @return
     */
    private Boolean analysisVariableInfo(UacDO uac, List<String[]> viList, OSMapper osMapper, HseRequest hse) throws Exception {
        //用来判断有没有要减去此条影响的纪录
        Boolean doJudge = false;
        for (String[] vi : viList) {
            //根据变量名称判断是否是所对应的影响
            if (uac.getOperation().contains(vi[0])) {

                //如果为需要替换的则进行替换
                if (vi[1].equals(NormalConstant.JUDGE_EFFECT_REPLACE) || vi[1].equals(NormalConstant.REPLACEMENT_VALUE)) {
                    uac.setOperation(uac.getOperation().replace(vi[0], vi[2] == null ? "" : vi[2]));
                } else {
                    uac.setOperation(uac.getOperation().replace(vi[0], ""));
                }
                //需要判定的类型
                if (vi[1].equals(NormalConstant.SUBTRACT_EFFECT) || vi[1].equals(NormalConstant.JUDGE_EFFECT_REPLACE)) {
                    if (!doJudgeMethod(vi, osMapper, hse)) {
                        //纪录曾经有过要减去影响的纪录
                        doJudge = true;
                        //继续循环（因为有可能配有多种的判断条件，不仅仅就一个判断，不同的判断有可能要替换不同的值（如：研发费用投入1））
                        continue;
                    }

                    //替换值或者直接执行影响即可
                    return true;
                }

                //直接替换的类型
                return true;
            }
        }
        //进入说明有过要减去影响的纪录,所以要减去
        if (doJudge) {
            return false;
        }
        //走到这说明此影响没有和变量有关
        return true;
    }

    /**
     * 执行判断方法并返回结果
     *
     * @param vi
     * @return
     */
    private Boolean doJudgeMethod(String[] vi, OSMapper osMapper, HseRequest hse) throws Exception {
        try {
            Class<CompareResultUtils> cru = CompareResultUtils.class;
            Method method = cru.getMethod(vi[3], ImpactJudgeMethodParam.class);
            //当取出来下标为4的数据为null的时候给一个默认的值，此值随便给不会影响业务，因为如果为null说明方法处理那边没有用到
            //如果方法为判断百分之80几率的方法则传如的double值为默认0
			Boolean result = (Boolean) method.invoke(cru.newInstance(), new ImpactJudgeMethodParam(hse, osMapper, vi));
			log.info("执行判断的方法名称为：{}，执行结果为：{}", vi[3], result);
			return result;
        } catch (Exception e) {
            log.error(vi[2] + "===" + vi[3], e);
            throw e;
        }
    }

    /*************处理变量信息开始*****************/

    /**
     * 类型：1:通过判定减去影响或者直接执行，2:仅仅替换值，3:通过判定减去影响或者替换值
     * viArr{"变量名称","类型","变量的值","判定的方法","此年份的判断区间值"}
     *
     * @param viList
     * @param vName
     * @param answer
     * @param sc
     */
    public Map<String, String> addVariableInfo(OSMapper osMapper, List<String[]> viList,
                                               String vName, String answer, String sc,
                                               String vType, String year, Integer classId) throws Exception{
        // 获取变量信息
        BalanceVariableDO variableInfo = osMapper.getVariableInfo(vName, answer, sc, classId);

        String variableVal = variableInfo.getVariableVal();
        String compareMethod = variableInfo.getCompareMethod();
        String unit = variableInfo.getUnit();

        String variableValUnit = null;
        if (variableVal != null && unit != null) {
            variableValUnit = handleVariableVal(unit, Double.valueOf(variableVal));
        } else if (variableVal != null) {
            variableValUnit = variableVal;
        }

        // 组装变量信息到集合
        viList.add(new String[]{vName, vType, variableValUnit, compareMethod, year});

        Map<String, String> result = new HashMap<>();
        result.put("variableVal", variableVal);
        result.put("unit", unit);
        return result;
    }

    /**
     * 通过单位处理变量的值
     *
     * @param unit
     * @param variableVal
     * @return
     */
    public String handleVariableVal(String unit, Double variableVal) {
        Double vv = variableVal;
        switch (unit) {
            case "%":
                vv = vv / 100;
                break;
        }
        return vv.toString();
    }

    /***********积分开始************/

    /**
     * 处理给学生添加积分
     *
     * @param osMapper
     * @param hseRequest
     * @param checkRes
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void handleAddScore(OSMapper osMapper, HseRequest hseRequest, List<Map<String, Object>> checkRes) throws Exception {
        try {
            if (checkRes == null || checkRes.size() < 1) {
                log.info(MessageConstant.NO_FIND_NEED_ADD_SCORE);
                return;
            }
            else{
                //判断是否为所有角色
                if (NormalConstant.ALL_ROLE_ID.toString().equals(hseRequest.getRoleId())) {
                    //更改为所有角色ID
                    hseRequest.setRoleId(NormalConstant.ALL_ROLE_IDS);
                }

                for (Map<String, Object> crMap : checkRes) {
                    ((AHseService) AopContext.currentProxy()).addScore(osMapper, hseRequest, crMap);
                }
            }

        }catch (Exception e){
            throw  e;
        }
    }

    /**
     * 批量添加积分
     *
     * @param hse
     * @param crMap
     */
    @Transactional(rollbackFor = Exception.class)
    public void addScore(OSMapper osMapper, HseRequest hse, Map<String, Object> crMap) throws Exception {
        try {


        ArrayList<StuScoreDTO> scoreList = new ArrayList<>();
        //以逗号分割的时候需要批量插入
        if (hse.getRoleId().contains(",")) {
            for (String rId : hse.getRoleId().split(",")) {
                hse.setRoleId(rId);
                scoreList.add(new StuScoreDTO(hse, crMap));
            }
        } else { //否则就是单一加入
            scoreList.add(new StuScoreDTO(hse, crMap));
        }

        osMapper.addStuScore(scoreList);
        } catch (Exception e){
            throw e;
        }
    }

    /*********************延迟执行任务**************************/

    /**
     * 添加延迟任务
     *
     * @param osMapper
     * @param scoreDO
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public void addDelayedUpdateTask(OSMapper osMapper, AnswerScoreDO scoreDO, List<String[]> viList) throws Exception{
        try {
            Map<String, Object> task =  ((AHseService) AopContext.currentProxy()).findImpactTask(osMapper, scoreDO);
            if (task == null || task.isEmpty()) {
                log.info("没有需要添加到延迟任务的影响：{}", scoreDO);
                return;
            }
            //获取延迟任务的信息
            List<String[]> impactTask = (List<String[]>) task.get("delayed");
            //不会对任何指标产生影响
            if (impactTask == null || impactTask.isEmpty()) {
                log.info("没有需要添加到延迟任务的影响：{}", scoreDO);
                return;
            }
            List<DelayedUpdateDO> duList = new ArrayList<>();
            for (String[] it : impactTask) {
                duList.add(new DelayedUpdateDO(scoreDO.getTeamId(), scoreDO.getClassId(),
                        it[3] == null ? scoreDO.getTimeLine() : it[3], Integer.valueOf(it[1]), it[0],
                        Integer.valueOf(it[2]), viList == null ? null : JSON.toJSONString(viList), it[4], Integer.valueOf(it[5])));
            }
            osMapper.addBatchDelayedUpdate(duList);
        }catch (Exception e){
            throw e;
        }
    }
    
    /**
     * 执行延迟任务
     *
     * @param osMapper
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public void handleDelayedUpdate(OSMapper osMapper, HseRequest hse, InitTeamIntermediateService itid) throws Exception {
        //检查执行任务所需参数是否正确
        try {
            if (!hse.checkCTTQ()) {
                throw new MissingParametersException(ExceptionCodeEnumConsts.ERROR_DELAYED_UPDATE_HANDLE.getMsg());
            }

            //执行完任务返回的数据
            Map<String, Object> duResMap = null;
            //查询出此团队所有需要执行的延迟任务
            List<DelayedUpdateDO> duList = osMapper.findDelayedUpdateTasks(new DelayedUpdateDO(hse.getTeamId(), hse.getClassId(), hse.getTimeLine(), null));
            //获取销量,单价类型所有需要执行的任务
            List<DelayedUpdateDO> updateTasks = getDelayedTaskByTypes(duList, new Integer[]{NormalConstant.DELAYED_TYPE_XL, NormalConstant.DELAYED_TYPE_DJ});

            //有关于销量单价的延迟任务的才取初始化数据
            if (updateTasks != null && updateTasks.size() > 0) {
                //执行销量单价的延迟任务(并返回最早的开始影响时间)
                log.info("开始处理执行销量单价延迟任务当前时间为：" + System.currentTimeMillis());
                duResMap = ((AHseService) AopContext.currentProxy()).delayedUpdate(osMapper, updateTasks, hse);
                log.info("处理完成执行销量单价延迟任务当前时间为：" + System.currentTimeMillis());
                log.info("执行延迟影响销量单价开始时间：" + duResMap.get("bDate") + "是否影响了数据：" + duResMap.get("isDo"));
                if ((boolean) duResMap.get("isDo")) {
                    //修改销售后单位材料成本和最终销售额
                    initTeamIntermediateService.updateTeamSalesAdjustedUnitMaterialCost( assemblyParameters(hse, duResMap.get("bDate").toString(),
                            DateUtil.getNextOrPrevDate(DateUtil.FORMART_4, hse.getTimeLine(), Calendar.MONTH, 1),
                            duResMap.get("vms") == null ? null : (Set<String>) duResMap.get("vms")));
                }
            }


            //获取材料成本类型所有需要执行的任务
            updateTasks = getDelayedTaskByTypes(duList, new Integer[]{NormalConstant.DELAYED_TYPE_CB});
            //有关于材料成本的延迟任务的才取初始化数据
            if (updateTasks != null && updateTasks.size() > 0) {
                //执行材料成本(并返回最早的开始影响时间)
                log.info("开始处理执行材料成本延迟任务当前时间为：" + System.currentTimeMillis());
                duResMap = ((AHseService) AopContext.currentProxy()).delayedUpdate(osMapper, updateTasks, hse);
                log.info("处理完成执行材料成本延迟任务当前时间为：" + System.currentTimeMillis());
                log.info("执行延迟影响材料成本开始时间：" + duResMap.get("bDate") + "是否影响了数据：" + duResMap.get("isDo"));
                if ((boolean) duResMap.get("isDo")) {
                    // 修改最终材料成本
                    initTeamIntermediateService.updateTeamFinalMaterialCost(assemblyParameters(hse, duResMap.get("bDate").toString(),
                            DateUtil.getNextOrPrevDate(DateUtil.FORMART_4, hse.getTimeLine(), Calendar.MONTH, 1),
                            duResMap.get("vms") == null ? null : (Set<String>) duResMap.get("vms")));
                    // 修改最终营业成本合计
                    initTeamIntermediateService.updateFinalOperatingCostPlus(assemblyParameters(hse, duResMap.get("bDate").toString(),
                            DateUtil.getNextOrPrevDate(DateUtil.FORMART_4, hse.getTimeLine(), Calendar.MONTH, 1),
                            duResMap.get("vms") == null ? null : (Set<String>) duResMap.get("vms")));
                }

            }


            //获取销售费用-售后服务费，管理费用-其他类型所有需要执行的任务
            updateTasks = getDelayedTaskByTypes(duList, new Integer[]{NormalConstant.DELAYED_TYPE_QT, NormalConstant.DELAYED_TYPE_SHFWWF});

            //有关于销售费用-售后服务费，管理费用-其他的延迟任务的才执行任务
            if (updateTasks != null && updateTasks.size() > 0) {
                //执行销售费用-售后服务费，管理费用-其他
                log.info("开始处理执行销售费用-售后服务费，管理费用-其他延迟任务当前时间为：" + System.currentTimeMillis());
                duResMap = ((AHseService) AopContext.currentProxy()).delayedUpdate(osMapper, updateTasks, hse);
                log.info("处理完成执行销售费用-售后服务费，管理费用-其他延迟任务当前时间为：" + System.currentTimeMillis());
                log.info("执行延迟影响销售费用-售后服务费，管理费用-其他开始时间：" + duResMap.get("bDate") + "是否影响了数据：" + duResMap.get("isDo"));
                if ((boolean) duResMap.get("isDo")) {
                    initTeamIntermediateService.updateFinalSalesTotal(assemblyParameters(hse, duResMap.get("bDate").toString(),
                            DateUtil.getNextOrPrevDate(DateUtil.FORMART_4, hse.getTimeLine(), Calendar.MONTH, 1),
                            duResMap.get("vms") == null ? null : (Set<String>) duResMap.get("vms")));
                    initTeamIntermediateService.updateManagementTotal(assemblyParameters(hse, duResMap.get("bDate").toString(),
                            DateUtil.getNextOrPrevDate(DateUtil.FORMART_4, hse.getTimeLine(), Calendar.MONTH, 1),
                            duResMap.get("vms") == null ? null : (Set<String>) duResMap.get("vms")));
                    initTeamIntermediateService.updateFinalManagementTotal(assemblyParameters(hse, duResMap.get("bDate").toString(),
                            DateUtil.getNextOrPrevDate(DateUtil.FORMART_4, hse.getTimeLine(), Calendar.MONTH, 1),
                            duResMap.get("vms") == null ? null : (Set<String>) duResMap.get("vms")));
                }

            }

            //获取风险识别类型延迟所有需要执行的任务
            updateTasks = getDelayedTaskByTypes(duList, new Integer[]{NormalConstant.DELAYED_TYPE_FXSB});
            //有关于风险识别类型延迟的延迟任务的才执行任务
            if (updateTasks != null && updateTasks.size() > 0) {
                //执行风险识别类型延迟(最终资产减值损失-坏账)
                log.info("开始处理执行风险识别类型延迟任务当前时间为：" + System.currentTimeMillis());
                duResMap = ((AHseService) AopContext.currentProxy()).delayedUpdate(osMapper, updateTasks, hse);
                log.info("处理完成执行风险识别类型延迟任务当前时间为：" + System.currentTimeMillis());
                log.info("执行延迟影响风险识别开始时间：" + duResMap.get("bDate") + "是否影响了数据：" + duResMap.get("isDo"));
                if ((boolean) duResMap.get("isDo")) {
                    initTeamIntermediateService.updateFinalAssetsImpairmentLossTotal(assemblyParameters(hse, duResMap.get("bDate").toString(),
                            DateUtil.getNextOrPrevDate(DateUtil.FORMART_4, hse.getTimeLine(), Calendar.MONTH, 1),
                            duResMap.get("vms") == null ? null : (Set<String>) duResMap.get("vms")));
                }
            }
        }catch (Exception e){
            throw e;
        }
    }
    
    /**
     * 执行关于对月度利润表和资产负债表的影响
     * @param osMapper
     * @param hse
     */
    @Transactional(rollbackFor = Exception.class)
    public void handleDelayedUpdate(OSMapper osMapper, HseRequest hse, String det, IBalanceSheetJSService bsService, List<String> imfactDate) throws Exception {
    	try {
            //检查执行任务所需参数是否正确
            if (!hse.checkCTTQ()) {
                throw new MissingParametersException(ExceptionCodeEnumConsts.ERROR_DELAYED_UPDATE_HANDLE.getMsg());
            }
            List<DelayedUpdateDO> updateTasks = null;
            for (int i = 0; i < imfactDate.size(); i++) {
                hse.setTimeLine(imfactDate.get(i));
                //查询出此团队所有需要执行的延迟任务
                updateTasks = osMapper.findDelayedUpdateTasks(new DelayedUpdateDO(hse.getTeamId(), hse.getClassId(), hse.getTimeLine(), det));
                //如果有需要执行的延迟任务才执行
                if (updateTasks != null && updateTasks.size() > 0) {
                    Map<String, Object> map =
                            ((AHseService) AopContext.currentProxy()).delayedUpdate(osMapper, updateTasks, hse);
                    log.info("执行延迟影响资产负债和月度利润开始时间：" + map.get("bDate") + "是否影响了数据：" + map.get("isDo"));
                    if ((boolean) map.get("isDo")) {
                        //不是月度利润的，资产负债表数据影响处理
                        if (!det.equals(NormalConstant.DELAYED_TYPE_CWFY.toString())) {
                            //资产负债表数据数据影响不处理，月度利润表利润表不变。modified 2019/07/05
                            //bsService.updateTeamBalanceSheet(Integer.valueOf(map.get("bDate").toString()), hse.getClassId(), hse.getTeamId());
                        } else {
                            bsService.updateTeamMonthlyProfitStatement(map.get("bDate").toString(), hse.getClassId(), hse.getTeamId());
                        }
                    }
                }
            }
        }catch (Exception e){
    	    throw e;
        }
    }
    
    /**
     * 获取需要类型的延迟任务
     * @param updateTasks
     * @param types
     * @return
     */
    private List<DelayedUpdateDO> getDelayedTaskByTypes(List<DelayedUpdateDO> updateTasks, Integer[] types){
    	List<DelayedUpdateDO> duList = new ArrayList<>();
    	for (DelayedUpdateDO du : updateTasks) {
    		//判断是否数据此次需要的延迟类型，如果是则加入返回结果集
			for (Integer dt : types) {
				if (dt.equals(du.getDelayedType())) {
					duList.add(du);
				}
			}
		}
    	return duList;
    }

    /**
     * 组装处理数据参数
     *
     * @param hse
     * @param bDate
     * @param eDate
     * @param vms
     * @return
     */
    public Map<String, Object> assemblyParameters(HseRequest hse, String bDate, String eDate, Set<String> vms) {
        Map<String, Object> map = new HashMap<>();
        map.put("classId", hse.getClassId());
        map.put("teamId", hse.getTeamId());
        map.put("bDate", bDate);
        map.put("eDate", eDate);
        map.put("vehicleModel", vms);
        return map;
    }

    /**
     * 执行延迟任务更新数据处理(返回开始时间)
     *
     * @param osMapper
     * @param updateTasks
     * @param hse
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> delayedUpdate(OSMapper osMapper, List<DelayedUpdateDO> updateTasks, HseRequest hse) throws Exception {
        try {
            //延迟任务剩余需要执行的次数
            int executionTimes;
            //需要删除的延迟任务
            String needDelIds = "";
            //开始时间（处理数据时需要，需要返回）
            String bDate = null;
            //处理任务时涉及的车型，如果为所有则为null
            Set<String> vmSet = new HashSet<>();
            Map<String, Object> map = null;
            //判断是否为年的延迟影响
            Boolean isYearDe = false;
            //是否执行过此类型影响
            Boolean isDo = false;
            for (DelayedUpdateDO d : updateTasks) {
                //如果为长度为4的话说明为年的影响
                if (d.getBTime().length() == 4) {
                    isYearDe = true;
                } else { //否则为月度的影响
                    isYearDe = false;
                }

                map = ((AHseService) AopContext.currentProxy()).doFinanceImpact(osMapper, JsonUtil.jsonToList(d.getViData(), String[].class), new AnswerScoreDO(d.getBTime()),
                        hse, d.getTaskIds());
                //减去本次的次数得出剩余的次数
                executionTimes = d.getExecutionTimes() - Integer.valueOf(map.get("impactNum").toString());
                //判断需要执行次数是否为0或者小于0，是的话说明最后一次的影响结束了需要进行删除任务,判断执行影响的次数如果为0说明不需要进行此影响了所以此延迟影响可以删除了
                if (executionTimes <= 0 || map.get("impactNum").toString().equals("0")) {
                    needDelIds += d.getId() + ",";
                } else { //否则进行修改执行次数和开始时间
                    osMapper.updateETAndBTime(executionTimes,
                            DateUtil.getNextOrPrevDate(isYearDe ? DateUtil.FORMART_2 : DateUtil.FORMART_4,
                                    hse.getTimeLine(), isYearDe ? Calendar.YEAR : Calendar.MONTH, 1),
                            d.getId());
                }

                //获取到最早的开始时间
                if (!map.get("impactNum").toString().equals("0")) {
                    bDate = DateUtil.getMinDate(bDate, map.get("bDate").toString(), isYearDe ? DateUtil.FORMART_2 : DateUtil.FORMART_4);
                }
                //添加车型
                if (vmSet != null) {
                    if (StringUtils.isNoneBlank(d.getVehicleModel())) {
                        //判断是否包含多个车型
                        if (d.getVehicleModel().indexOf(",") > -1) {
                            for (String vm : d.getVehicleModel().split(",")) {
                                vmSet.add(vm);
                            }
                        } else {
                            vmSet.add(d.getVehicleModel());
                        }
                    } else { // 如果为null说明所有车型，所以直接设置set为null
                        vmSet = null;
                    }
                }

                if (!isDo) {
                    isDo = (Boolean) map.get("isDo");
                }
            }

            //判断如果此变量有值则进行删除任务操作
            if (StringUtils.isNotBlank(needDelIds)) {
                osMapper.deleteDelayedUpdate(needDelIds.substring(0, needDelIds.length() - 1));
            }

            Map<String, Object> resMap = new HashMap<>();
            //最早开始时间
            resMap.put("bDate", bDate);
            //所涉及车型为null说明全部都涉及
            resMap.put("vms", vmSet);
            //
            resMap.put("isDo", isDo);
            return resMap;
        }catch (Exception e){
            throw e;
        }
    }

    /*********************组装战略话参数*************************/
    /**
     * 组装战略初始化数据传参
     *
     * @param hse
     * @param osMapper
     * @return
     */
    public Map<String, String> getSCInitParam(HseRequest hse, OSMapper osMapper, String sc) {
        StringBuffer sb = null;
        //战略地图用的标志
        Boolean flagDT = false;
        //战略数据初始化标志(变为了true说明到了16年末，所以年需要传17)
        Boolean flagSJ = false;
        if (StringUtils.isNotBlank(sc) && sc.equals(NormalConstant.WEY)) {
        	flagDT = true;
		}
        if (StringUtils.isBlank(sc)) {
            //按照车型和时间排序查询出所有车型的选择战略
            List<String> vmsc = osMapper.getAllVMSC(hse.getClassId(), hse.getTeamId());
            //开始进行组装传参格式
            sb = new StringBuffer();
            //如果是十二月的话就使用下一年去开头，如果是其他月就用本年开头
            sb.append((hse.getTimeLine().contains("-12") ? (Integer.valueOf(hse.getTimeLine().substring(0, 4)) + 1)
                    : hse.getTimeLine().substring(0, 4)) + "_");
            for (String vs : vmsc) {
                switch (vs) {
                    case NormalConstant.CYHZL:
                        sb.append(NormalConstant.CYHZL_CN + "_");
                        break;
                    case NormalConstant.DCBZL:
                        sb.append(NormalConstant.DCBZL_CN + "_");
                        break;
                    case NormalConstant.ZJZL:
                        sb.append(NormalConstant.ZJZL_CN + "_");
                        break;
                    case NormalConstant.WGZL:
                        sb.append(NormalConstant.WGZL_CN + "_");
                        break;
                    case NormalConstant.TWEYZL:
                    	//如果为2016年年末的话初始化2017年的
                    	if (hse.getTimeLine().contains(NormalConstant.YEAR_2016)) {
                    		flagSJ = true;
						}
                        sb.append(NormalConstant.TWEYZL_CN + "_");
                        break;
                    case NormalConstant.CWEYZL:
                    	//如果为2016年年末的话初始化2017年的
                    	if (hse.getTimeLine().contains(NormalConstant.YEAR_2016)) {
                    		flagSJ = true;
						}
                        sb.append(NormalConstant.CWEYZL_CN + "_");
                        break;
                }
            }
        }

        Map<String, String> map = new HashMap<>();
        map.put("classId", hse.getClassId().toString());
        map.put("teamId", hse.getTeamId().toString());
        map.put("year", (flagDT && hse.getTimeLine().contains(NormalConstant.YEAR_2016)) || flagSJ ? NormalConstant.YEAR_2017 : hse.getTimeLine().substring(0, 4));
        map.put("strategicSelect", flagDT ? null : StringUtils.isBlank(sc) ? (sb.toString().substring(0, sb.toString().length() - 1)) : sc);
        return map;
    }
    
    /***********初始化研发费用数据************/
    
    /**
     * 初始化下一年的填报研发费用数据
     * @param osMapper
     * @param hse
     * @param itidService
     */
    public void initNextYearRDCost(OSMapper osMapper, HseRequest hse, InitTeamIntermediateService itidService) throws Exception{
    	int attrYear = DateUtil.getDateAttr(DateUtil.strToDate(DateUtil.FORMART_4, hse.getTimeLine()), Calendar.YEAR);
    	int yearN1 = attrYear + 1;
    	int yearN2 = attrYear + 2;
    	Set<String> vms = osMapper.findVMByYear(hse.getClassId(), yearN1);
    	itidService.updateManagementTotal(
    			assemblyParameters(hse, yearN1 + NormalConstant.JANUARY, yearN2 + NormalConstant.JANUARY, vms));
    	itidService.updateFinalManagementTotal(
    			assemblyParameters(hse, yearN1 + NormalConstant.JANUARY, yearN2 + NormalConstant.JANUARY, vms));
    }

}

