package com.tzcpa.service.treatment.impl;

import com.alibaba.fastjson.JSON;
import com.tzcpa.mapper.treatment.ClassCostQuoteMapper;
import com.tzcpa.model.treatment.ClassCostQuoteDO;
import com.tzcpa.service.treatment.InitClassCostQuoteService;
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
 * @ClassName InitClassCostQuoteServiceImpl
 * @Description  初始化班级成本引用
 * @Author hanxf
 * @Date 2019/5/17 11:47
 * @Version 1.0
 **/
@Service(value = "initClassCostQuoteService")
@Slf4j
public class InitClassCostQuoteServiceImpl implements InitClassCostQuoteService {

    @Resource
    private ClassCostQuoteMapper classCostQuoteMapper;

    @Async
    @Override
    public void initClassCostQuote(int classId) {
        log.info("初始化班级成本引用 classId={}",classId);
        int count = classCostQuoteMapper.initClassCostQuote(classId);
        log.info("初始化班级成本引用 count={}",count);
    }

}
