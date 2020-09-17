package com.tzcpa.service.student.impl;

import com.tzcpa.service.student.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * @ClassName TestServiceImpl
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/19 14:44
 * @Version 6.0
 **/
@Slf4j
@Service
public class TestServiceImpl implements TestService {
    @Override
    @Transactional
    public String getString() throws Exception{
        log.info("aaaaaaaaa");
        throw new Exception("aa");
//        return "aaaa";
    }

    @PostConstruct
    public void testStart(){
        log.info("aaa");
    }
}
