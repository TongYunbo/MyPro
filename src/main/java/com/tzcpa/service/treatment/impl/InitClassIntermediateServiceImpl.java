package com.tzcpa.service.treatment.impl;

import com.alibaba.fastjson.JSON;
import com.tzcpa.mapper.treatment.*;
import com.tzcpa.model.treatment.*;
import com.tzcpa.service.treatment.InitClassCostQuoteService;
import com.tzcpa.service.treatment.InitClassIntermediateService;
import com.tzcpa.utils.ExcelSearchFunctions;
import com.tzcpa.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName InitBasicDataServiceImpl
 * @Description 中间表 基准数据初始化
 * @Author hanxf
 * @Date 2019/5/15
 * @Version 1.0
 **/
@Service("initClassIntermediateService")
@Slf4j
public class InitClassIntermediateServiceImpl implements InitClassIntermediateService {

    @Resource
    private ClassIntermediateMapper classIntermediateMapper;


    @Async
    @Override
    public void initClassIntermediate(int classId) {

        log.info("初始化中间表基础数据 classId={}",classId);
        int count = classIntermediateMapper.initClassIntermediate(classId);
        log.info("初始化中间表基础数据 count={}",count);
    }



}
