package com.tzcpa.service.treatment.impl;

import com.tzcpa.mapper.treatment.ClassBalancedScorecardTargetValueMapper;
import com.tzcpa.service.treatment.InitClassBalancedScorecardTargetValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName InitClassBalancedScorecardTargetValueImpl
 * @Description
 * @Author hanxf
 * @Date 2019/7/15 18:55
 * @Version 1.0
 **/
@Service("initClassBalancedScorecardTargetValue")
@Slf4j
public class InitClassBalancedScorecardTargetValueServiceImpl implements InitClassBalancedScorecardTargetValueService {

    @Resource
    private ClassBalancedScorecardTargetValueMapper classBalancedScorecardTargetValueMapper;

    @Override
    public void initClassBalancedScorecardTargetValue(int classId) {
        log.info("初始化班级平衡记分卡目标值 classId={}",classId);
        int count = classBalancedScorecardTargetValueMapper.initClassBalancedScorecardTargetValue(classId);
        log.info("初始化班级平衡记分卡目标值 count={}",count);
    }
}
