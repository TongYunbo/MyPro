package com.tzcpa.service.treatment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 初始化固定值比例信息
 * @author WangYao
 * 2019年5月22日
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class InitTeamBalancedScorecardServiceTest {
	
	@Autowired
	private InitClassBalanceSheetService initTeamBalancedScorecardService;

	/**
	 * 测试初始化team的平衡记分卡
	 */
	@Test
	public void test(){
		initTeamBalancedScorecardService.initClassBalanceSheet(1);
	}
}
