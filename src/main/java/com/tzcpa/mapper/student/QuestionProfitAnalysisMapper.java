package com.tzcpa.mapper.student;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * <p>Description: </p>
 * @author WTL
 * @date 2019年7月4日
 */
public interface QuestionProfitAnalysisMapper {

	/**
	 * 获取月度利润表的数据
	 * @param year
	 * @param month
	 * @param teamId
	 * @param classId
	 * @return
	 */
	Map<String, BigDecimal> getMonthPData(@Param("year") Integer year,
			@Param("teamId") Integer teamId, @Param("classId") Integer classId);

	/**
	 * 获取中间表的数据
	 * @param year
	 * @param month
	 * @param teamId
	 * @param classId
	 * @return
	 */
	Map<String, BigDecimal> getTIData(@Param("year") Integer year,
			@Param("teamId") Integer teamId, @Param("classId") Integer classId);
}

