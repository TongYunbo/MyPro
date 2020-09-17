package com.tzcpa.controller;

import com.tzcpa.service.student.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description
 * @Author wangzhangju
 * @Date 2019/7/9 18:36
 * @Version 6.0
 **/
@RestController
@RequestMapping("/")
public class TestController {

    @Autowired
    private TestService testService;
    @RequestMapping("/test")
    public String test(){
        try {
            testService.getString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "aaaaaaaaaa";
    }
}
