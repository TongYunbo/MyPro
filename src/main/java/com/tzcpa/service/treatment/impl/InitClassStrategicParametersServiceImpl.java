package com.tzcpa.service.treatment.impl;

import com.tzcpa.mapper.treatment.ClassStrategicParametersMapper;
import com.tzcpa.service.treatment.InitClassStrategicParametersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName InitClassStrategicParametersServiceImpl
 * @Description
 * @Author hanxf
 * @Date 2019/5/16 17:14
 * @Version 1.0
 **/
@Service("initClassStrategicParametersService")
@Slf4j
public class InitClassStrategicParametersServiceImpl implements InitClassStrategicParametersService {

    @Resource
    private ClassStrategicParametersMapper classStrategicParametersMapper;

    @Override
    public void initClassStrategicParameters(int classId) {
        log.info("初始化班级战略参数 classId={}",classId);
        int count = classStrategicParametersMapper.initClassStrategicParameters(classId);
        log.info("初始化班级战略返回值 count={}",count);
    }
}
