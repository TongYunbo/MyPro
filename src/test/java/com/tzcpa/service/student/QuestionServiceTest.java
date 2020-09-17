package com.tzcpa.service.student;

import com.alibaba.fastjson.JSON;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.model.treatment.ClassQuestionOptionDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;

    @Test
    public void getQuestionOptions() {
        List<Integer> questionIds = new ArrayList<Integer>();
        questionIds.add(36);
        questionIds.add(37);
        questionIds.add(38);
        questionIds.add(39);
        questionIds.add(40);
        questionIds.add(41);
        questionIds.add(42);
        questionIds.add(43);
        System.out.println(JSON.toJSONString(questionIds));
        List<ClassQuestionOptionDO> questionOptions = questionService.getQuestionOptions(questionIds);
        System.out.println(questionOptions);
    }
}