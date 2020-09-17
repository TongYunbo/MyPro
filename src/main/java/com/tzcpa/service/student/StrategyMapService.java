package com.tzcpa.service.student;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @ClassName StrategyMapService
 * @Description
 * @Author hanxf
 * @Date 2019/5/27 16:58
 * @Version 1.0
 **/
public interface StrategyMapService {

    /**
     * 查询战略地图
     *
     * @Author hanxf
     * @Date 14:13 2019/5/28
     * @param
     * @return java.lang.String
    **/
    String getStrategyMapByYear(JSONObject jsonObject);

    /**
     * 查询战略地图的年份
     *
     * @Author hanxf
     * @Date 10:49 2019/5/29
     * @param classId 班级id
     * @param teamId 团队id
     * @return java.util.List<java.lang.Integer>
    **/
    List<Integer> getStrategyMapYear(int classId, int teamId);
}
