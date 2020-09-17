package com.tzcpa.controller;

import com.alibaba.fastjson.JSONObject;
import com.tzcpa.constant.MessageConstant;
import com.tzcpa.controller.result.ResponseResult;
import com.tzcpa.model.teacher.Teacher;
import com.tzcpa.model.teacher.Team;
import com.tzcpa.model.treatment.UserInfo;
import com.tzcpa.service.student.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 登录管理
 *
 * @author wangbj
 * <p>
 * 2019年5月5日
 */
@RestController
@RequestMapping("/")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    /**
     * 登录
     *
     * @param jsonObject:
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseResult login(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        logger.info("Controller.login 进入");
        try {
            String account = jsonObject.getString("account");
            String password = jsonObject.getString("password");
            logger.info("Controller.login account={} password={}", account, password);
            Map<String, Object> result = loginService.login(account, password);
            Object token = result.get("token");
            if (null != token && !"".equals(token.toString())) {

                if ("2".equals(result.get("isDel").toString())) {
                    logger.info("Controller.login 结束 用户已锁定");
                    return ResponseResult.fail("-2", MessageConstant.LOGIN_LOCKED);
                }

                request.getSession().setAttribute("token", token);
                request.getSession().setAttribute("teacher", result.get("teacher"));
                request.getSession().setAttribute("team", result.get("team"));
                request.getSession().setAttribute("stuRoleList", result.get("stuRoleList"));

                // 账户单点登录 前面的用户被挤掉
                String userAccount;
                if (result.get("teacher") != null) {
                    Teacher teacher = (Teacher) result.get("teacher");
                    userAccount = teacher.getAccount();
                } else {
                    Team team = (Team) result.get("team");
                    userAccount = team.getAccount();
                }
                HttpSession session = request.getSession();
                //logger.info("Controller.login sessionId={}", session.getId());
                ServletContext application = session.getServletContext();
                if (application.getAttribute(userAccount) != null) {
                    HttpSession sessionOld = (HttpSession) application.getAttribute(userAccount);
                    //logger.info("Controller.login sessionIdOld={}", sessionOld.getId());
                    //if (!session.getId().equals(sessionOld.getId())) {
                    application.removeAttribute(userAccount);
                    try {
                        sessionOld.invalidate();
                    } catch (Exception e) {
                        logger.error("Controller.login 销毁session异常-{}", e.getMessage());
                    }
                    //}
                }
                application.setAttribute(userAccount, session);

                logger.info("Controller.login 结束");
                return ResponseResult.success(result);
            } else {
                logger.info("Controller.login 结束 用户名或密码不正确");
                return ResponseResult.fail(MessageConstant.LOGIN_FAIL);
            }
        } catch (Exception ee) {
            logger.error("Controller.login 异常", ee);
            return ResponseResult.fail();
        }
    }


    @RequestMapping(value = "checkLogin", method = RequestMethod.POST)
    public ResponseResult login() {
        return ResponseResult.success();
    }
    /**
     * 退出登录
     *
     * @param :
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseResult logout(HttpServletRequest request) {
        logger.info("Controller.logout 进入");
        request.getSession().invalidate();
        logger.info("Controller.logout 结束");
        return ResponseResult.success();
    }

    /**
     * 修改密码
     *
     * @param jsonObject:
     * @return
     */
    @RequestMapping(value = "resetPassword", method = RequestMethod.POST)
    public ResponseResult resetPassword(HttpServletRequest request, @RequestBody JSONObject jsonObject) {
        logger.info("Controller.resetPassword 进入");
        try {
            String teacherId = jsonObject.getString("teacherId");
            String teamId = jsonObject.getString("teamId");
            logger.info("Controller.login teacherId={} teamId={}", teacherId, teamId);
            String passwordOld = jsonObject.getString("passwordOld");
            String passwordNew = jsonObject.getString("passwordNew");
//            logger.info("Controller.login passwordOld={} passwordNew={}", passwordOld, passwordNew);
            Teacher teacher = null;
            if (null != request.getSession().getAttribute("teacher")) {
                teacher = (Teacher) request.getSession().getAttribute("teacher");
            }
            Team team = null;
            if (null != request.getSession().getAttribute("team")) {
                team = (Team) request.getSession().getAttribute("team");
            }
            int result = loginService.resetPassword(teacher, team, passwordOld, passwordNew);
            logger.info("Controller.resetPassword 结束");
            if (0 == result) {
                // 修改密码后需要重新登录
                request.getSession().invalidate();
                return ResponseResult.success();
            } else if (1 == result) {
                return ResponseResult.fail(MessageConstant.PASSWORD_FAIL);
            } else {
                return ResponseResult.fail(MessageConstant.LOGIN_TIMEOUT);
            }
        } catch (Exception ee) {
            logger.error("Controller.login 异常", ee);
            return ResponseResult.fail();
        }
    }

    /**
     * @author: wangzhangju
     * @date: 2019/5/14 9:28
     * @param: null
     * @return:
     * @exception:
     * @description: 获取当前用户的登陆信息
     * @step:
     */
    @RequestMapping(value = "getLogUserInfo", method = RequestMethod.POST)
    public ResponseResult getLogUserInfo() {
        logger.info("LoginController.getLogUserInfo 进入");
        UserInfo info = loginService.getLogUserInfo();
        if (null == info) {
            return ResponseResult.fail("用户为空,请重新登陆!");
        }
        logger.info("LoginController.getLogUserInfo 结束 " + info.toString());
        return ResponseResult.success("获取用户信息成功", info);
    }

}
