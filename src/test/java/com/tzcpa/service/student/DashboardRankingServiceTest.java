package com.tzcpa.service.student;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * 测试排行榜
 * @author WangYao
 * 2019年5月23日
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class DashboardRankingServiceTest {

	@Autowired
	private DashboardRankingService dashboardRankingService;
	
	@Test
	public void test(){
		List<Map<String, Object>> operatingProfit = dashboardRankingService.getMonthlyGrossMargin(2011, 143, 109);
		log.info("operatingProfit = {}",JSON.toJSONString(operatingProfit));
	}
}
