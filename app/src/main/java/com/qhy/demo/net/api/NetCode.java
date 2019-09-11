package com.qhy.demo.net.api;

/**
 * Created by qhy on 2019/9/9.
 */
public enum NetCode {

    E1000(1000, "未知错误"),

    E2000(2000, ""),
    E2001(2001, "request转换错误1"),
    E2002(2002, "request转换错误2"),

    E3000(3000, "解析错误"),
    E3001(3001, "response解析错误1"),
    E3002(3002, "response解析错误2"),
    E3003(3003, "response解析错误3"),
    E3004(3004, "responseClass错误"),

    E4000(4000, ""),
    E4001(4001, "网络错误1"),
    E4002(4002, "网络错误2"),
    E4003(4003, "网络错误3"),
    E4004(4004, "网络错误4"),
    E4005(4005, "网络错误5"),
    E4006(4006, "网络错误6"),

    S99(99, "登录失效"),
    S1(1, "失败"),
    S0(0, "成功");

    private int code;
    private String message;

    NetCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
