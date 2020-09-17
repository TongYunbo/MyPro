package com.tzcpa.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class MyInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(MyInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {

        logger.info("MyInterceptor.preHandle 进入");
        logger.info("MyInterceptor.preHandle {}", request.getRequestURL());
        try {
            HttpSession session = request.getSession();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = null;
            logger.info("MyInterceptor.preHandle SessionId={}", session.getId());
            if (null == session.getAttribute("token")) {
                logger.info("MyInterceptor.preHandle 拦截 服务器无token");
                JSONObject result = new JSONObject();
                result.put("code", "-1");
                result.put("message", "服务器无token");
                writer = response.getWriter();
                writer.append(result.toString());
                return false;
            }
            String token = session.getAttribute("token").toString();
            logger.info("MyInterceptor.preHandle token={}", token);
            Cookie[] cookies = request.getCookies();
            if (null != cookies) {
                for (Cookie cookie : cookies) {
                    if ("userToken".equals(cookie.getName())) {
                        String userToken = cookie.getValue();
                        logger.info("MyInterceptor.preHandle userToken={}", userToken);
                        if (token.equals(userToken)) {
                            logger.info("MyInterceptor.preHandle 放行");
                            return true;
                        }
                    }
                }
            }
            logger.info("MyInterceptor.preHandle 拦截 token验证失败");
            JSONObject result = new JSONObject();
            result.put("code", "-1");
            result.put("message", "token验证失败");
            writer = response.getWriter();
            writer.append(result.toString());
            return false;
        } catch (Exception ee) {
            logger.info("MyInterceptor.preHandle 异常:", ee);
            return false;
        }
    }

}
