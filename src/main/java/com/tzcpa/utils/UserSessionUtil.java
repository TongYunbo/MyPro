package com.tzcpa.utils;

import com.tzcpa.model.teacher.StuRole;
import com.tzcpa.model.teacher.Teacher;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.model.treatment.UserInfo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserSessionUtil
 * @Description
 * @Author wangzhangju
 * @Date 2019/5/9 15:32
 * @Version 6.0
 **/
public class UserSessionUtil {

    public static Team getTeam() {
        Team team = new Team();
        HttpSession session = getSession();
        if (null != session) {
            team = (Team) session.getAttribute("team");
        }
        return team;
    }

    public static Teacher getTeacher() {
        Teacher teacher = new Teacher();
        HttpSession session = getSession();
        if (null != session) {
            teacher = (Teacher) session.getAttribute("teacher");
        }
        return teacher;
    }

    /**
     * @return SocketUser --获取webSocket用户
     */
    public static UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        HttpSession session = getSession();
        if (null != session) {
            Team team = (Team) session.getAttribute("team");
            Teacher teacher = (Teacher) session.getAttribute("teacher");
            List<StuRole> stuRoleList = (List<StuRole>) session.getAttribute("stuRoleList");
            if (team != null) {
                userInfo.setAccount(team.getAccount());
                userInfo.setClassId(team.getClassId());
                userInfo.setTeamId(team.getId());
                userInfo.setName(team.getTeamName());
                userInfo.setRole("team");
                userInfo.setId("team" + team.getId());
                userInfo.setTable(String.valueOf(team.getClassId()));
                userInfo.setTeamName(team.getTeamName());
                userInfo.setTeamDesc(team.getTeamDesc());
                userInfo.setTeamProspect(team.getTeamProspect());

                for(StuRole stuRole : stuRoleList) {
                    if ("CFO".equals(stuRole.getRoleName())) {
                        userInfo.setCfoName(stuRole.getUserName());
                    }
                }
            } else if (teacher != null) {
                userInfo.setAccount(teacher.getAccount());
//                userInfo.setClassId(91);
                userInfo.setTeacherId(teacher.getId());
                userInfo.setName(teacher.getName());
                userInfo.setRole("teacher");
//                userInfo.setId("teacher"+91);
//                userInfo.setTable(String.valueOf(91));
            }

        }
        return userInfo;
    }

    public static HttpSession getSession() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    }
}
