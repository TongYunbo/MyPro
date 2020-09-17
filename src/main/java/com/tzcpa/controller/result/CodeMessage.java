package com.tzcpa.controller.result;


import lombok.Data;

import java.io.Serializable;

@Data
public class CodeMessage implements Serializable {

    private static final long serialVersionUID = -273144807024673938L;

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
