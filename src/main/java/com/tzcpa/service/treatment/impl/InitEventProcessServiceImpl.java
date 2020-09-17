package com.tzcpa.service.treatment.impl;

import com.tzcpa.mapper.treatment.ClassEventFlowMapper;
import com.tzcpa.service.treatment.InitEventProcessService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName InitEventProcessServiceImpl
 * @Description
 * @Author hanxf
 * @Date 2019/5/14 11:33
 * @Version 1.0
 **/
@Service("initEventProcessService")
@Slf4j
public class InitEventProcessServiceImpl implements InitEventProcessService {
    @Resource
    private ClassEventFlowMapper classEventFlowMapper;

    /***
     * @Author hanxf
     * @Description 班级事件流程初始化
     * @Date 14:58 2019/5/6
     * @Param [classId] 班级id
     * @return void
     **/
    @Async
    @Override
    public void initEventProcess(int classId) {
        log.info("班级事件流程初始化 classId={}", classId);
        int count = classEventFlowMapper.initEventFlow(classId);
        log.info("班级事件流程初始化 count={}", count);
    }
}
