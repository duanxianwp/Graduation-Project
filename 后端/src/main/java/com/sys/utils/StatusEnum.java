package com.sys.utils;

import org.omg.CORBA.UNKNOWN;

/**
 * @author sys
 */
public enum StatusEnum {

    UNKNOWN_EXCEPTION("0100", "未知异常"),
    SUCCESS("0200", "成功"),
    FAILED("0400", "失败"),
    ACC_OR_PWD_ERROR("0401", "账号或密码错误"),
    NO_PERMISSION("0402", "没有权限"),
    ORDER_NOT_EXIST("0403", "工单不存在"),
    UN_LOGIN("0404", "尚未登陆"),
    DATA_NOT_EXSIST("0405", "数据不存在"),
    DATA_AlREADY_EXSIST("0406", "数据已存在"),
    SERVER_ERROR("0500", "服务器错误"),
    ILLEGAL_CLIENT("0501", "客户端已过期"),
    PARAM_ERROR("0600", "参数错误"),
    EMAIL_SEND_FAILED("0701", "邮件发送失败");

    private String code;

    private String msg;

    StatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
