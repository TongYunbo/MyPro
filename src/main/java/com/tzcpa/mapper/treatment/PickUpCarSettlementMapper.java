package com.tzcpa.mapper.treatment;

import org.apache.ibatis.annotations.Param;

import com.tzcpa.model.student.HseRequest;

import java.util.Map;

/**
 * 团队中间表
 *
 * @Author lrs
 * @Date 13:45 2019/6/20
 **/
public interface PickUpCarSettlementMapper {
    
	/**
     * 按年查询中间表
     *
     * @param map
     * @return
     */
    Map<String, Object> selectTeamIntermediateYearSum(@Param(value = "map")Map<String, Object> map);
    /**
     * 查询上年赊销收现
     *
     * @param 
     * @return Double
     */
	Double getSNSX();
	/**
     * 查询销售当年收现比例
     *
     * @param 
     * @return Double
     */
	Double getSXBL();
	/**
     * 查询产品增加数
     *
     * @param 
     * @return Double
     */
	Double getCpNum();
	/**
     * 查询发动机采购单价
     *
     * @param 
     * @return Double
     */
	Double getCGdj();
	/**
     * 查询变速箱采购单价
     *
     * @param 
     * @return Double
     */
	Double getCGDJBSX();
	/**
     * 查询其他部件采购单价
     *
     * @param 
     * @return Double
     */
	Double getCGDJOther();
	/**
     * 查询前期赊销付现
     *
     * @param 
     * @return Double
     */
	Double getQJSXFX();
	/**
     * 查询采购材料当年付现比例
     *
     * @param 
     * @return Double
     */
	Double getFXBL();
	
	/**
	 * 查询期初皮卡事业部现金结存
	 * @param classId
	 * @param questionId
	 * @return
	 */
	String getDesc(@Param("classId")Integer classId, @Param("questionId")Integer questionId);
	/**
     * 资产负债表值影响计算
     *
     * @param 
     * @return
     */
	void updateTeamBalanceSheet(@Param("year")Integer year,@Param("classId")Integer classId,@Param("teamId")Integer teamId);
	/**
     * 资产负债表流动负债合计影响计算
     *
     * @param 
     * @return
     */
	void updateTeamBalanceLDFZHJSheet(@Param("year")Integer year,@Param("classId")Integer classId,@Param("teamId")Integer teamId);
	/**
     * 资产负债表负债合计影响计算
     *
     * @param 
     * @return
     */
	void updateTeamBalanceFZHJSheet(@Param("year")Integer year,@Param("classId")Integer classId,@Param("teamId")Integer teamId);
	/**
     * 资产负债表负债和所有者权益计算
     *
     * @param 
     * @return
     */
	void updateTeamBalanceFZSYZHJSheet(@Param("year")Integer year,@Param("classId")Integer classId,@Param("teamId")Integer teamId);
	/**
     * 月度利润表营业总成本
     *
     * @param 
     * @return
     */
	void updateTeamMonthlyProfitStatementYYZCB(@Param("ymDate")String ymDate,@Param("classId")Integer classId,@Param("teamId")Integer teamId);
	/**
     * 月度利润表营业利润
     *
     * @param 
     * @return
     */
	void updateTeamMonthlyProfitStatementYYLR(@Param("ymDate")String ymDate,@Param("classId")Integer classId,@Param("teamId")Integer teamId);
	/**
     * 月度利润表营业利润总额
     *
     * @param 
     * @return
     */
	void updateTeamMonthlyProfitStatementYYZE(@Param("ymDate")String ymDate,@Param("classId")Integer classId,@Param("teamId")Integer teamId);
	/**
     * 月度利润表净利润
     *
     * @param 
     * @return
     */
	void updateTeamMonthlyProfitStatementJLR(@Param("ymDate")String ymDate,@Param("classId")Integer classId,@Param("teamId")Integer teamId);
	/**
     * 月度利润表未分配利润增减变动金额
     *
     * @param 
     * @return
     */
	void updateTeamMonthlyProfitStatementBDJE(@Param("ymDate")String ymDate,@Param("classId")Integer classId,@Param("teamId")Integer teamId);
	/**
     * 月度利润表本年年末未分配利润 91
     *
     * @param 
     * @return
     */
	void updateTeamMonthlyProfitStatementWFPLR(@Param("ymDate")String ymDate,@Param("classId")Integer classId,@Param("teamId")Integer teamId);
	/**
     * 资产负债表非流动负债计算
     *
     * @param 
     * @return
     */
	void updateTeamBalanceFLDFZJSheet(@Param("year")Integer year,@Param("classId")Integer classId,@Param("teamId")Integer teamId);
	/**
     * 资产负债表所有者权益合计计算
     *
     * @param 
     * @return
     */
	void updateTeamBalanceSYZJSheet(@Param("year")Integer year,@Param("classId")Integer classId,@Param("teamId")Integer teamId);
	
	/**
	 * 查询皮卡销售考核得分
	 * @param hse
	 * @return
	 */
	Double findKHScore(@Param("hse")HseRequest hse);
	
	/**
	 * 所得税费用
	 * @param ymDate
	 * @param classId
	 * @param teamId
	 */
	void updateTeamMonthlyProfitStatementSDSFY(@Param("ymDate")String ymDate,@Param("classId")Integer classId,@Param("teamId")Integer teamId);
}