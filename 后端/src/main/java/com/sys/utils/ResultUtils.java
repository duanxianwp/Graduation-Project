package com.sys.utils;

/**
 * @author sys
 */
public class ResultUtils {


    public static Result success() {

        return success(null);
    }

    public static Result success(Object data) {

        Result result = new Result();
        result.setCode(StatusEnum.SUCCESS.getCode());
        result.setMsg(StatusEnum.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static Result error() {

        Result result = new Result();
        result.setCode(StatusEnum.FAILED.getCode());
        result.setMsg(StatusEnum.FAILED.getMsg());
        return result;
    }

    public static Result error(String code, String msg) {

        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
