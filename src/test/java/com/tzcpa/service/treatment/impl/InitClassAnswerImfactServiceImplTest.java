package com.tzcpa.service.treatment.impl;

import com.tzcpa.service.treatment.InitClassAnswerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InitClassAnswerImfactServiceImplTest {

    @Autowired
    private InitClassAnswerService initClassAnswerImfactService;

    @Test
    public void initClassAnswerImfact() {
        initClassAnswerImfactService.initClassAnswerImfact(1);
    }
}