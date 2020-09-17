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
 * 测试排行榜
 * @author WangYao
 * 2019年6月5日
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RankingListServiceTest {

	@Autowired
	private RankingListService rankingListService;
	
	@Test
	public void Test(){
		List<Map<String, Object>> syntheticAbility = rankingListService.getSyntheticAbility(31);
		log.info("综合能力排行   syntheticAbility={}",syntheticAbility);
	}
}
