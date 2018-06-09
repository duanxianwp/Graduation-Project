package com.sys.utils;

import com.sys.exception.AppException;

public class ServiceUtils {

    /**
     * 鉴权
     * @param level
     */
    public static void Authentication(Integer level,Integer powerLevel) {
        if (level < powerLevel) {
            throw new AppException(StatusEnum.NO_PERMISSION.getCode(), StatusEnum.NO_PERMISSION.getMsg());
        }
    }
}
