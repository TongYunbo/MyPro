package com.tzcpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tzcpa.model.PageBean;
import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.service.teacher.ClassService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BakerApplicationTests {
	@Autowired
	ClassService classService;
	
	
	@Test
	public void contextLoads() {
		
		PageBean<Clazz> classBean = classService.findByPage(1);
		System.out.println(classBean);
	}

}
