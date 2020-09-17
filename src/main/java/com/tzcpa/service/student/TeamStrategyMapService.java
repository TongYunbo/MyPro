package com.tzcpa.service.student;

import java.util.Map;

/**
 * @ClassName TeamStrategyMapService
 * @Description 团队战略地图
 * @Author hanxf
 * @Date 2019/5/29 10:21
 * @Version 1.0
 **/
public interface TeamStrategyMapService {

    /**
     * 初始化团队战略地图
     *
     * @Author hanxf
     * @Date 10:22 2019/5/29
     * @param map
     * @return void
     **/
    void initTeamStrategyMap(Map<String,String> map) throws Exception;
}
