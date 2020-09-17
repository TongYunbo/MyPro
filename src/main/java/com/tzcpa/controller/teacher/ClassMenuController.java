package com.tzcpa.controller.teacher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tzcpa.constant.TeacherInfoConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.treatment.ClassMenuDO;
import com.tzcpa.service.treatment.InitClassMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 团队菜单控制
 * @author WangYao
 * 2019年5月28日
 */
@RestController
@RequestMapping("/classMenu")
@Slf4j
public class ClassMenuController {

	@Autowired
	private InitClassMenuService classMenuService;
	
	/**
	 * 查询菜单列表
	 * @author WangYao
	 * @date 2019年5月28日
	 */
	@RequestMapping("/list")
	public ResponseResult getClassMenu(@RequestBody JSONObject jsonObject){
		log.info("进入团队菜单控制  jsonObject={}", JSON.toJSONString(jsonObject));
		int stuOrTea = jsonObject.getInteger("stuOrTea");
		int classId = jsonObject.getInteger("classId");
		List<ClassMenuDO> list = classMenuService.getClassMenu(classId, stuOrTea);
		log.info("团队菜单控制 list={}",list);
		return ResponseResult.success("查询是否开启排行榜成功",list);
	}
	
	/**
	 * 修改是否显示排行榜
	 * @author WangYao
	 * @date 2019年5月29日
	 * @param jsonObject
	 */
	@RequestMapping("/updateIsShow")
	public ResponseResult<String> updateIsShow(@RequestBody JSONObject jsonObject){
		log.info("修改是否显示排行榜  jsonObject={}", jsonObject);
		int classId = Integer.valueOf(jsonObject.get("classId").toString());
		int isShow = Integer.valueOf(jsonObject.get("isShow").toString());
		boolean b = classMenuService.updateIsShow(classId, isShow);
		if(isShow == TeacherInfoConstant.ISSHOWYES){
			return new ResponseResult<String>("0","打开成功！");
		}else{
			return new ResponseResult<String>("0","关闭成功！");
		}
	}
}
