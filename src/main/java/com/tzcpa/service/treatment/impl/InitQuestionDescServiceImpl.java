package com.tzcpa.service.treatment.impl;

import com.tzcpa.mapper.treatment.ClassQuestionDescMapper;
import com.tzcpa.service.treatment.InitQuestionDescService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName QuestionServiceImpl
 * @Description 班级题库初始化实现
 * @Author hanxf
 * @Date 2019/5/6 11:25
 * @Version 1.0
 **/
@Service("initQuestionService")
public class InitQuestionDescServiceImpl implements InitQuestionDescService {

    private Logger logger = LoggerFactory.getLogger(InitQuestionDescServiceImpl.class);

    @Resource
    private ClassQuestionDescMapper classQuestionDescMapper;
    /**
     * @return void
     * @Author hanxf
     * @Description 初始化班级的题库
     * @Date 17:45 2019/5/6
     * @Param [classId] 班级id
     **/
    @Async
    @Override
    public void initClassQuestionDesc(int classId) {
        logger.info("初始化班级题库 classId={}", classId);
        int count = classQuestionDescMapper.initClassQuestionDesc(classId);
        logger.info("初始化班级题库 count={}", count);
    }

}
