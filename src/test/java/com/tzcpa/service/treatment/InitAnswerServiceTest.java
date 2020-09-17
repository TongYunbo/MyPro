package com.tzcpa.service.treatment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class InitAnswerServiceTest {

    @Resource
    private InitClassAnswerService initAnswerService;

    @Test
    public void initClassAnswerScore() {
        initAnswerService.initClassAnswerScore(1);
    }
}