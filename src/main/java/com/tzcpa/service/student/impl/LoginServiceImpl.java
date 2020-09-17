package com.tzcpa.service.student.impl;

import com.tzcpa.constant.Constant;
import com.tzcpa.mapper.teacher.StuRoleMapper;
import com.tzcpa.mapper.teacher.TeacherMapper;
import com.tzcpa.mapper.teacher.TeamMapper;
import com.tzcpa.model.teacher.StuRole;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.model.treatment.UserInfo;
import com.tzcpa.service.student.LoginService;
import com.tzcpa.utils.MD5Utils;
import com.tzcpa.utils.TokenUtil;
import com.tzcpa.utils.UserSessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.tzcpa.model.teacher.Teacher;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录管理
 *
 * @author wangbj
 * <p>
 * 2019年4月28日
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private StuRoleMapper stuRoleMapper;

    @Resource
    private TeacherMapper teacherMapper;

    /**
     * 登录
     *
     * @param account
     * @param password
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> login(String account, String password) throws Exception {
        logger.info("Service.login 进入");
        Map<String, Object> result = new HashMap<>();
        boolean checked = false;
        if (Constant.ACCOUNT_ADMIN.equals(account) || (account.contains("@") && account.contains("."))) {
            logger.info("Service.login 教师登录");
            Teacher teacher = teacherMapper.teacherLogin(account);
            if (teacher != null) {
                logger.info("Service.login teacher={}", teacher);
                checked = MD5Utils.validPassword(password, teacher.getPassword());
                logger.info("Service.login checked={}", checked);
                if (checked) {
                    result.put("isDel", teacher.getIsDel());
                    result.put("teacher", teacher);
                }
            }
        } else {
            logger.info("Service.login 学员登录");
            Team team = teamMapper.teamLogin(account);
            if (team != null) {
                logger.info("Service.login team={}", team);
                checked = MD5Utils.validPassword(password, team.getPassword());
                logger.info("Service.login checked={}", checked);
                if (checked) {
                    result.put("isDel", team.getIsDel());
                    result.put("team", team);

                    StuRole stuRole = new StuRole();
                    stuRole.setClassId(team.getClassId());
                    stuRole.setTeamId(team.getId());
                    List<StuRole> stuRoleList = stuRoleMapper.selectStuRole(stuRole);
                    result.put("stuRoleList", stuRoleList);
                }
            }
        }
        if (checked) {
            result.put("token", TokenUtil.generateToken(account));
        }
        logger.info("Service.login 结束");
        return result;
    }

    /**
     * 修改密码
     *
     * @param teacher
     * @param team
     * @param passwordOld
     * @param passwordNew
     * @return
     * @throws Exception
     */
    @Override
    public int resetPassword(Teacher teacher, Team team, String passwordOld, String passwordNew) throws Exception {
        logger.info("Service.resetPassword 进入");
        if (teacher != null) {
            logger.info("Service.resetPassword 教师修改密码");
            if (MD5Utils.validPassword(passwordOld, teacher.getPassword())) {
                teacher.setPassword(MD5Utils.getEncryptedPwd(passwordNew));
                teacher.setModifyPerson(teacher.getAccount());
                teacher.setId(teacher.getId());
                teacherMapper.updateTeacher(teacher);
                logger.info("Service.resetPassword 结束 密码修改成功");
                return 0;
            } else {
                logger.info("Service.resetPassword 结束 原密码验证失败");
                return 1;
            }
        }
        if (team != null) {
            logger.info("Service.resetPassword 学员修改密码");
            if (MD5Utils.validPassword(passwordOld, team.getPassword())) {
                team.setPassword(MD5Utils.getEncryptedPwd(passwordNew));
                team.setModifyPerson(team.getAccount());
                team.setId(team.getId());
                teamMapper.updateTeam(team);
                logger.info("Service.resetPassword 结束 密码修改成功");
                return 0;
            } else {
                logger.info("Service.resetPassword 结束 原密码验证失败");
                return 1;
            }
        }
        logger.info("Service.resetPassword 结束 无用户登录信息");
        return 2;
    }

    @Override
    public UserInfo getLogUserInfo() {
        return UserSessionUtil.getUserInfo();
    }

}
