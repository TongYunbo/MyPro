package com.tzcpa.service.treatment.impl;

import com.tzcpa.mapper.treatment.ClassSalesUnivalenceMapper;
import com.tzcpa.service.treatment.InitClassSaleUnivalenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName InitClassSaleUnivalenceServiceImpl
 * @Description 初始化销售量配置表
 * @Author hanxf
 * @Date 2019/5/17 11:57
 * @Version 1.0
 **/
@Service("initClassSaleUnivalenceService")
@Slf4j
public class InitClassSaleUnivalenceServiceImpl implements InitClassSaleUnivalenceService {

    @Resource
    private ClassSalesUnivalenceMapper classSalesUnivalenceMapper;

    @Async
    @Override
    public void initClassSaleUnivalence(int classId) {
        log.info("初始化销量单价配置 classId={}",classId);
        int count = classSalesUnivalenceMapper.initClassSaleUnivalence(classId);
        log.info("初始化销量单价配置 count={}",count);
    }
}
