package com.tzcpa.mapper.treatment;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 团队战略地图
 *
 * @Author hanxf
 * @Date 10:01 2019/5/29
**/
public interface TeamStrategyMapMapper {

    /**
     * 初始化团队战略地图
     *
     * @Author hanxf
     * @Date 10:05 2019/5/29
     * @param map
     * @return int
    **/
    int initTeamStrategyMap(@Param(value = "map") Map<String,String> map);
}