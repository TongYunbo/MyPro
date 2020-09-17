package com.tzcpa.service.student;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TeamStrategyMapServiceTest {
    @Autowired
    private TeamStrategyMapService teamStrategyMapService;
    @Test
    public void initTeamStrategyMap() throws Exception{
        Map<String,String> map = new HashMap<>();
        map.put("classId","1");
        map.put("teamId","2");
        map.put("year","2012");
        map.put("strategicSelect","A");
        teamStrategyMapService.initTeamStrategyMap(map);
    }
}