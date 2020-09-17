package com.tzcpa.service.treatment.impl;

import com.tzcpa.mapper.treatment.ClassQuestionOptionMapper;
import com.tzcpa.service.treatment.InitClassQuestionOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName InitClassQuestionOptionServiceImpl
 * @Description
 * @Author hanxf
 * @Date 2019/5/14 16:32
 * @Version 1.0
 **/
@Service("initClassQuestionOptionService")
@Slf4j
public class InitClassQuestionOptionServiceImpl implements InitClassQuestionOptionService {

    @Resource
    private ClassQuestionOptionMapper classQuestionOptionMapper;

    @Async
    @Override
    public void initClassQuestionOption(int classId) {
        log.info("初始化班级题目选项 classId={}",classId);
        int count = classQuestionOptionMapper.initClassQuestionOption(classId);
        log.info("初始化班级题目选项 count={}",count);
    }
}
