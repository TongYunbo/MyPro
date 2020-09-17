package com.tzcpa.service.student.impl;

import com.alibaba.fastjson.JSONObject;
import com.tzcpa.mapper.treatment.ClassStrategyMapMapper;
import com.tzcpa.service.student.StrategyMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName StrategyMapServiceImpl
 * @Description
 * @Author hanxf
 * @Date 2019/5/27 16:58
 * @Version 1.0
 **/
@Service(value = "strategyMapService")
@Slf4j
public class StrategyMapServiceImpl implements StrategyMapService {

    @Resource
    private ClassStrategyMapMapper classStrategyMapMapper;

    @Override
    public String getStrategyMapByYear(JSONObject jsonObject) {
        log.info("获取根据年份战略地图 jsonObject={}",jsonObject);
        String url = classStrategyMapMapper.getStrategyMapByYear(jsonObject);
        log.info("获取根据年份战略地图 返回值 url={}",url);
        return url;
    }

    @Override
    public List<Integer> getStrategyMapYear(int classId, int teamId) {
        return classStrategyMapMapper.getStrategyMapYear(classId,teamId);
    }
}
