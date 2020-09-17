package com.tzcpa.controller.student;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.service.student.RelativePlotService;
import com.tzcpa.utils.UserSessionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 雷达图
 * @author WangYao
 * 2019年5月21日
 */
@RestController
@RequestMapping("/relativePlot")
@Slf4j
public class RelativePlotController {

	@Autowired
	private RelativePlotService relativePlotService;
	
	/**
	 * 根据年限查看所有团队的总分数以及列表信息
	 * @author WangYao
	 * @date 2019年5月21日
	 * @param jsonObject
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public ResponseResult<Map<String, Object>> getRelativePlotList(){
		log.info("根据年限查看所有团队的总分数以及列表信息");
		Team team = UserSessionUtil.getTeam();
		HashMap<String, Object> map = relativePlotService.getRelativePlotList(team.getClassId(),team.getId());
		log.info("根据年限查看所有团队的总分数以及列表信息 map={} ",JSON.toJSONString(map));
		return new ResponseResult<Map<String,Object>>(map);
	}
	
	/**
	 * 查看雷达图和团队信息
	 * @author WangYao
	 * @date 2019年6月14日
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/getTeamInfo")
	public ResponseResult<Map<String, Object>> getTeamInfo(@RequestBody JSONObject jsonObject){
		log.info("查看雷达图和团队信息   jsonObject={}",JSON.toJSONString(jsonObject));
		Integer classId = jsonObject.getInteger("classId");
		Integer teamId = jsonObject.getInteger("teamId");
		HashMap<String, Object> map = relativePlotService.getTeamInfo(classId,teamId);
		log.info("查看雷达图和团队信息 map={} ",JSON.toJSONString(map));
		return new ResponseResult<Map<String,Object>>(map);
    }
}
