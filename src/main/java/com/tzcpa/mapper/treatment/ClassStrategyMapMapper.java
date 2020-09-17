package com.tzcpa.mapper.treatment;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClassStrategyMapMapper {

    /**
     * 获取战略地图
     *
     * @Author hanxf
     * @Date 10:54 2019/5/29
     * @param
     * @return java.lang.String
    **/
    String getStrategyMapByYear(@Param(value = "jsonObject") JSONObject jsonObject);

	/**
	 * 初始化班级战略地图
	 * @author WangYao
	 * @date 2019年5月29日
	 * @param classId
	 */
	void initClassStrategyMap(int classId);

    /**
     * 获取战略地图的年份
     *
     * @Author hanxf
     * @Date 10:54 2019/5/29
     * @param
     * @return java.util.List<java.lang.Integer>
    **/
    List<Integer> getStrategyMapYear(@Param(value = "classId") int classId, @Param(value="teamId") int teamId);
}