package com.tzcpa.service.treatment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class InitClassIntermediateVariableServiceTest {

    @Autowired
    private InitClassIntermediateVariableService initClassIntermediateVariableService;

    @Test
    public void initClassIntermediateVariable() {
        initClassIntermediateVariableService.InitClassIntermediateVariable(84);
    }
}