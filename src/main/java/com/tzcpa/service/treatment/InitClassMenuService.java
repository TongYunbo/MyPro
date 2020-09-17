package com.tzcpa.service.treatment;

import java.util.List;

import com.tzcpa.model.treatment.ClassMenuDO;

/**
 * 团队菜单控制
 * @author WangYao
 * 2019年5月28日
 */
public interface InitClassMenuService {

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
	 * @param stuOrTea 
	 * @return
	 */
	List<ClassMenuDO> getClassMenu(int classId, int stuOrTea);

	/**
	 * 修改是否显示排行榜
	 * @author WangYao
	 * @date 2019年5月29日
	 * @param classId
	 * @param isShow
	 * @return
	 */
	boolean updateIsShow(int classId, int isShow);
}
