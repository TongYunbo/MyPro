package com.tzcpa.service.treatment.impl;

import com.tzcpa.mapper.treatment.ClassAnswerImfactMapper;
import com.tzcpa.mapper.treatment.ClassAnswerScoreMapper;
import com.tzcpa.service.treatment.InitClassAnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName InitClassAnswerServiceImpl
 * @Description
 * @Author hanxf
 * @Date 2019/5/14 17:13
 * @Version 1.0
 **/
@Service("initClassAnswerService")
@Slf4j
public class InitClassAnswerServiceImpl implements InitClassAnswerService {

    @Resource
    private ClassAnswerImfactMapper classAnswerImfactMapper;

    @Resource
    private ClassAnswerScoreMapper classAnswerScoreMapper;
    /**
     * 初始化班级答案影响
     *
     * @Author hanxf
     * @Date 17:25 2019/5/14
     * @param
     * @return void
    **/
    @Override
    public void initClassAnswerImfact(int classId) {
        log.info("初始化班级问题影响 classId={}",classId);
        int count = classAnswerImfactMapper.initClassAnswerImfact(classId);
        log.info("初始化班级问题影响 count={}",count);
    }

    /**
     * 初始化班级标准答案得分
     *
     * @Author hanxf
     * @Date 15:04 2019/5/14
     * @Param [classId]
     * @return void
     **/
    @Async
    @Override
    public void initClassAnswerScore(int classId){
        log.info("初始化班级标准答案得分 classId={}",classId);
        int count = classAnswerScoreMapper.initClassAnswerScore(classId);
        log.info("初始化班级标准答案得分 count={}",count);
    }
}
