package com.tzcpa.service.treatment.impl;

import com.tzcpa.mapper.treatment.ClassIntermediateVariableMapper;
import com.tzcpa.service.treatment.InitClassIntermediateVariableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName InitClassIntermediateVariableServiceImpl
 * @Description
 * @Author hanxf
 * @Date 2019/5/16 13:41
 * @Version 1.0
 **/
@Service("initClassIntermediateVariableService")
@Slf4j
public class InitClassIntermediateVariableServiceImpl implements InitClassIntermediateVariableService {

    @Resource
    private ClassIntermediateVariableMapper classIntermediateVariableMapper;

    @Async
    @Override
    public void InitClassIntermediateVariable(int classId) {
        log.info("初始化班级中间变量信息 classId={}",classId);
        int count = classIntermediateVariableMapper.initClassIntermediateVariable(classId);
        log.info("初始化班级中间变量信息 count={}",count);
    }
}
