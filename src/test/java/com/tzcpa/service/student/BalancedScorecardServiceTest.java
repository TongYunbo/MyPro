package com.tzcpa.service.student;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

/**
 * 平衡记分卡
 * @author WangYao
 * 2019年6月4日
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class BalancedScorecardServiceTest {

	@Autowired
	private BalancedScorecardService balancedScorecardService;
	
	@Test
	public void Test(){
		List<Map<String, Object>> teamList = balancedScorecardService.getBalancedScorecardYear(171, 170);
		log.info("平衡记分卡  teamList={}", teamList);
	}
}
