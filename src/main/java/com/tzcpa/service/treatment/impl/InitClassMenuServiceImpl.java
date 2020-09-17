package com.tzcpa.service.treatment.impl;

import com.tzcpa.constant.TeacherInfoConstant;
import com.tzcpa.mapper.treatment.ClassMenuMapper;
import com.tzcpa.model.treatment.ClassMenuDO;
import com.tzcpa.service.treatment.InitClassMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 团队菜单控制
 * @author WangYao
 * 2019年5月28日
 */
@Service
@Slf4j
public class InitClassMenuServiceImpl implements InitClassMenuService{

	@Resource
	private ClassMenuMapper classMenuMapper;

	/**
	 * 初始化班级菜单
	 * @author WangYao
	 * @date 2019年5月28日
	 * @param classId
	 */
	@Async
	@Override
	public void initClassMenu(int classId) {
		log.info("初始化班级菜单 classId={}", classId);
		classMenuMapper.initClassMenu(classId);
	}
	
	/**
	 * 查询菜单列表
	 * @author WangYao
	 * @date 2019年5月28日
	 * @param classId
	 * @param stuOrTea
	 * @return
	 */
	@Override
	public List<ClassMenuDO> getClassMenu(int classId, int stuOrTea) {
		log.info("查询菜单列表 classId={}, stuOrTea={}", classId, stuOrTea);
		List<ClassMenuDO> list = new ArrayList<>();
		//stuOrTea为1，是老师，只查询该班级的排行榜信息
		if(stuOrTea == TeacherInfoConstant.isTea){
			list = classMenuMapper.getTeacherMenu(classId);
		}
		//stuOrTea为0 ，是学生
		else if(stuOrTea == TeacherInfoConstant.isStu){
			list = classMenuMapper.getClassMenu(classId);
		}
		log.info("查询菜单列表 list={}", list);
		return list;
	}

	/**
	 * 修改是否显示排行榜
	 * @author WangYao
	 * @date 2019年5月29日
	 * @param classId
	 * @param isShow
	 * @return
	 */
	@Override
	public boolean updateIsShow(int classId, int isShow) {
		log.info("修改是否显示排行榜  classId={}, isShow={}", classId, isShow);
		Map<String, Object> map = new HashMap<>();
			map.put("classId", classId);
			map.put("isShow", isShow);
			classMenuMapper.updateIsShow(map);
			return true;
		}
}
