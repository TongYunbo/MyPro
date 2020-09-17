package com.tzcpa.service.student;

import java.util.HashMap;

/**
 * 雷达图
 * @author WangYao
 * 2019年5月21日
 */
public interface RelativePlotService {

	/**
	 * 根据年限查看所有团队的总分数以及列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param year
	 * @param classId
	 * @return
	 */
	HashMap<String, Object> getRelativePlotList(int year, int classId);

	/**
	 * 查看雷达图和团队信息
	 * @author WangYao
	 * @date 2019年6月14日
	 * @param classId
	 * @param id
	 * @return
	 */
	HashMap<String, Object> getTeamInfo(Integer classId, Integer teamId);

}
