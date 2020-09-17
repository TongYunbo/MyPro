package com.tzcpa.service.treatment;

import com.tzcpa.mapper.treatment.OSMapper;
import com.tzcpa.model.student.HseRequest;

import java.util.Map;

/**
 * @ClassName InitTeamIntermediateService
 * @Description 初始化团队中间表信息
 * @Author hanxf
 * @Date 2019/5/16 16:16
 * @Version 1.0
 **/
public interface InitTeamIntermediateService {


    /**
     * 班级中的团队中间表基准数据初始化
     *
     * @Author hanxf
     * @Date 11:14 2019/5/16
     * @param classId 班级id
     * @param teamId  团队id
     * @return void
     **/
    void initTeamIntermediate(int classId, int teamId) throws Exception;


    /**
     *
     * 选择自建的情况，h8的1~5月销量设置为0
     * @Author hanxf
     * @Date 15:22 2019/6/27
     * @param
     * @return void
    **/
    void updateAdjustedSalesByH8(Map<String,Object> map) throws Exception;
    void updatePackagingSales(Map<String,Object> map) throws Exception;

    /**
     * 初始化团队战略基准数据
     *
     * @Author hanxf
     * @Date 16:24 2019/5/16
     * @param map 班级id
     * @return void
    **/
    void initTeamIntermediateOfStrategic(Map<String,String> map) throws Exception;

    /**
     * 销量调后-单位材料成本
     *
     * @Author hanxf
     * @Date 18:40 2019/5/17
     * @param map 参数集合
     * @return int
    **/
    void updateTeamSalesAdjustedUnitMaterialCost(Map<String,Object> map) throws Exception;

    /**
     * 单位材料成本修改 =>最终材料成本
     *
     * @Author hanxf
     * @Date 19:34 2019/6/18
     * @param map
     * @return void
    **/
    void updateTeamFinalMaterialCost(Map<String, Object> map) throws Exception;
    /**
     * 修改最终销售额和最终材料成本
     * @author WangYao
     * @date 2019年5月24日
     * @param map
     */
    void updateFinalOperatingCostPlus(Map<String, Object> map) throws Exception;


    /**
     * 修改销量和单价
     *
     * @Author hanxf
     * @Date 14:08 2019/6/4
     * @param map
     * @return void
    **/
    void updateTeamIntermediateSalesUnitPrice(Map<String, Object> map) throws Exception;


    /**
     * 管理费用-研发费 管理费用-折旧费 管理费用-其他 =》管理费用合计
     *
     * @Author hanxf
     * @Date 16:53 2019/6/5
     * @param
     * @return void
    **/
    void updateManagementTotal(Map<String, Object> map) throws Exception;

    /**
     * 填报的研发费用  调整后管理费用-折旧费 调整后管理费用-其他  管理费用-研发费=》最终管理费用合计
     *
     * @Author hanxf
     * @Date 16:53 2019/6/5
     * @param
     * @return void
    **/
    void updateFinalManagementTotal(Map<String, Object> map) throws Exception;

    /**
     * 最终资产减值损失-坏账 最终资产减值损失-存货 最终资产减值损失-固定资产 =>最终资产减值损失合计
     *
     * @Author hanxf
     * @Date 17:00 2019/6/5
     * @param
     * @return
    **/
    void updateFinalAssetsImpairmentLossTotal(Map<String, Object> map) throws Exception;

    /**
     * 填报的研发费用
     *
     * @Author hanxf
     * @Date 17:49 2019/6/5
     * @param
     * @return void
    **/
    void updateInitInDevelopmentCost(Map<String, Object> map) throws Exception;


    /**
     * 最终宣传推广费  调整后销售费用-售后服务费 最终销售费用-其他 =》最终销售费用合计
     *
     * @Author hanxf
     * @Date 20:02 2019/6/5
     * @param
     * @return void
    **/
    void updateFinalSalesTotal(Map<String, Object> map) throws Exception;


    void updateInitFinancialCost(Map<String,Object> map) throws Exception;


    void initNextYearRDCost(OSMapper osMapper, HseRequest hse, InitTeamIntermediateService itidService) throws Exception;
}
