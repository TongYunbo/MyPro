package com.tzcpa.service.teacher;

import java.util.Map;

/**
 * 学生成绩单列表
 * @author WangYao
 * 2019年5月27日
 */
public interface ReportCardService {

	/**
	 * 查看学生成绩单列表
	 * @author WangYao
	 * @date 2019年5月27日
	 * @param classId
	 * @return
	 */
	Map<String, Object> getReportCardList(int classId);

}
