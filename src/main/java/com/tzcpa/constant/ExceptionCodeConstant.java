package com.tzcpa.constant;

/**
 * 定义业务错误码
 */
public class ExceptionCodeConstant {

    /*********** 短信验证码 *************/
    /**
     * 短信验证码发送超过当天最大次数
     */
    public static final String NUM_OVER_DAY = "OMR400001";
    /**
     * 短信验证码发送超过每小时最大次数
     */
    public static final String SMS_NUM_OVER_HOUR = "OMR400002";

}
