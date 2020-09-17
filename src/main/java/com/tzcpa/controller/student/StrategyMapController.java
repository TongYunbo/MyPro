package com.tzcpa.controller.student;

import com.alibaba.fastjson.JSONObject;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.service.student.StrategyMapService;
import com.tzcpa.utils.UserSessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName StrategyMapController
 * @Description
 * @Author hanxf
 * @Date 2019/5/27 16:55
 * @Version 1.0
 **/
@RestController
public class StrategyMapController {

    @Autowired
    private StrategyMapService strategyMapService;

    /**
     * 查询战略地图
     *
     * @Author hanxf
     * @Date 14:12 2019/5/28
     * @param jsonObject
     * @return com.tzcpa.controller.result.ResponseResult
    **/
    @PostMapping(value = "getStrategyMapByYear")
    public ResponseResult getStrategyMapByYear(@RequestBody JSONObject jsonObject){
        Team team = UserSessionUtil.getTeam();
        jsonObject.put("classId",team.getClassId());
        jsonObject.put("teamId",team.getId());
        String url = strategyMapService.getStrategyMapByYear(jsonObject);
        return ResponseResult.success("查询战略地图成功",url);
    }

    @PostMapping(value = "getStrategyMapYear")
    public ResponseResult getStrategyMapYear(){
        try {
            Team team = UserSessionUtil.getTeam();
            int classId = team.getClassId();
            int teamId = team.getId();
            List<Integer> answerYearList = strategyMapService.getStrategyMapYear(classId, teamId);
            return ResponseResult.success("查询已答题年份",answerYearList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.fail("查询已答题年份失败");
    }
}
