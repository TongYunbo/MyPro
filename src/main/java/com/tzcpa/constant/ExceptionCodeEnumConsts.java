package com.tzcpa.constant;


/**
 *
 */
public enum ExceptionCodeEnumConsts {

    TOKEN_ERROR("-2", "token错误"),
    PARAM_NOT_NULL("OMR100001", "参数不能为空"),
	ERROR_IMPACT_HANDLE("-1", "做题处理错误影响参数不足"),
	ERROR_DELAYED_UPDATE_HANDLE("-1", "执行延迟任务参数不足");


    //错误码
    private String code;
    //错误消息
    private String msg;

    ExceptionCodeEnumConsts(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }

}
