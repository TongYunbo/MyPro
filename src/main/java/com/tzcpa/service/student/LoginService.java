package com.tzcpa.service.student;

import com.tzcpa.model.teacher.Teacher;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.model.treatment.UserInfo;

import java.util.Map;

public interface LoginService {

    Map<String, Object> login(String account, String password) throws Exception;

    int resetPassword(Teacher teacher, Team team, String passwordOld, String passwordNew) throws Exception;

    /**
     * @author:     wangzhangju
     * @date:       2019/5/14 9:31
     * @param:      null
     * @return:
     * @exception:
     * @description: 获取用户信息
     * @step:
     */
    public  UserInfo getLogUserInfo();

}
