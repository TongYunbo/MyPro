package com.tzcpa.mapper.student;

import java.util.List;
import java.util.Map;

import com.tzcpa.model.treatment.StuScore;

/**
 * @author WangYao
 * 2019年5月21日
 */
public interface RelativePlotMapper {

	/**
	 * 查询雷达图列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param stuScore
	 * @return
	 */
	List<StuScore> selectList(StuScore stuScore);

	/**
	 * 查询总分
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param stuScore
	 * @return
	 */
	Map<String, Object> selectTotalScore(StuScore stuScore);

	/**
	 * 查询团队信息
	 * @author WangYao
	 * @date 2019年6月14日
	 * @param stuScore
	 * @return
	 */
	Map<String, Object> getTeamInfo(StuScore stuScore);

}
