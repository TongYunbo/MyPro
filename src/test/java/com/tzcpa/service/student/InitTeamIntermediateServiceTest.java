package com.tzcpa.service.student;

import com.tzcpa.service.treatment.InitTeamIntermediateService;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试排行榜
 * @author WangYao
 * 2019年5月23日
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class InitTeamIntermediateServiceTest {

	@Autowired
	private InitTeamIntermediateService initTeamIntermediateService;
	
//	@Test
//	public void test(){
//		Map<String, Object> map = new HashMap<String, Object>();
//		Set<String> set = new HashSet<>();
//		set.add("h6");
//		set.add("h8");
//		set.add("limousine");
//		map.put("classId", 98);
//		map.put("teamId", 43);
//		map.put("bDate", "2011-1");
//		map.put("eDate", "2011-2");
//		map.put("vehicleModel", set);
//		initTeamIntermediateService.updateTeamIntermediateOfFinalSales(map);
//	}
}
