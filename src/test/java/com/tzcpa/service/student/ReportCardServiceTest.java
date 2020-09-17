package com.tzcpa.service.student;

import com.alibaba.fastjson.JSON;
import com.tzcpa.service.teacher.ReportCardService;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ReportCardServiceTest {

    @Autowired
    ReportCardService reportCardService;

    @Test
    public void getReportCardList() {
        Map<String, Object> reportCardList = reportCardService.getReportCardList(1);
        log.info("学生成绩 reportCardList={}", JSON.toJSONString(reportCardList));
    }
}
