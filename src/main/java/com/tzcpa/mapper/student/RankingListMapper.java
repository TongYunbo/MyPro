package com.tzcpa.mapper.student;

import java.util.List;
import java.util.Map;

import com.tzcpa.model.student.SyntheticAbilityDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 排行榜
 * @author WangYao
 * 2019年6月3日
 */
public interface RankingListMapper {

	/**
	 * 盈利能力列表
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> getProfitabilityList(int classId);

	/**
	 * 收入能力列表
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> getEarningPowerList(int classId);

	/**
	 * 资产规模列表
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> getAssetSizeList(@Param(value = "classId") int classId, @Param(value = "year") int year);

	/**
	 * 执行能力列表
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> getExecutiveCapabilityList(int classId);

	/**
	 * CEO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> getCEOScoreList(int classId);

	/**
	 * CMO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> getCMOScoreList(int classId);

	/**
	 * COO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> getCOOScoreList(int classId);
	
	/**
	 * CFO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> getCFOScoreList(int classId);

	/**
	 * CRO得分
	 * @author WangYao
	 * @date 2019年6月3日
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> getCROScoreList(int classId);

	/**
	 * 系统计分卡考核得分累计
	 * @author WangYao
	 * @date 2019年6月5日
	 * @param classId
	 * @return
	 */
	List<SyntheticAbilityDTO> getScorecardTotalScoreList(int classId);

	/**
	 * 团队操作积分累计
	 * @author WangYao
	 * @date 2019年6月5日
	 * @param classId
	 * @return
	 */
	List<SyntheticAbilityDTO> getTeamTotalScoreList(int classId);

	/**
	 * 净利润（加总）
	 * @author WangYao
	 * @date 2019年6月5日
	 * @param classId
	 * @return
	 */
	List<SyntheticAbilityDTO> getRetainedProfitsTotalList(int classId);

	/**
	 * 营业收入（加总）
	 * @author WangYao
	 * @date 2019年6月5日
	 * @param classId
	 * @return
	 */
	List<SyntheticAbilityDTO> getOperationRevenueTotalList(int classId);

	/**
	 * 销售毛利率（平均）
	 * @author WangYao
	 * @date 2019年6月5日
	 * @param classId
	 * @return
	 */
	List<SyntheticAbilityDTO> getGrossProfitMarginTotalList(int classId);

	/**
	 * 总资产（最终报表数）
	 * @author WangYao
	 * @date 2019年6月5日
	 * @param classId
	 * @return
	 */
	List<SyntheticAbilityDTO> getTotalAssetsList(int classId);

	/**
	 * 销量（分三个产品）H6
	 * @author WangYao
	 * @date 2019年6月5日
	 * @param classId
	 * @return
	 */
	List<SyntheticAbilityDTO> getH6TotalSalesVolumeList(int classId);

	/**
	 * 销量（分三个产品）H8
	 * @author WangYao
	 * @date 2019年6月5日
	 * @param classId
	 * @return
	 */
	List<SyntheticAbilityDTO> getH8TotalSalesVolumeList(int classId);

	/**
	 * 销量（分三个产品）WEY
	 * @author WangYao
	 * @date 2019年6月5日
	 * @param classId
	 * @return
	 */
	List<SyntheticAbilityDTO> getWEYTotalSalesVolumeList(int classId);

	/**
	 * 获取得分
	 * @author WangYao
	 * @param classId 
	 * @date 2019年6月6日
	 * @return
	 */
	List<Map<String, Object>> getWeightMap(int classId);

	/**
	 * 净资产收益率（平均）
	 * @author WangYao
	 * @date 2019年6月6日
	 * @param classId
	 * @return
	 */
	List<SyntheticAbilityDTO> getReturnOnEquityScoreList(int classId);

}
