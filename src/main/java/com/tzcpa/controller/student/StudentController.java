package com.tzcpa.controller.student;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tzcpa.constant.MessageConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.student.TeamAndRoleVO;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.service.student.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 学员管理
 *
 * @author wangbj
 * <p>
 * 2019年5月5日
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    /**
     * 完善团队信息
     * @param request
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "editTeamAndRole", method = RequestMethod.POST)
    public ResponseResult editTeamAndRole(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        logger.info("Controller.editTeamAndRole 进入");
        try {
            Integer classId = jsonObject.getInteger("classId");
            Integer teamId = jsonObject.getInteger("teamId");
            logger.info("Controller.editTeamAndRole classId={} teamId={}", classId, teamId);
            String teamName = jsonObject.getString("teamName");
            String teamDesc = jsonObject.getString("teamDesc");
            String teamProspect = jsonObject.getString("teamProspect");
            logger.info("Controller.editTeamAndRole teamName={} teamDesc={} teamProspect={}", teamName, teamDesc, teamProspect);
            String ceoName = jsonObject.getString("ceoName");
            String cfoName = jsonObject.getString("cfoName");
            String croName = jsonObject.getString("croName");
            String cooName = jsonObject.getString("cooName");
            String cmoName = jsonObject.getString("cmoName");
            logger.info("Controller.editTeamAndRole ceoName={} cfoName={} croName={} cooName={} cmoName={}", ceoName, cfoName, croName, cooName, cmoName);

            Team team = null;
            if (null == request.getSession().getAttribute("team")) {
                logger.info("Controller.editTeamAndRole 结束 无用户登录信息");
                return ResponseResult.fail(MessageConstant.LOGIN_TIMEOUT);
            } else {
                team = (Team) request.getSession().getAttribute("team");
                if (!teamId.equals(team.getId())) {
                    logger.info("Controller.editTeamAndRole 结束 无用户登录信息");
                    return ResponseResult.fail(MessageConstant.LOGIN_TIMEOUT);
                }
            }

            TeamAndRoleVO teamAndRoleVO = new TeamAndRoleVO();
            teamAndRoleVO.setClassId(classId);
            teamAndRoleVO.setTeamId(teamId);
            teamAndRoleVO.setTeamName(teamName);
            teamAndRoleVO.setTeamDesc(teamDesc);
            teamAndRoleVO.setTeamProspect(teamProspect);
            teamAndRoleVO.setCeoName(ceoName);
            teamAndRoleVO.setCfoName(cfoName);
            teamAndRoleVO.setCroName(croName);
            teamAndRoleVO.setCooName(cooName);
            teamAndRoleVO.setCmoName(cmoName);
            teamAndRoleVO.setModifyPerson(team.getAccount());
            Map<String, Object> result = studentService.editTeamAndRole(teamAndRoleVO);

            request.getSession().setAttribute("team", result.get("team"));
            request.getSession().setAttribute("stuRoleList", result.get("stuRoleList"));
            logger.info("Controller.editTeamAndRole 结束");
            return ResponseResult.success(result);
        } catch (Exception ee) {
            logger.error("Controller.login 异常", ee);
            return ResponseResult.fail();
        }
    }

    /**
     * @author:     wangzhangju
     * @date:       2019/5/23 15:57
     * @param:      null
     * @return:
     * @exception:
     * @description: 分页插件查询班级下的team
     * @step:
     */
    @RequestMapping(value = "getTeamByClass", method = RequestMethod.POST)
    public ResponseResult getTeamByClass( @RequestBody JSONObject jsonObject) {
        PageInfo<Team> pageInfo=null;
        try {
            int pageNum = jsonObject.getInteger("pageNum");
            PageHelper.startPage(pageNum, 10);
            List<Team> teamList = studentService.getTeamByClass(jsonObject.getInteger("classId"));
            pageInfo = new PageInfo<Team>(teamList);
        }catch (Exception e){
            logger.error("",e);
        }
        return ResponseResult.success(pageInfo);
    }

}
