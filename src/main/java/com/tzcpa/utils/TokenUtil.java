package com.tzcpa.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TokenUtil {

    /**
     * 生成token(格式为token:设备-加密的用户名-时间-六位随机数)
     *
     * @param username 用户登录名
     * @return
     */
    public static String generateToken(String username) {
        StringBuilder token = new StringBuilder();
        //加token:
//        token.append("token:");
        //加设备
//        UserAgent userAgent1 = UserAgent.parseUserAgentString(userAgent);
//        if (userAgent1.getOperatingSystem().isMobileDevice()){
//            token.append("MOBILE-");
//        } else {
//            token.append("PC-");
//        }
        //加加密的用户名
        token.append(DigestUtils.md5Hex(username) + "-");
        //加时间
        token.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "-");
        //加六位随机数111111-999999
        token.append(new Random().nextInt((999999 - 111111 + 1)) + 111111);
        System.out.println("token=" + token.toString());
        return token.toString();
    }
}
