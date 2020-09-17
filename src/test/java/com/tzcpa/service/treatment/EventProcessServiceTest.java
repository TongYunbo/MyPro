package com.tzcpa.service.treatment;

import com.tzcpa.service.teacher.EventProcessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
@EnableAsync
public class EventProcessServiceTest {

    @Autowired
    private InitEventProcessService initEventProcessService;

    @Test
    public void testInitEventProcess(){

        initEventProcessService.initEventProcess(1);
    }

}