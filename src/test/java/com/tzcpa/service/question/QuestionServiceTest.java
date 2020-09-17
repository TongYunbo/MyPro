package com.tzcpa.service.question;

import com.tzcpa.service.student.QuestionService;
import com.tzcpa.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @ClassName QuestionServiceTest
 * @Description
 * @Author wangzhangju
 * @Date 2019/6/10 12:34
 * @Version 6.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class QuestionServiceTest {

    @Resource
    private  QuestionService questionService;

    @Test
    public void test() throws Exception{
//        questionService.setMonthlyProfit(143,104, "2011-01-01",2010l,1l);
        questionService.setYearlyProfit(113,51,DateUtil.strToDate("yyyy-MM-dd","2011-01-01"));
    }
}
