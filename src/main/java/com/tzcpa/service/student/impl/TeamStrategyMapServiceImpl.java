package com.tzcpa.service.student.impl;

import com.alibaba.fastjson.JSON;
import com.tzcpa.mapper.treatment.TeamStrategyMapMapper;
import com.tzcpa.service.student.TeamStrategyMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName TeamStrategyMapServiceImpl
 * @Description
 * @Author hanxf
 * @Date 2019/5/29 10:21
 * @Version 1.0
 **/
@Service(value = "teamStrategyMapService")
@Slf4j
public class TeamStrategyMapServiceImpl implements TeamStrategyMapService {

    @Resource
    private TeamStrategyMapMapper teamStrategyMapMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initTeamStrategyMap(Map<String, String> map) throws Exception{
        log.info("初始化团队战略地图 map={}", JSON.toJSONString(map));
        try {
            int count = teamStrategyMapMapper.initTeamStrategyMap(map);
        log.info("初始化战略地图 返回值 count={}",count);
        }catch (Exception e){
            throw e;
        }
    }
}
