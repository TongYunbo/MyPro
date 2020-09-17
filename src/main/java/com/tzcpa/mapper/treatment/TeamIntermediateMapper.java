package com.tzcpa.mapper.treatment;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 团队中间表
 *
 * @Author hanxf
 * @Date 13:45 2019/5/20
 **/
public interface TeamIntermediateMapper {

    /**
     *初始化班级内团队的基准数据
     *
     * @Author hanxf
     * @Date 15:42 2019/5/16
     * @param classId 班级id
     * @param teamId 团队id
     * @return int
    **/
    int initTeamIntermediate(@Param(value="classId")int classId, @Param(value="teamId")int teamId);

    /**
     * 选择自建的情况，h8的1~5月销量设置为0
     *
     * @Author hanxf
     * @Date 15:26 2019/6/27
     * @param
     * @return int
    **/
	List<Integer> selectAdjustedSalesByH8(@Param(value = "map") Map<String,Object> map);
	int updateAdjustedSalesByH8(@Param(value = "list") List<Integer> list);

	/**
	 * 折旧  能耗 人工成本  销量 调整后单位材料成本 材料成本  =》最终营业成本合计
	 *
	 * @Author hanxf
	 * @Date 15:53 2019/6/5
	 * @param
	 * @return int
	**/
	List<Map<String,Object>> selectFinalOperatingCostPlus(@Param(value = "map") Map<String, Object> map);
    int updateFinalOperatingCostPlus(@Param(value = "list") List<Map<String, Object>> list);

    /**
     * 最终宣传推广费  调整后销售费用-售后服务费 最终销售费用-其他 =》最终销售费用合计
     *
     * @Author hanxf
     * @Date 16:15 2019/6/5
     * @param map
     * @return int
    **/
	List<Map<String,Object>> selectFinalSalesTotal(@Param(value = "map") Map<String, Object> map);
    int updateFinalSalesTotal(@Param(value = "list") List<Map<String, Object>> list);

    /**
     * 管理费用-研发费 管理费用-折旧费 管理费用-其他  =》管理费用合计
     *
     * @Author hanxf
     * @Date 16:17 2019/6/5
     * @param map
     * @return int
    **/
    List<Map<String,Object>> selectManagementTotal(@Param(value = "map") Map<String, Object> map);

    int updateManagementTotal(@Param(value = "list") List<Map<String, Object>> list);
//    int updateManagementTotal(@Param(value = "map") Map<String, Object> map);

    /**
     * 填报的研发费用  调整后管理费用-折旧费 调整后管理费用-其他  管理费用-研发费=》最终管理费用合计
     *
     * @Author hanxf
     * @Date 16:43 2019/6/5
     * @param
     * @return int
    **/
	List<Map<String,Object>> selectFinalManagementTotal(@Param(value = "map") Map<String, Object> map);
    int updateFinalManagementTotal(@Param(value = "list") List<Map<String, Object>> list);

    /**
     * 最终资产减值损失-坏账 最终资产减值损失-存货 最终资产减值损失-固定资产 =>最终资产减值损失合计
     *
     * @Author hanxf
     * @Date 17:01 2019/6/5
     * @param
     * @return int
    **/
	List<Map<String,Object>> selectFinalAssetsImpairmentLossTotal(@Param(value = "map") Map<String, Object> map);
    int updateFinalAssetsImpairmentLossTotal(@Param(value = "list") List<Map<String, Object>> list);

	/**
	 * 清空临时表
	 *
	 * @Author hanxf
	 * @Date 10:52 2019/6/4
	 * @return int
	**/
	int deleteTeamIntermediateTempOne(@Param(value = "tableName") String tableName);



	/**
	 * 查询资产减值损失-坏账
	 * @param calNum
	 * @param classId
	 * @param teamId
	 * @param year
	 * @param month
	 * @param vehicleModel
	 * @return
	 */
	Double getLABD(double calNum, Integer classId, Integer teamId, Integer year, Integer month, String vehicleModel);

	/**
     * 按年查询中间表
     *
     * @param map
     * @return
     */
    Map<String, Object> selectTeamIntermediateYearSum(@Param(value = "map")Map<String, Object> map);

    /**
     * 查询调整后销量
     * @param map
     * @return
     */
    Long selectAdjustedSales(@Param(value = "map")Map<String, Object> map);


	/**
	 * @author WangYao
	 * @date 2019年6月10日
	 * @param map
	 * @return
	 */
	int initTeamIntermediateTempSalesUnitPriceOne(@Param(value = "map")Map<String, Object> map);

	/**
	 * 创建临时表
	 * @param tableName
	 * @return
	 */
	int createTempTable(@Param(value = "tableName")String tableName);

	/**
	 * @author WangYao
	 * @date 2019年6月10日
	 */
	void updateTeamIntermediateSalesUnitPriceOne(@Param(value = "tableName")String tableName);

	/**
	 * @author WangYao
	 * @date 2019年6月11日
	 * @param map
	 * @return
	 */
	int initTeamIntermediateTempTwo(@Param(value = "map") Map<String, String> map);

	/**
	 * @author WangYao
	 * @date 2019年6月11日
	 */
	void updateTeamIntermediateTempTwo(@Param(value = "map") Map<String, String> map);

	/**
	 * @author WangYao
	 * @date 2019年6月11日
	 * @param map
	 * @return
	 */
	int updateInitSalesDataByParamThree(@Param(value = "map") Map<String, Object> map);

	/**
	 * @author WangYao
	 * @date 2019年6月11日
	 */
	void updateSalesDataByParamThree(@Param(value = "map") Map<String, Object> map);
	
	/**
	 * 单位材料成本 =>最终材料成本
	 *
	 * @Author hanxf
	 * @Date 19:40 2019/6/18
	 * @param 
	 * @return void
	**/
	List<Map<String,Object>> selectTeamFinalMaterialCostSix(@Param(value = "map") Map<String, Object> map);
	void updateTeamFinalMaterialCostSix(@Param(value = "list") List<Map<String, Object>> list);


	/**
	 * @author WangYao
	 * @date 2019年6月11日
	 * @param map
	 * @return
	 */
	int initInDevelopmentCostTempFour(@Param(value = "map")Map<String, Object> map);

	/**
	 * @author WangYao
	 * @date 2019年6月11日
	 */
	void updateInitInDevelopmentCostFour(@Param(value = "map")Map<String, Object> map);

	/**
	 * @author WangYao
	 * @date 2019年6月11日
	 * @param map
	 * @return
	 */
	int updateInitFinancialCostFive(@Param(value = "map")Map<String, Object> map);

	/**
	 * @author WangYao
	 * @date 2019年6月11日
	 */
	void updateFinancialCostFive(@Param(value = "map")Map<String, Object> map);

	/**
	 * @author WangYao
	 * @date 2019年6月13日
	 * @param tableName
	 */
	void createTempTableTwo(@Param(value = "tableName") String tableName);

	/**
	 * @author WangYao
	 * @date 2019年6月13日
	 * @param tableName
	 */
	void createTempTableThree(@Param(value = "tableName") String tableName);

	/**
	 * @author WangYao
	 * @date 2019年6月13日
	 * @param tableName
	 */
	void createTempTableFour(@Param(value = "tableName") String tableName);

	void createTempTableSix(@Param(value = "tableName") String tableName);

	/**
	 * @author WangYao
	 * @date 2019年6月13日
	 * @param tableName
	 */
	void createTempTableFive(@Param(value = "tableName") String tableName);

	Map<String ,String > getH6Sale(@Param(value = "classId") Integer classId,@Param(value = "teamId") Integer teamId);



}