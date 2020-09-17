package com.tzcpa.mapper.student;

import com.tzcpa.model.teacher.RoleQuestionScore;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 学生成绩单列表
 * @author WangYao
 * 2019年5月27日
 */
public interface ReportCardMapper {

	/**
	 * 查看标题
	 * @author WangYao
	 * @date 2019年5月29日
	 * @param classId
	 * @return
	 */
	List<Map<String, Object>> getTitle(int classId);

	/**
	 * 角色得分
	 *
	 * @Author hanxf
	 * @Date 16:13 2019/5/29
	 * @param classId 班级ID
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	**/
	List<RoleQuestionScore> getRoleScoreList(@Param(value = "classId") int classId);

	/**
	 * 查询学生成绩单 题目列表
	 * @author WangYao
	 * @date 2019年5月31日
	 * @param classId
	 * @return
	 */
	List<RoleQuestionScore> getQuestionScoreList(@Param(value = "classId") int classId);

	/**
	 * 查看每个团队的总分
	 * @author WangYao
	 * @date 2019年5月31日
	 * @param classId
	 * @return
	 */
	List<RoleQuestionScore> getTotalScore(int classId);

}
