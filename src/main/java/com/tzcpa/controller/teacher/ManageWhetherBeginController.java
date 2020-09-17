package com.tzcpa.controller.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.service.teacher.ManageWhetherBeginService;

import lombok.extern.slf4j.Slf4j;

/**
 * 经营是否开始
 * @author WangYao
 * 2019年6月17日
 */
@RestController
@RequestMapping("manageWhetherBegin")
@Slf4j
public class ManageWhetherBeginController {

	@Autowired
	private ManageWhetherBeginService manageWhetherBeginService;
	
	/**
	 * 判断经营是否开始
	 * @author WangYao
	 * @date 2019年6月17日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("count")
	public ResponseResult<Integer> getManageWhetherBegin(@RequestBody JSONObject jsonObject){
		log.info("判断经营是否开始  jsonObject={}", jsonObject);
		int classId = jsonObject.getInteger("classId");
		Integer count = manageWhetherBeginService.getManageWhetherBegin(classId);
		log.info("判断经营是否开始  count={}", count);
		return new ResponseResult<Integer>(count);
	}
}
