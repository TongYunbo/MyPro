package com.tzcpa.service.treatment.impl;

import com.tzcpa.mapper.treatment.ClassDataEffectMapper;
import com.tzcpa.service.treatment.InitDataEffectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName InitDataEffectService
 * @Description 初始化数据影响公式
 * @Author hanxf
 * @Date 2019/5/14 14:02
 * @Version 1.0
 **/
@Service("initDataEffectService")
@Slf4j
public class InitDataEffectServiceImpl implements InitDataEffectService {

    @Resource
    private ClassDataEffectMapper classDataEffectMapper;
    /**
     *初始化数据影响公式
     *
     * @Author hanxf
     * @Date 15:08 2019/5/14
     * @Param [classId]
     * @return void
    **/
    @Async
    @Override
    public void initDataEffect(int classId){
        log.info("初始化班级数据影响公式 classId={}",classId);
        int count = classDataEffectMapper.initClassDataEffect(classId);
        log.info("初始化班级数据影响公式 count={}",count);

    }
}
