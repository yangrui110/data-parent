package com.yang.system.support.exception;

/**
 * 自定义的内部异常
 * */
public class InterException extends RuntimeException {

    private String message;
    private boolean success;
    private int code;

    public InterException(int code, String message) {
        this.message = message;
        this.success=false;
        this.code = code;
    }
    public InterException(int code, boolean success, String message) {
        this.message = message;
        this.success=success;
        this.code = code;
    }
    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        return success;
    }
}
