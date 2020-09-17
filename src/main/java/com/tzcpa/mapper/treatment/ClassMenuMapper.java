package com.tzcpa.mapper.treatment;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tzcpa.model.treatment.ClassMenuDO;

/**
 * 班级菜单
 *
 * @Author hanxf
 * @Date 16:25 2019/5/28
**/
public interface ClassMenuMapper {

	/**
	 * 初始化班级菜单
	 * @author WangYao
	 * @date 2019年5月28日
	 * @param classId
	 */
	void initClassMenu(int classId);

	/**
	 * 查询菜单列表
	 * @author WangYao
	 * @date 2019年5月28日
	 * @param classId
	 * @return
	 */
	List<ClassMenuDO> getClassMenu(int classId);

	/**
	 * 修改是否显示排行榜
	 * @author WangYao
	 * @date 2019年5月29日
	 * @param map
	 */
	void updateIsShow(@Param("map")Map<String, Object> map);

	/**
	 * 查询该班级的排行榜信息
	 * @author WangYao
	 * @date 2019年5月29日
	 * @param classId
	 * @return
	 */
	List<ClassMenuDO> getTeacherMenu(int classId);
}