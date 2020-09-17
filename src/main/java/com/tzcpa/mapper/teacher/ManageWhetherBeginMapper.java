package com.tzcpa.mapper.teacher;

import org.apache.ibatis.annotations.Param;

/**
 * @author WangYao
 * 2019年6月17日
 */
public interface ManageWhetherBeginMapper {

	/**
	 * 判断经营是否开始
	 * @author WangYao
	 * @date 2019年6月17日
	 * @param classId
	 * @param teamId
	 * @return
	 */
	Integer getManageWhetherBegin(@Param(value = "classId")int classId);

}
