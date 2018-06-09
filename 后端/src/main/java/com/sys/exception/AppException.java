package com.sys.exception;

/**
 * @author sys
 */
public class AppException extends RuntimeException {

    private String code;

    public AppException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
