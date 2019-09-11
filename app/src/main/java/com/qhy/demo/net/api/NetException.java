package com.qhy.demo.net.api;

/**
 * Created by qhy on 2019/9/9.
 */
public class NetException extends Exception {

    private int mCode;
    private String mMessage;

    NetException(NetCode apiCode) {
        super(apiCode.getMessage());
        this.mCode = apiCode.getCode();
        this.mMessage = apiCode.getMessage();
    }

    public NetException(int code, String message) {
        super(message);
        this.mCode = code;
        this.mMessage = message;
    }

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

}
