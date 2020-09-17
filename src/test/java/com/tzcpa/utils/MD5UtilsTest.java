package com.tzcpa.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class MD5UtilsTest{

    @Test
    public void encrypt() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String res = MD5Utils.getEncryptedPwd("1234");
        System.out.println("MD5 Pwd is : "+res);

    }

}