package com.tzcpa.mapper.treatment;

import com.tzcpa.model.treatment.TeamBalanceSheetDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TeamBalanceSheetMapper {

	/**
	 * 初始化团队资产负债表
	 * @author WangYao
	 * @date 2019年5月20日
	 * @param classId 班级id
	 * @param teamId 团队id
	 */
	int initClassBalanceSheet(@Param(value="classId")int classId, @Param(value="teamId")int teamId);


	int insert( TeamBalanceSheetDO teamBalanceSheetDO);

    /**
     * 查询存货
     * @param param
     * @return
     */
    Long getInventory(Map<String, Object> param);

    /**
     * 查询资产总计
     * @param param
     * @return
     */
    Long selectTotalAssets(Map<String, Object> param);

	/**
	 *  获取资产负债表数据源
	 * @return
	 */
	List<TeamBalanceSheetDO> getBalanceSheetSource(@Param("classId") Integer classId,@Param("teamId") Integer teamId,
												   @Param("lastYear") Integer lastYear, @Param("currentMonth") Date currentMonth);

	List<TeamBalanceSheetDO> getBalanceSheetForm(@Param("classId") Integer classId,@Param("teamId") Integer teamId,
												 @Param("lastYear") Integer lastYear,@Param("localYear") Integer localYear );
}