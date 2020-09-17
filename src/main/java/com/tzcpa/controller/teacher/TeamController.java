/**
 *
 */
package com.tzcpa.controller.teacher;

import com.alibaba.fastjson.JSONObject;
import com.tzcpa.constant.Constant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.teacher.Clazz;
import com.tzcpa.model.teacher.Teacher;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.service.teacher.TeamService;
import com.tzcpa.utils.UserSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理页面---学员管理
 *
 * @author WangYao
 * <p>
 * 2019年5月5日
 */
@RestController
@RequestMapping("teacher/team")
@Slf4j
public class TeamController {

    @Autowired
    private TeamService teamService;

    /**
     * 查看团队列表
     *
     * @param jsonObject:currentPage 当前页
     * @return
     */
    @RequestMapping("/list")
    public ResponseResult<Map<String, Object>> getTeamList(@RequestBody JSONObject jsonObject) {
    	log.info("进入团队列表 参数jsonObject={}",jsonObject);
        int currentPage = (int) jsonObject.get("currentPage");
        HashMap<String, Object> map = teamService.findByPage(currentPage);
        return new ResponseResult<Map<String, Object>>(map);
    }

    /**
     * 添加团队之班级回显
     *
     * @return
     */
    @RequestMapping("/getClazzInfo")
    public ResponseResult<Map<String, Object>> getClazzInfo() {
        Teacher teacher = UserSessionUtil.getTeacher();
        String teacherAccount = teacher.getAccount();
        if(Constant.ACCOUNT_ADMIN.equals(teacherAccount)){
            teacherAccount = null;
        }
        List<Clazz> clazzLists = teamService.getClazzInfo(teacherAccount);
        HashMap<String, Object> map = new HashMap<>();
        map.put("clazzLists", clazzLists);
        return new ResponseResult<Map<String, Object>>(map);
    }


    /**
     * 添加团队信息
     *
     * @param jsonObject account：团队账号；classId：团队所属班级id
     * @return
     */
    @RequestMapping("/insert")
    public ResponseResult<String> insertTeamInfo(@RequestBody JSONObject jsonObject) {
    	log.info("添加团队信息 参数jsonObject={}",jsonObject);
        Team team = new Team();
        String account = jsonObject.getString("account");
        int classId = jsonObject.getInteger("classId");
        team.setAccount(account);
        team.setClassId(classId);

        try {
            boolean b = teamService.insertTeamInfo(team);
            if (b == true) {
                return new ResponseResult<String>("0", "添加成功！");
            } else {
                return new ResponseResult<String>("0", "账号不能重复！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseResult<String>("0", "添加成功！");
    }

    /**
     * 重置团队密码为默认密码
     *
     * @param jsonObject:id 团队id
     * @return
     */
    @RequestMapping("/reset")
    public ResponseResult<String> resetTeamPwd(@RequestBody JSONObject jsonObject) {
    	log.info("重置团队密码为默认密码 参数jsonObject={}",jsonObject);
        int id = (int) jsonObject.get("id");
        boolean b = teamService.resetTeamPwd(id);
        if (b == true) {
            return new ResponseResult<String>("0", "操作成功", "true");
        } else {
            return new ResponseResult<String>("0", "操作失败", "false");
        }
    }

    /**
     * 停用团队
     *
     * @param jsonObject:id 团队id；isDel：当前团队的是否删除状态 0：启用状态；2：禁用状态
     * @return
     */
    @RequestMapping("/blockUp")
    public ResponseResult<String> blockUpTeam(@RequestBody JSONObject jsonObject) {
    	log.info("停用团队 参数jsonObject={}",jsonObject);
    	int id = (int) jsonObject.get("id");
    	int isdel = (int) jsonObject.get("isDel");
    	int isDel = teamService.blockUpTeam(id, isdel);
        if (isDel == 0) {
            return new ResponseResult<String>("0", "已启用");
        } else {
            return new ResponseResult<String>("0", "已禁用");
        }
    }

    /**
     * 根据团队id查看班级信息
     *
     * @param jsonObject ：id ：团队id
     * @return
     */
    @RequestMapping("/getClazzInfoById")
    public ResponseResult<Map<String, Object>> getClazzInfoById(@RequestBody JSONObject jsonObject) {
    	log.info("根据团队id查看班级信息 参数jsonObject={}",jsonObject);
    	int id = (int) jsonObject.get("id");
        List<Clazz> clazzLists = teamService.getClazzInfoById(id);
        HashMap<String, Object> map = new HashMap<>();
        map.put("clazzLists", clazzLists);
        return new ResponseResult<Map<String, Object>>(map);
    }

    /**
     * 修改团队信息
     *
     * @param jsonObject id：团队id，account：团队账号；classId：团队所属班级id
     * @return
     */
    @RequestMapping("/updateTeamInfo")
    public ResponseResult<String> updateTeamInfo(@RequestBody JSONObject jsonObject) {
    	log.info("修改团队信息 参数jsonObject={}",jsonObject);
    	int id = (int) jsonObject.get("id");
        String account = (String) jsonObject.get("account");
        int classId = (int) jsonObject.get("classId");
        boolean b = teamService.updateTeamInfo(id, account, classId);
        if (b == true) {
            return new ResponseResult<String>("0", "修改成功！");
        } else {
            return new ResponseResult<String>("0", "团队名称已存在！");
        }
    }
}
