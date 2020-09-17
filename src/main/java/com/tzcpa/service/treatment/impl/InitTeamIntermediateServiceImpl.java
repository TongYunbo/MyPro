package com.tzcpa.service.treatment.impl;

import com.alibaba.fastjson.JSON;
import com.tzcpa.constant.NormalConstant;
import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.mapper.treatment.TeamIntermediateMapper;
import com.tzcpa.model.student.HseRequest;
import com.tzcpa.service.treatment.InitTeamIntermediateService;
import com.tzcpa.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName InitTeamIntermediateService
 * @Description 初始化团队中间表信息
 * @Author hanxf
 * @Date 2019/5/16 16:16
 * @Version 1.0
 **/
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class InitTeamIntermediateServiceImpl implements InitTeamIntermediateService {

    @Resource
    private TeamIntermediateMapper teamIntermediateMapper;

    /**
     * 创建团队时调用
     *
     * @Author hanxf
     * @Date 10:02 2019/6/5
     * @param
     * @return void
    **/
    @Async
    @Override
    public void initTeamIntermediate(int classId, int teamId) throws Exception{
        log.info("初始化班级内团队的基准数据 classId={},teamId={}",classId,teamId);
        int count = teamIntermediateMapper.initTeamIntermediate(classId, teamId);
        log.info("初始化班级内团队的基准数据 count={}",count);

    }

    @Override
    public void updateAdjustedSalesByH8(Map<String,Object> map) throws Exception{
        log.info("选择自建的情况，h8的1~5月销量设置为0 map={}",map);
        List<Integer> resultMap = teamIntermediateMapper.selectAdjustedSalesByH8(map);
        if(resultMap.size()>0){
            int count = teamIntermediateMapper.updateAdjustedSalesByH8(resultMap);
            log.info("选择自建的情况，h8的1~5月销量设置为0 count={}",count);
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePackagingSales(Map<String,Object> map) throws Exception{
        log.info("updatePackagingSales map={}",JSON.toJSONString(map));
        try {
            ((InitTeamIntermediateService) AopContext.currentProxy()).updateTeamIntermediateSalesUnitPrice(map);
        }catch (Exception e){
            throw e;
        }
        //改为自建影响 wtl 2019-06-28
//        String timeLine = String.valueOf(map.get("timeLine"));
//        String time[] = timeLine.split("-");
//        if(time.length<2){
//            return;
//        }
//        int year = Integer.valueOf(time[0]);
//        int month = Integer.valueOf(time[1]);
//        map.put("year",year);
//        map.put("month",month);
//        if(year == 2015 && (month>=1 && month<=5)){
//            this.updateAdjustedSalesByH8(map);
//        }
    }

    /**
     * 团队的战略基准值  年初或者做了战略基准的题目后调用
     *
     * @Author hanxf
     * @Date 10:01 2019/6/5
     * @param
     * @return void
    **/
//    @Async
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initTeamIntermediateOfStrategic(Map<String,String> map) throws Exception{
        log.info("插入团队战略基准值 map={}",map);
        //创建临时表
        String tableName = "t_temp_two_"+map.get("teamId");
        try {
            teamIntermediateMapper.createTempTableTwo(tableName);
            map.put("tableName",tableName);
            int count = teamIntermediateMapper.initTeamIntermediateTempTwo(map);
            if(count>0){
                log.info("修改团队的战略基准值");
                teamIntermediateMapper.updateTeamIntermediateTempTwo(map);
            }
        }catch (Exception e){
            throw e;
        }finally {
            log.info("清除临时表");
            teamIntermediateMapper.deleteTeamIntermediateTempOne(tableName);
        }

    }


    /**
     * 有销量、价格 影响的情况下调用
     *
     * 销售额 销售费用-售后服务费(最终销售额) 管理费用-其他（最终销售额）  调整后单位材料成本 最终材料成本 最终营业税金及附加  最终能耗成本
     *
     * @Author hanxf
     * @Date 10:06 2019/6/5
     * @param 
     * @return void
    **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTeamSalesAdjustedUnitMaterialCost(Map<String, Object> map) throws Exception{
        log.info("销量调后-数据修改 init map={}",map);
        //创建临时表
        String tableName = "t_temp_three_"+map.get("teamId");
        try {
            teamIntermediateMapper.createTempTableThree(tableName);
            map.put("tableName",tableName);
            int count = teamIntermediateMapper.updateInitSalesDataByParamThree(map);
            if(count>0){
                log.info("销量调后-数据修改 update");
                teamIntermediateMapper.updateSalesDataByParamThree(map);
            }
        }catch (Exception e){
            throw e;
        }finally {
            log.info("清除临时表");
            teamIntermediateMapper.deleteTeamIntermediateTempOne(tableName);
        }
    }



    /**
     * 单位材料成本修改 =>最终材料成本
     *
     * @Author hanxf
     * @Date 19:36 2019/6/18
     * @param 
     * @return void
    **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTeamFinalMaterialCost(Map<String, Object> map) throws Exception{
        log.info("单位材料成本-数据修改 init map={}",map);
        try {
            List<Map<String, Object>> resultMap = teamIntermediateMapper.selectTeamFinalMaterialCostSix(map);
            if (resultMap.size() > 0) {
                teamIntermediateMapper.updateTeamFinalMaterialCostSix(resultMap);
            }

        }catch (Exception e){
            throw e;
        }
        //创建临时表
//        String tableName = "t_temp_six_"+map.get("teamId");
//        teamIntermediateMapper.createTempTableSix(tableName);
//        map.put("tableName",tableName);
//        int count = teamIntermediateMapper.updateTeamFinalMaterialCostSix(map);
//        if(count>0){
//            log.info("单位材料成本-数据修改 update");
//            teamIntermediateMapper.updateSalesDataByParamThree(map);
//        }
//        log.info("清除临时表");
//        teamIntermediateMapper.deleteTeamIntermediateTempOne(tableName);
    }



    /**
     * 最终生产折旧  最终能耗成本 最终生产人工成本  调整后销量 调整后单位材料成本 最终材料成本  =》最终营业成本合计
     * @author WangYao
     * @date 2019年5月24日
     * @param map
     */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public void updateFinalOperatingCostPlus(Map<String, Object> map) throws Exception{

		log.info("修改最终营业成本合计 map={}",JSON.toJSONString(map));
        try {
		List<Map<String,Object>> resultMap = teamIntermediateMapper.selectFinalOperatingCostPlus(map);
        log.info("修改最终营业成本合计 resultMap={}",JSON.toJSONString(resultMap));
        if (resultMap.size()>0) {
            int count = teamIntermediateMapper.updateFinalOperatingCostPlus(resultMap);
            log.info("修改最终营业成本合计 count={}",count);
        }

        }catch (Exception e){
		    throw e;
        }

	}


	/**
	 * 最终宣传推广费  调整后销售费用-售后服务费 最终销售费用-其他 =》最终销售费用合计
	 *
	 * @Author hanxf
	 * @Date 16:08 2019/6/5
	 * @param map
	 * @return void
	**/
	@Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFinalSalesTotal(Map<String, Object> map) throws Exception{
        log.info("最终销售费用合计 map={}",JSON.toJSONString(map));
        try {
        List<Map<String,Object>> resultMap = teamIntermediateMapper.selectFinalSalesTotal(map);
        log.info("最终销售费用合计 resultMap={}",JSON.toJSONString(resultMap));
        if (resultMap.size()>0) {
            int count = teamIntermediateMapper.updateFinalSalesTotal(resultMap);
            log.info("最终销售费用合计 count={}",count);
        }
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 管理费用-研发费 调整后管理费用-研发费 管理费用-折旧费 管理费用-其他 =》管理费用合计
     *
     * @Author hanxf
     * @Date 16:16 2019/6/5
     * @param  map
     * @return void
    **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateManagementTotal(Map<String, Object> map) throws Exception{
        log.info("修改管理费用合计 map={}",JSON.toJSONString(map));
        try {
            List<Map<String, Object>> resultMap = teamIntermediateMapper.selectManagementTotal(map);
            log.info("修改管理费用合计 resultMap={}", JSON.toJSONString(resultMap));
            if (resultMap.size() > 0) {
                int count = teamIntermediateMapper.updateManagementTotal(resultMap);
                log.info("修改管理费用合计 count={}", count);
            }
        }catch (Exception e){
            throw e;
        }
    }

    /**
     *填报的研发费用  调整后管理费用-折旧费 调整后管理费用-其他  管理费用-研发费 调整后管理费用-研发费=》最终管理费用合计
     *
     * @Author hanxf
     * @Date 16:42 2019/6/5
     * @param
     * @return void
    **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFinalManagementTotal(Map<String, Object> map) throws Exception{
        log.info("修改最终管理费用合计 map={}",JSON.toJSONString(map));
        try {
            List<Map<String, Object>> resultMap = teamIntermediateMapper.selectFinalManagementTotal(map);
            log.info("修改最终管理费用合计 resultMap={}", JSON.toJSONString(resultMap));
            if (resultMap.size() > 0) {
                int count = teamIntermediateMapper.updateFinalManagementTotal(resultMap);
                log.info("修改最终管理费用合计 count={}", count);
            }
        }catch (Exception e){
            throw e;
        }

    }

    /**
     * 最终资产减值损失-坏账 最终资产减值损失-存货 最终资产减值损失-固定资产 =>最终资产减值损失合计
     *
     * @Author hanxf
     * @Date 17:01 2019/6/5
     * @param
     * @return void
    **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFinalAssetsImpairmentLossTotal(Map<String, Object> map) throws Exception{
        log.info("修改最终资产减值损失合计 map={}",JSON.toJSONString(map));
        try {
        List<Map<String,Object>> resultMap = teamIntermediateMapper.selectFinalAssetsImpairmentLossTotal(map);
        log.info("修改最终资产减值损失合计 resultMap={}",JSON.toJSONString(resultMap));
        if (resultMap.size()>0) {
            int count = teamIntermediateMapper.updateFinalAssetsImpairmentLossTotal(resultMap);
            log.info("修改最终资产减值损失合计 count={}",count);
        }
        }catch (Exception e){
            throw  e;
        }
    }


    /**
     * 填报的研发费用
     *
     * @Author hanxf
     * @Date 17:16 2019/6/5
     * @param
     * @return void
    **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInitInDevelopmentCost(Map<String, Object> map) throws Exception{
        log.info("修改填报的研发费用 map={}",JSON.toJSONString(map));
        //创建临时表
        String tableName = "t_temp_four_"+map.get("teamId");
        try {
            teamIntermediateMapper.createTempTableFour(tableName);
            map.put("tableName",tableName);
            int count = teamIntermediateMapper.initInDevelopmentCostTempFour(map);
            if(count > 0){
                teamIntermediateMapper.updateInitInDevelopmentCostFour(map);
            }
        }catch (Exception e){
            log.error("updateInitInDevelopmentCost不可回滚",e);
        }finally {
            log.info("清除临时表");
            teamIntermediateMapper.deleteTeamIntermediateTempOne(tableName);
        }


    }

    /**
     * 初始化财务费用  每年年初调用
     *
     * @Author hanxf
     * @Date 19:51 2019/6/5
     * @param
     * @return void
    **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInitFinancialCost(Map<String, Object> map) throws Exception{
        log.info("初始化财务费用 map={}",JSON.toJSONString(map));
        //创建临时表
        String tableName = "t_temp_five_"+map.get("teamId");
        try{
            teamIntermediateMapper.createTempTableFive(tableName);
            map.put("tableName",tableName);
            int count = teamIntermediateMapper.updateInitFinancialCostFive(map);
            if(count > 0){
                teamIntermediateMapper.updateFinancialCostFive(map);
            }
        }catch (Exception e){
            throw e;
        }finally {
            log.info("清除临时表");
            teamIntermediateMapper.deleteTeamIntermediateTempOne(tableName);
        }

    }


	/**
	 * 初始化  调整后销量 调整后单价 最终销售额 最终能耗成本 调整后单位材料成本  销售费用-售后服务费
     * 最终售后服务费 管理费用-其他 调整后管理费用-其他 月底最初执行 1
	 *
	 * @Author hanxf
	 * @Date 10:00 2019/6/5
	 * @param
	 * @return void
	**/
    @Override
    public void updateTeamIntermediateSalesUnitPrice(Map<String, Object> map) throws Exception{
        log.info("初始化销量和单价材料成本等 map={}",JSON.toJSONString(map));
        try {
            String timeLine = String.valueOf(map.get("timeLine"));
            String time[] = timeLine.split("-");
            if (time.length < 2) {
                return;
            }
            int year = Integer.valueOf(time[0]);
            int month = Integer.valueOf(time[1]);
            map.put("year", year);
            map.put("month", month);
            log.info("初始化销量和单价材料成本等 updateMap={}", JSON.toJSONString(map));
            //创建临时表
            String tableName = "t_temp_one_" + map.get("teamId");
            try {
                teamIntermediateMapper.createTempTable(tableName);
                map.put("tableName", tableName);
                int count = teamIntermediateMapper.initTeamIntermediateTempSalesUnitPriceOne(map);
                if (count > 0) {
                    teamIntermediateMapper.updateTeamIntermediateSalesUnitPriceOne(tableName);
                }
            } finally {
                log.info("清除临时表");
                teamIntermediateMapper.deleteTeamIntermediateTempOne(tableName);
            }
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initNextYearRDCost(OSMapper osMapper, HseRequest hse, InitTeamIntermediateService itidService) throws Exception{
        try {
            int attrYear = DateUtil.getDateAttr(DateUtil.strToDate(DateUtil.FORMART_4, hse.getTimeLine()), Calendar.YEAR);
            int yearN1 = attrYear + 1;
            int yearN2 = attrYear + 2;
            Set<String> vms = osMapper.findVMByYear(hse.getClassId(), yearN1);
            itidService.updateManagementTotal(
                    assemblyParameters(hse, yearN1 + NormalConstant.JANUARY, yearN2 + NormalConstant.JANUARY, vms));
            itidService.updateFinalManagementTotal(
                    assemblyParameters(hse, yearN1 + NormalConstant.JANUARY, yearN2 + NormalConstant.JANUARY, vms));
        }catch (Exception e){
            throw e;
        }
    }

    public Map<String, Object> assemblyParameters(HseRequest hse, String bDate, String eDate, Set<String> vms) {
        Map<String, Object> map = new HashMap<>();
        map.put("classId", hse.getClassId());
        map.put("teamId", hse.getTeamId());
        map.put("bDate", bDate);
        map.put("eDate", eDate);
        map.put("vehicleModel", vms);
        return map;
    }

}



