package com.tzcpa.service.teacher.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tzcpa.mapper.teacher.ManageWhetherBeginMapper;
import com.tzcpa.service.teacher.ManageWhetherBeginService;

import lombok.extern.slf4j.Slf4j;

/**
 * 经营是否开始
 * @author WangYao
 * 2019年6月17日
 */
@Service
@Slf4j
public class ManageWhetherBeginServiceImpl implements ManageWhetherBeginService{

	@Resource
	private ManageWhetherBeginMapper manageWhetherBeginMapper;

	/**
	 * 判断经营是否开始
	 * @author WangYao
	 * @date 2019年6月17日
	 * @param classId
	 * @return
	 */
	@Override
	public Integer getManageWhetherBegin(int classId) {
		log.info("判断经营是否开始  classId={}", classId);
		return manageWhetherBeginMapper.getManageWhetherBegin(classId);
	}
}
