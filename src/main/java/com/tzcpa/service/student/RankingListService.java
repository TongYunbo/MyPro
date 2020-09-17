package com.tzcpa.service.student;

import java.util.List;
import java.util.Map;

/**
 * 排行榜
 * @author WangYao
 * 2019年6月3日
 */
public interface RankingListService {

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
	List<Map<String, Object>> getAssetSizeList(int classId);

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
	 * 综合能力排名列表
	 * @author WangYao
	 * @date 2019年6月4日
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> getSyntheticAbility(int classId);

}
