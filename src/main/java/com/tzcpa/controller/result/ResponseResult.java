package com.tzcpa.controller.result;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ResponseResult<T> {

    private static final String RETURN_CODE_SUCCESS = "0";
    private static final String RETURN_CODE_ERROR = "-1";
    private static final String RETURN_MSG_SUCCESS = "操作成功！";
    private static final String RETURN_MSG_ERROR = "操作失败！";
    private static final String RETURN_DATA = "data";
    private static final String RETURN_META = "meta";

    private CodeMessage meta;
    private T data;

    public ResponseResult(T data) {
        this("0", "操作成功！", data);
    }

    public ResponseResult(String code, String message) {
        this(code, message, null);
    }

    public ResponseResult(String code, String message, T data) {
        this.meta = new CodeMessage();
        this.meta.setCode(code);
        this.meta.setMessage(message);
        this.data = data;
    }

    public static <T> ResponseResult success(T data) {
        return new ResponseResult(data);
    }

    public static <T> ResponseResult success(String msg, T data) {
        return new ResponseResult("0", msg, data);
    }

    public static ResponseResult success(String msg) {
        return new ResponseResult("0", msg, (Object) null);
    }

    public static ResponseResult success() {
        return new ResponseResult("0", "操作成功！", (Object) null);
    }

    public static <T> ResponseResult success(String code, String msg, T data) {
        return new ResponseResult(code, msg, data);
    }

    public static ResponseResult fail() {
        return new ResponseResult("-1", "操作失败！", (Object) null);
    }

    public static ResponseResult fail(String msg) {
        return new ResponseResult("-1", msg, (Object) null);
    }

    public static ResponseResult fail(String code, String msg) {
        return new ResponseResult(code, msg, (Object) null);
    }

    public static <T> ResponseResult fail(String code, String msg, T data) {
        return new ResponseResult(code, msg, data);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE, false);
    }

    private static <T> JSONObject getJSON(String code, String msg, T data) {
        JSONObject jo = new JSONObject();
        CodeMessage meta = new CodeMessage();
        meta.setCode(code);
        meta.setMessage(msg);
        jo.put("meta", meta);
        if (data != null) {
            jo.put("data", data);
        }

        return jo;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public CodeMessage getMeta() {
        return this.meta;
    }

    public void setMeta(CodeMessage meta) {
        this.meta = meta;
    }
}