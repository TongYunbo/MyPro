package com.tzcpa.controller.teacher;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.service.teacher.ReportCardService;

import lombok.extern.slf4j.Slf4j;

/**
 * 学生成绩单列表
 * @author WangYao
 * 2019年5月27日
 */
@RestController
@RequestMapping("/reportCard")
@Slf4j
public class ReportCardController {
	
	@Autowired
	private ReportCardService reportCardService;

	/**
	 * 查看学生成绩单列表
	 * @author WangYao
	 * @date 2019年5月27日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/list")
	public ResponseResult<Map<String, Object>> getReportCardList(@RequestBody JSONObject jsonObject){
		log.info("查看学生成绩单列表 jsonObject={}", jsonObject);
		int classId = (int)jsonObject.get("classId");
		Map<String, Object> map = reportCardService.getReportCardList(classId);
		log.info("查看学生成绩单列表 map={}", map);
		return new ResponseResult<Map<String, Object>>(map);
	}
}
